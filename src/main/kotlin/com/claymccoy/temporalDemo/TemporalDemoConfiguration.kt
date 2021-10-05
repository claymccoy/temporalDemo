package com.claymccoy.temporalDemo

import com.claymccoy.temporalDemo.hello.GreetingWorkflow
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import io.temporal.serviceclient.WorkflowServiceStubs
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemporalDemoConfiguration {

    companion object {
        val taskQueue = "HelloActivityTaskQueue"
        val workflowId = "HelloActivityWorkflow"
    }

    @Bean
    fun workflowClient(): WorkflowClient {
        val service = WorkflowServiceStubs.newInstance()
        return WorkflowClient.newInstance(service)
    }

    @Bean
    fun greetingWorkflow(workflowClient: WorkflowClient): GreetingWorkflow {
        return workflowClient.newWorkflowStub(
                GreetingWorkflow::class.java,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue(taskQueue)
                        .build())
    }

}
