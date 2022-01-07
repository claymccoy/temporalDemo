package com.claymccoy.temporalDemo.hello

import io.temporal.activity.ActivityInterface

@ActivityInterface
interface AwsDeploymentActivities {
    fun deployImageToRegion(
            resource: AwsImage,
            application: String,
            account: String,
            region: String): Step
}

