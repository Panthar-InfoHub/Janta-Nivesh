package org.velvetinvesting.jantanivesh.app.features.login.data.mapper

import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStage
import org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp.VerifyOtpDto
import org.velvetinvesting.jantanivesh.app.features.login.domain.model.LoginDomain

fun VerifyOtpDto.toLoginDomain(): LoginDomain {
    return LoginDomain(
        onboarded = data.onboarding.is_completed,
        userId = data.user.user_id,
        stage = OnboardingStage.fromIdOrDefault(data.onboarding.current_stage)
    )
}