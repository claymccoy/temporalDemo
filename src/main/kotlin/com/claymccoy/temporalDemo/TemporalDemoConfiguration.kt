package com.claymccoy.temporalDemo

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.common.converter.DefaultDataConverter
import io.temporal.common.converter.JacksonJsonPayloadConverter
import io.temporal.common.converter.KotlinObjectMapperFactory
import io.temporal.serviceclient.WorkflowServiceStubs
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemporalDemoConfiguration {

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


}
