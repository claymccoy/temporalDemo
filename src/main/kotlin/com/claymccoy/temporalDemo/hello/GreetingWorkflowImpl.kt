package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Async
import io.temporal.workflow.Workflow
import java.time.Duration

class GreetingWorkflowImpl : GreetingWorkflow {
    private val activities = Workflow.newActivityStub(
            GreetingActivities::class.java,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build())

    override fun getGreeting(greeting: Greeting): String {

        val hello = Async.function(activities::composeGreeting, GreetingParams("Hello", greeting.name))
        val bye = Async.function(activities::composeGreeting, GreetingParams("Bye", greeting.name))
        return """
            ${hello.get()}
            ${bye.get()}
            """.trimIndent()
    }
}
