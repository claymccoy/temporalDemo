package com.claymccoy.temporalDemo.hello

import io.temporal.workflow.QueryMethod
import io.temporal.workflow.SignalMethod
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface CharacterCountWorkflow {
    @WorkflowMethod
    fun start(options: CharacterCountWorkflowOptions)

    @QueryMethod
    fun getTotals() : List<CharacterCount>

    @QueryMethod
    fun getHistory(limit: Int) : List<HistoryMessage>

    @SignalMethod
    fun count(message: String)

    @SignalMethod
    fun exit()

}

data class CharacterCountWorkflowOptions (
        val validCharacters: List<Char>
)

data class CharacterCount (
        val character: Char,
        val count: Int
)

data class HistoryMessage (
        val message: String,
        val charCounts: List<CharacterCount>
)
