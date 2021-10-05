package com.claymccoy.temporalDemo.hello

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/greeting")
class GreetingController(private val greetingService: GreetingService) {

    @GetMapping("/hello")
    fun hello(@RequestParam name: String): String {
        return greetingService.greet(name)
    }

}