package com.claymccoy.temporalDemo.hello

import io.temporal.workflow.QueryMethod
import io.temporal.workflow.SignalMethod
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface CharStateWorkflow {
    @WorkflowMethod
    fun start(options: CharStateWorkflowOptions)

    @QueryMethod
    fun getTotal() : CharacterCount

    @SignalMethod
    fun add(additionalAmount: Int)

    @SignalMethod
    fun exit()

}

data class CharStateWorkflowOptions (
        val character: Char
)

data class CharacterCount (
        val character: Char,
        val count: Int
)
