package org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.kyc.domain.repository.MFKYCRepository

class LinkKycDocumentsUseCase(private val repository: MFKYCRepository) {
    suspend operator fun invoke(type: String, imgUrl: String) = repository.uploadImageAndSignatureData(type, imgUrl)
}
