package org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.BankAccount
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.InvestorProfile
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.Nominee
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

class SubmitPennyDropUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(bankAccount: BankAccount): NetworkResponse<Unit, ErrorDomain> {
        return onboardingRepo.submitPennyDrop(bankAccount)
    }
}

class SubmitInvestorProfileUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    suspend operator fun invoke(profile: InvestorProfile): NetworkResponse<Unit, ErrorDomain> {
        return onboardingRepo.submitInvestorProfile(profile)
    }
}

class SubmitNomineesUseCase(
    private val onboardingRepo: OnboardingRepo
) {
    /** An empty [nominees] list is the "add them later" case and maps to `{ "skip": true }`. */
    suspend operator fun invoke(nominees: List<Nominee>): NetworkResponse<Unit, ErrorDomain> {
        return if (nominees.isEmpty()) {
            onboardingRepo.skipNominees()
        } else {
            onboardingRepo.submitNominees(nominees)
        }
    }
}
