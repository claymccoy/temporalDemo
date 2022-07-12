package com.claymccoy.temporalDemo.hello

import com.claymccoy.temporalDemo.TemporalDemoConfiguration
import io.temporal.api.common.v1.WorkflowExecution
import io.temporal.api.filter.v1.WorkflowTypeFilter
import io.temporal.api.query.v1.WorkflowQuery
import io.temporal.api.taskqueue.v1.TaskQueue
import io.temporal.api.workflowservice.v1.*
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
        val service = workflowClient.workflowServiceStubs

        val nsResponse = service.blockingStub().describeNamespace(
                DescribeNamespaceRequest.newBuilder()
                        .setNamespace(workflowClient.options.namespace)
                        .build()
        )

        val tqResponse = service.blockingStub().describeTaskQueue(
                DescribeTaskQueueRequest.newBuilder()
                        .setNamespace(workflowClient.options.namespace)
                        .setTaskQueue(TaskQueue.newBuilder().setName(TemporalDemoConfiguration.taskQueue).build())
                        .build()
        )

        val wfResponse = service.blockingStub().describeWorkflowExecution(
                DescribeWorkflowExecutionRequest.newBuilder()
                        .setNamespace(workflowClient.options.namespace)
                        .setExecution(WorkflowExecution.newBuilder()
                                .setWorkflowId("LetterCounter")
                                .build())
                        .build()
        )

        val wfhResponse = service.blockingStub().getWorkflowExecutionHistory(
                GetWorkflowExecutionHistoryRequest.newBuilder()
                        .setNamespace(workflowClient.options.namespace)
                        .setExecution(WorkflowExecution.newBuilder()
                                .setWorkflowId("LetterCounter")
                                .build())
                        .build()
        )

        val qwfResponse = service.blockingStub().queryWorkflow(QueryWorkflowRequest.newBuilder()
                .setNamespace(workflowClient.options.namespace)
                .setExecution(WorkflowExecution.newBuilder()
                        .setWorkflowId("LetterCounter")
                        .build())
                .setQuery(WorkflowQuery.newBuilder()
                        .setQueryType("__stack_trace")
                        .build())
                .build());



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
