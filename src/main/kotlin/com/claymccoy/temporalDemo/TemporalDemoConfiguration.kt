package com.claymccoy.temporalDemo

import com.claymccoy.temporalDemo.hello.CharacterCountActivitiesImpl
import com.claymccoy.temporalDemo.hello.CharacterCountWorkflowImpl
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
        val taskQueue = "LetterCountTaskQueue"
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
        worker.registerWorkflowImplementationTypes(CharacterCountWorkflowImpl::class.java)
        worker.registerActivitiesImplementations(CharacterCountActivitiesImpl())
        factory.start()
        return workflowClient
    }

}
