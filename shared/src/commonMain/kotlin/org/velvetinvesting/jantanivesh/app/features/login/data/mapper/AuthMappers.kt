package org.velvetinvesting.jantanivesh.app.features.login.data.mapper

import org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp.VerifyOtpDto
import org.velvetinvesting.jantanivesh.app.features.login.domain.model.LoginDomain

fun VerifyOtpDto.toLoginDomain(): LoginDomain {
    return LoginDomain(
        onboarded = this.data.user.metadata.is_onboarding_completed,
        userId = this.data.user.user_id
    )
}
