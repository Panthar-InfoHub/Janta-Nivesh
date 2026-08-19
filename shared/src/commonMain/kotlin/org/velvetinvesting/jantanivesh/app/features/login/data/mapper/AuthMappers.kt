package org.velvetinvesting.jantanivesh.app.features.login.data.mapper

import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStage
import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStepStatus
import org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp.VerifyOtpDto
import org.velvetinvesting.jantanivesh.app.features.login.domain.model.LoginDomain

fun VerifyOtpDto.toLoginDomain(): LoginDomain {
    val onboarding = data.onboarding
    val stages = onboarding.stages

    // Readiness is only ever skipped once the basic details are in: the user gave us what we need
    // to open the app and chose to defer the rest, which the app then chases up from inside the
    // main flow. Readiness still pending means the remaining steps are due now, in onboarding.
    val deferredRemainingSteps =
        OnboardingStepStatus.fromValue(stages.readiness) == OnboardingStepStatus.SKIPPED &&
                OnboardingStepStatus.fromValue(stages.basic_details) == OnboardingStepStatus.VERIFIED

    return LoginDomain(
        canEnterMainApp = onboarding.is_completed || deferredRemainingSteps,
        userId = data.user.user_id,
        // Recorded as-is, not as a resume point: for a user heading into the main app this is the
        // honest note of where they stand, and the autopay substitution would claim a step they
        // may not owe. Whoever enters the onboarding flow applies `resumePoint` at that moment.
        stage = OnboardingStage.normalize(onboarding.current_stage)
    )
}
