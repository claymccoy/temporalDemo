package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityInterface

@ActivityInterface
interface CharacterCountActivities {
    fun count(message: String): Map<Char, Int>
}

