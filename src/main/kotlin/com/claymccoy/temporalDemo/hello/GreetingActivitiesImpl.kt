package com.claymccoy.temporalDemo.hello

class GreetingActivitiesImpl : GreetingActivities {
    override fun composeGreeting(greetingParams: GreetingParams): String {
        return "${greetingParams.greeting} ${greetingParams.name}!"
    }
}
