package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface GreetingActivities {
    @ActivityMethod(name = "greet")
    fun composeGreeting(greeting: String, name: String): String
}
