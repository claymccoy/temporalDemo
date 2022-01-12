package com.claymccoy.temporalDemo.hello

import com.claymccoy.temporalDemo.TemporalDemoConfiguration
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowExecutionAlreadyStarted
import io.temporal.client.WorkflowOptions
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class LetterCountService(private val workflowClient: WorkflowClient) : InitializingBean {

    val workflowId = "LetterCountWorkflowId"
    private val workflow = workflowClient.newWorkflowStub(
                CharacterCountWorkflow::class.java,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue(TemporalDemoConfiguration.taskQueue)
                        .build())

    override fun afterPropertiesSet() {
        val options = CharacterCountWorkflowOptions(
                "abcdefghijklmnopqrstuvwxyz".toCharArray().asList()
        )
        try {
            WorkflowClient.start(workflow::start, options)
        } catch (e: WorkflowExecutionAlreadyStarted) {
            println("Already running as " + e.execution)
        }
    }

    fun countCharacters(message: String) : String {
        workflow.count(message)
        return message
    }

    fun totals() : List<CharacterCount> {
        return workflow.getTotals()
    }

    fun history(limit: Int) : List<HistoryMessage> {
        return workflow.getHistory(limit)
    }

}
