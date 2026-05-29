package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingDomain

interface OnboardingRepo {
    suspend fun onBoardUser(onboardingDomain: OnboardingDomain): NetworkResponse<Unit, ErrorDomain>
}