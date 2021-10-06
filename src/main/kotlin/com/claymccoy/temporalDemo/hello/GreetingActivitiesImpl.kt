package com.claymccoy.temporalDemo.hello

class GreetingActivitiesImpl : GreetingActivities {
    override fun composeGreeting(greetingParams: GreetingParams): String {
        println("composeGreeting: $greetingParams")
        return "${greetingParams.greeting} ${greetingParams.name}!"
    }
}
