package com.claymccoy.temporalDemo

import com.claymccoy.temporalDemo.hello.GreetingWorkflow
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.client.WorkflowOptions
import io.temporal.common.converter.DefaultDataConverter
import io.temporal.common.converter.JacksonJsonPayloadConverter
import io.temporal.common.converter.KotlinObjectMapperFactory
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
    fun workflowClientOptions(): WorkflowClientOptions {
        return WorkflowClientOptions.newBuilder()
                .setDataConverter(DefaultDataConverter(JacksonJsonPayloadConverter(KotlinObjectMapperFactory.new())))
                .build()
    }

    @Bean
    fun workflowClient(workflowClientOptions: WorkflowClientOptions): WorkflowClient {
        val service = WorkflowServiceStubs.newInstance()
        return WorkflowClient.newInstance(service, workflowClientOptions)
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
