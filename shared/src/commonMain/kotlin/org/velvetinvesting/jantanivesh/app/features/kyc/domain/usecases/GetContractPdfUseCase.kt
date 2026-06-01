package org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.kyc.domain.repository.MFKYCRepository

class GetContractPdfUseCase(private val repository: MFKYCRepository) {
    suspend operator fun invoke() = repository.getContractPdf()
}
