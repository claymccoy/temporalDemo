package com.claymccoy.temporalDemo.hello

import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface GreetingWorkflow {
    @WorkflowMethod
    fun getGreeting(name: String): String
}
