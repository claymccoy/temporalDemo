package com.claymccoy.temporalDemo.hello

import io.temporal.client.WorkflowClient

class CharActivitiesImpl(private val workflowClient: WorkflowClient) : CharActivities {
    override fun count(message: String): Set<CharacterCount> {
        return message.toCharArray().groupBy { it }.map { (k, v) ->  CharacterCount(k, v.size) }.toSet()
    }

}
