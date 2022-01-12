package com.claymccoy.temporalDemo.hello

class CharacterCountActivitiesImpl : CharacterCountActivities {
    override fun count(message: String): Map<Char, Int> {
        return message.toCharArray().groupBy { it }.mapValues { (k, v) ->  v.size }
    }
}
