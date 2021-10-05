package com.claymccoy.temporalDemo.hello

import org.springframework.stereotype.Component

@Component
class GreetingService(private val greetingWorkflow: GreetingWorkflow) {

    fun greet(name: String) : String {
        return greetingWorkflow.getGreeting(Greeting(name))
    }

}
