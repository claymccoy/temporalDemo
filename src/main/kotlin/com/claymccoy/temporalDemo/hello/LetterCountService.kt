package com.claymccoy.temporalDemo.hello

import com.claymccoy.temporalDemo.TemporalDemoConfiguration
import io.temporal.api.filter.v1.WorkflowTypeFilter
import io.temporal.api.workflowservice.v1.ListOpenWorkflowExecutionsRequest
import io.temporal.api.workflowservice.v1.ListWorkflowExecutionsRequest
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowExecutionAlreadyStarted
import io.temporal.client.WorkflowOptions
import io.temporal.serviceclient.WorkflowServiceStubs
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component


@Component
class LetterCountService(private val workflowClient: WorkflowClient) : InitializingBean {

    val workflowId = "LetterCounter"
    private val workflow = workflowClient.newWorkflowStub(
                CharCounterWorkflow::class.java,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue(TemporalDemoConfiguration.taskQueue)
                        .build())

    private val service = WorkflowServiceStubs.newInstance()

    override fun afterPropertiesSet() {
        val options = CharacterCountWorkflowOptions(
                "abcdefghijklmnopqrstuvwxyz".toCharArray().asList().toSet()
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

    fun totals(): List<CharacterCount> {
        val request = ListOpenWorkflowExecutionsRequest.newBuilder()
                .setNamespace(workflowClient.options.namespace)
                .setTypeFilter(WorkflowTypeFilter.newBuilder().setName(CharStateWorkflow::class.java.simpleName))
                .build()
        val response = service.blockingStub().listOpenWorkflowExecutions(request)
        val workflowIds = response.executionsList.map { it.execution.workflowId }
        val charStateWorkflows = workflowIds.map {
            workflowClient.newWorkflowStub(CharStateWorkflow::class.java, it)
        }
        val totalCharCounts = charStateWorkflows.map {
            it.getTotal()
        }
        return totalCharCounts.sortedBy { it.character }
    }

    fun history(limit: Int = 10) : List<HistoryMessage> {
        return workflow.getHistory(limit)
    }

}
