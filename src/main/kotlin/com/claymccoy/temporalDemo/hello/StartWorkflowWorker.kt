package com.claymccoy.temporalDemo.hello

import io.temporal.client.WorkflowClient
import io.temporal.worker.WorkerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class StartWorkflowWorker(private val workflowClient: WorkflowClient) : InitializingBean {

    override fun afterPropertiesSet() {
        val factory = WorkerFactory.newInstance(workflowClient)
        val worker = factory.newWorker(GreetingService.taskQueue)
        worker.registerWorkflowImplementationTypes(DeploymentWorkflowImpl::class.java)
        worker.registerActivitiesImplementations(GreetingActivitiesImpl())
        factory.start()
    }

}
