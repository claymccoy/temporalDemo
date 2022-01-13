package com.claymccoy.temporalDemo

import com.claymccoy.temporalDemo.hello.CharActivitiesImpl
import com.claymccoy.temporalDemo.hello.CharCounterWorkflowImpl
import com.claymccoy.temporalDemo.hello.CharStateWorkflowImpl
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.common.converter.DefaultDataConverter
import io.temporal.common.converter.JacksonJsonPayloadConverter
import io.temporal.common.converter.KotlinObjectMapperFactory
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.worker.WorkerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemporalDemoConfiguration {

    companion object {
        val taskQueue = "LetterCounterTaskQueue"
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
        val workflowClient = WorkflowClient.newInstance(service, workflowClientOptions)
        val factory = WorkerFactory.newInstance(workflowClient)
        val worker = factory.newWorker(taskQueue)
        worker.registerWorkflowImplementationTypes(
                CharCounterWorkflowImpl::class.java,
                CharStateWorkflowImpl::class.java
        )
        worker.registerActivitiesImplementations(CharActivitiesImpl(workflowClient))
        factory.start()
        return workflowClient
    }

}
