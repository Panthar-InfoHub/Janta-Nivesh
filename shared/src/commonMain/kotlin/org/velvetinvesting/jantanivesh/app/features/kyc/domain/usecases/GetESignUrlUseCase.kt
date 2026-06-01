package org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.kyc.domain.repository.MFKYCRepository

class GetESignUrlUseCase(private val repository: MFKYCRepository) {
    suspend operator fun invoke() = repository.getESignUrl()
}
