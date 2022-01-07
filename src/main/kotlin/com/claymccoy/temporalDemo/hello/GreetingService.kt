package com.claymccoy.temporalDemo.hello

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import org.springframework.stereotype.Component

@Component
class GreetingService(private val workflowClient: WorkflowClient) {

    companion object {
        val taskQueue = "HelloActivityTaskQueue"
    }
    val workflowId = "HelloActivityWorkflow"

    fun greet(name: String) : String {
        return getWorkflow().getGreeting(Greeting(name))
    }

    fun getWorkflow(): DeploymentWorkflow {
        return workflowClient.newWorkflowStub(
                DeploymentWorkflow::class.java,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue(taskQueue)
                        .build())
    }

}
