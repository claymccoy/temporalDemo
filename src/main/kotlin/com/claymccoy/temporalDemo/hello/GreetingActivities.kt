package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityInterface

@ActivityInterface
interface GreetingActivities {
    fun composeGreeting(GreetingParams: GreetingParams): String
}

data class GreetingParams(
        val greeting: String,
        val name: String
)
