package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingDomain
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

class OnboardUserUseCase(private val repository: OnboardingRepo) {
    suspend operator fun invoke(onboardingDomain: OnboardingDomain) = repository.onBoardUser(onboardingDomain)
}
