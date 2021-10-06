package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Async
import io.temporal.workflow.Promise
import io.temporal.workflow.Workflow
import java.time.Duration

class GreetingWorkflowImpl : GreetingWorkflow {
    private val activities = Workflow.newActivityStub(
            GreetingActivities::class.java,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build())

    private var history = emptyList<String>()
    private var greetings = listOf("Hello")

    override fun getHistory(): List<String> {
        return history
    }

    override fun addGreeting(greeting: String) {
        this.greetings = greetings
    }

    override fun getGreeting(greeting: Greeting): String {
        val promises = greetings.map { Async.function(activities::composeGreeting, GreetingParams(it, greeting.name)) }
        Promise.allOf(promises).get()
        val greetings = promises.joinToString { it.get() }
        history = history + greetings
        return greetings
    }
}
