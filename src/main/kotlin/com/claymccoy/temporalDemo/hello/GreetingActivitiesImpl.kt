package com.claymccoy.temporalDemo.hello

class GreetingActivitiesImpl : GreetingActivities {
    override fun composeGreeting(greeting: String, name: String): String {
        return "$greeting $name!"
    }
}
