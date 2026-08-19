package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model

/**
 * Result of `POST /onboarding/email/verify-otp`. [email] is echoed back as the address that was
 * actually confirmed, which is worth trusting over whatever the client thinks it sent.
 */
data class EmailVerification(
    val email: String?,
    val onboarding: OnboardingStatus?
)
