package org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.kyc.domain.repository.MFKYCRepository

class InitiateKycUseCase(private val repository: MFKYCRepository) {
    suspend operator fun invoke() = repository.initiateKyc()
}
