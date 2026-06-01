package org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.kyc.domain.model.KycFormDataDomain
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.repository.MFKYCRepository

class UploadKycFormDataUseCase(private val repository: MFKYCRepository) {
    suspend operator fun invoke(data: KycFormDataDomain) = repository.uploadKYCFormData(data)
}
