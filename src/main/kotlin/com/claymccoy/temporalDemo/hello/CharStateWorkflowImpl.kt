package com.claymccoy.temporalDemo.hello

import io.temporal.workflow.Workflow

class CharStateWorkflowImpl : CharStateWorkflow {

    var currentCount: CharacterCount? = null
    var exit = false

    override fun start(options: CharStateWorkflowOptions) {
        currentCount = CharacterCount(options.character, 0)
        while (true) {
            Workflow.await { exit }
            if (exit) {
                return
            }
        }
    }

    override fun add(additionalAmount: Int) {
        currentCount = currentCount?.let { it.copy(count = additionalAmount + it?.count) }
    }

    override fun exit() {
        exit = true
    }

    override fun getTotal(): CharacterCount {
        return currentCount!!
    }

}
