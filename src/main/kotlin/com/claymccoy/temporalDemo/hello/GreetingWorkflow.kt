package com.claymccoy.temporalDemo.hello

import io.temporal.workflow.QueryMethod
import io.temporal.workflow.SignalMethod
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface GreetingWorkflow {
    @WorkflowMethod
    fun getGreeting(greeting: Greeting): String

    @QueryMethod
    fun getHistory() : List<String>

    @SignalMethod
    fun addGreeting(greeting: String)
}

data class Greeting(
        val name: String
)
