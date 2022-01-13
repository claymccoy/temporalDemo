package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityOptions
import io.temporal.client.WorkflowExecutionAlreadyStarted
import io.temporal.workflow.Async
import io.temporal.workflow.ChildWorkflowOptions
import io.temporal.workflow.Workflow
import java.time.Duration

class CharCounterWorkflowImpl : CharCounterWorkflow {

    private val activities = Workflow.newActivityStub(
            CharActivities::class.java,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build())

    var charStateWorflowsByChar: Map<Char, CharStateWorkflow> = emptyMap()
    var messageHistory: List<HistoryMessage> = emptyList()
    var exit = false

    override fun start(options: CharacterCountWorkflowOptions) {
        charStateWorflowsByChar = options.validCharacters.map { char ->
            (char to Workflow.newChildWorkflowStub(CharStateWorkflow::class.java,
                    ChildWorkflowOptions.newBuilder()
                            .setWorkflowId("CharState for $char")
//                            .setTaskQueue("CharState")
                            .build()))
        }.toMap()
        charStateWorflowsByChar.forEach { char, workflow ->
            try {
                Async.function(workflow::start, CharStateWorkflowOptions(char))
            } catch (e: WorkflowExecutionAlreadyStarted) {
                println("Already running as " + e.execution)
            }
        }
        while (true) {
            Workflow.await { exit }
            if (exit) {
                return
            }
        }
    }

    override fun count(message: String) {
        val messageCharCounts = activities.count(message)
        messageHistory = messageHistory + HistoryMessage(message, messageCharCounts)
        messageCharCounts.forEach { charCount ->
            charStateWorflowsByChar[charCount.character]?.add(charCount.count)
        }
    }

    override fun exit() {
        exit = true
    }

    override fun getHistory(limit: Int): List<HistoryMessage> {
        return messageHistory.takeLast(limit).reversed()
    }

}
