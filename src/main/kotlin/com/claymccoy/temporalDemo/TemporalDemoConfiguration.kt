package com.claymccoy.temporalDemo

import com.claymccoy.temporalDemo.hello.GreetingActivitiesImpl
import com.claymccoy.temporalDemo.hello.GreetingWorkflow
import com.claymccoy.temporalDemo.hello.GreetingWorkflowImpl
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.worker.WorkerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemporalDemoConfiguration {

    @Bean
    fun greetingWorkflow(): GreetingWorkflow {
        val taskQueue = "HelloActivityTaskQueue"
        val workflowId = "HelloActivityWorkflow"

        val service = WorkflowServiceStubs.newInstance()
        val client = WorkflowClient.newInstance(service)
        val factory = WorkerFactory.newInstance(client)
        val worker = factory.newWorker(taskQueue)
        worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl::class.java)
        worker.registerActivitiesImplementations(GreetingActivitiesImpl())
        factory.start()
        return client.newWorkflowStub(
                GreetingWorkflow::class.java,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue(taskQueue)
                        .build())
    }
}
