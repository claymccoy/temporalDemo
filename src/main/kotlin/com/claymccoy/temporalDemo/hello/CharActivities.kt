package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityInterface

@ActivityInterface
interface CharActivities {
    fun count(message: String): Set<CharacterCount>

}
