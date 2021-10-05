package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Workflow
import java.time.Duration

class GreetingWorkflowImpl : GreetingWorkflow {
    private val activities = Workflow.newActivityStub(
            GreetingActivities::class.java,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build())

    override fun getGreeting(greeting: Greeting): String {
        return activities.composeGreeting("Hello", greeting.name)
    }
}
