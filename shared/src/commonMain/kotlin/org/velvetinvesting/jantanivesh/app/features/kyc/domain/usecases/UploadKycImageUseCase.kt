package org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.kyc.domain.repository.MFKYCRepository

class UploadKycImageUseCase(private val repository: MFKYCRepository) {
    suspend operator fun invoke(imageBytes: ByteArray, mimeType: String) = repository.uploadImage(imageBytes, mimeType)
}
