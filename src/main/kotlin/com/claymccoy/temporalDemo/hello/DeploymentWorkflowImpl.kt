package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Async
import io.temporal.workflow.Promise
import io.temporal.workflow.Workflow
import java.time.Duration
import java.util.*

class DeploymentWorkflowImpl : DeploymentWorkflow {
    private val activities = Workflow.newActivityStub(
            AwsDeploymentActivities::class.java,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build())

    private var task = Task("Pending", Date())

    override fun startDeployment(deployment: Deployment): Task {

        when (deployment) {
            is AwsDeployment -> executeAwsDeployment(deployment)
            else -> throw IllegalArgumentException("Unrecognized deployment type")
        }

        val promises = greetings.map { Async.function(activities::composeGreeting, GreetingParams(it, greeting.name)) }
        Promise.allOf(promises).get()
        val greetings = promises.joinToString { it.get() }
        history = history + greetings
        return greetings
    }

    private fun executeAwsDeployment(deployment: AwsDeployment) {
        deployment.target.regions.map {
            Async.function(activities::deployImageToRegion, GreetingParams(it, greeting.name))
        }
    }


    override fun getStatus(): List<Step> {
        return steps
    }

    override fun rollback() {
        TODO("Not yet implemented")
    }

    override fun manualJudgement(token: String, isSuccessful: Boolean) {
        TODO("Not yet implemented")
    }
}
