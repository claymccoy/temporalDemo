package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Workflow
import java.time.Duration

class CharacterCountWorkflowImpl : CharacterCountWorkflow {

    private val activities = Workflow.newActivityStub(
            CharacterCountActivities::class.java,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build())

    var currentCounts: List<CharacterCount> = emptyList()
    var messageHistory: List<HistoryMessage> = emptyList()
    var exit = false

    override fun start(options: CharacterCountWorkflowOptions) {
        currentCounts = options.validCharacters.map { CharacterCount(it, 0) }
        while (true) {
            Workflow.await { exit }
            if (exit) {
                return
            }
        }
    }

    override fun count(message: String) {
        val messageCharToCount = activities.count(message)
        messageHistory = messageHistory + HistoryMessage(message,
                messageCharToCount.map { (k, v) -> CharacterCount(k, v) })
        currentCounts = currentCounts.map { charCount ->
            val charCountFromMessage = messageCharToCount[charCount.character] ?: 0
            charCount.copy(count = charCount.count + charCountFromMessage)
        }
    }

    override fun exit() {
        exit = true
    }

    override fun getTotals(): List<CharacterCount> {
        return currentCounts
    }

    override fun getHistory(limit: Int): List<HistoryMessage> {
        return messageHistory.takeLast(limit).reversed()
    }

}
