package com.claymccoy.temporalDemo.hello

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chars")
class CharacterCounterController(private val letterCountService: LetterCountService) {

    @GetMapping("count")
    fun count(@RequestParam message: String): String {
        return letterCountService.countCharacters(message)
    }

    @GetMapping("totals")
    fun totals(): List<CharacterCount> {
        return letterCountService.totals()
    }

    @GetMapping("history")
    fun history(@RequestParam limit: Int): List<HistoryMessage> {
        return letterCountService.history(limit)
    }

}