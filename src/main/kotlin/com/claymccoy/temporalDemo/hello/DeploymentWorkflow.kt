package com.claymccoy.temporalDemo.hello

import io.temporal.workflow.QueryMethod
import io.temporal.workflow.SignalMethod
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod
import java.util.*

@WorkflowInterface
interface DeploymentWorkflow {
    @WorkflowMethod
    fun startDeployment(deployment: Deployment): String

    @QueryMethod
    fun getStatus() : List<Step>

    @SignalMethod
    fun rollback()

    @SignalMethod
    fun manualJudgement(token: String, isSuccessful: Boolean)
}

interface Deployment {
    val target: Target
    val resource: Resource
    val strategy: Strategy
    val validation: Validation
}

data class AwsDeployment(
        override val target: AwsTarget,
        override val resource: AwsImage,
        override val strategy: Strategy,
        override val validation: Validation
) : Deployment

interface Target {
    val application: String
    val account: String
}

data class AwsTarget (
        override val application: String,
        override val account: String,
        val regions: Set<String>
) : Target

interface Resource

data class AwsImage(
        val reference: String
) : Resource

interface Strategy {
    val type: String
    val timeoutMillis: Long
}

data class BlueGreen (
        override val timeoutMillis: Long,
        val deletePrevious: Boolean
) : Strategy {
    override val type = "BlueGreen"
}

interface Validation {

}

data class ManualJudgement(
        val timeoutMillis: Long
) : Validation


data class Task(
        val status: String,
        val start: Date,
        val end: Date? = null,
        val conclusion: String? = null,
        val steps: List<Step>? = emptyList()
)

data class Step(
        val status: String,
        val start: Date,
        val end: Date?,
        val conclusion: String?,
        val attributes: Map<String, Any>
)