package org.velvetinvesting.jantanivesh.app.features.onboarding.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.onboarding.data.remote.model.OnBoardingDto
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.remote.model.Profile
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingDomain

fun OnboardingDomain.toDto(): OnBoardingDto{
    return OnBoardingDto(
        profile=Profile(
            full_name = fullName,
            email = email,
            dob = dob
        )
    )
}