package org.velvetinvesting.jantanivesh.app.features.kyc.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.model.DigiLockerDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.model.KYCInitInfo
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.model.KycFormDataDomain

interface MFKYCRepository {
    suspend fun initiateKyc(): NetworkResponse<KYCInitInfo, ErrorDomain>
    suspend fun getDigiLockerDetails(): NetworkResponse<DigiLockerDetailsDomain, ErrorDomain>
    suspend fun uploadKYCFormData(data: KycFormDataDomain): NetworkResponse<Unit, ErrorDomain>
    suspend fun uploadImage(imageBytes: ByteArray, mimeType: String): NetworkResponse<String, ErrorDomain>
    suspend fun uploadSignature(imageBytes: ByteArray, mimeType: String): NetworkResponse<String, ErrorDomain>
    suspend fun uploadImageAndSignatureData(type: String, imgUrl: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun getContractPdf(): NetworkResponse<String, ErrorDomain>
    suspend fun getESignUrl(): NetworkResponse<String, ErrorDomain>
    suspend fun finalizeKyc(): NetworkResponse<Unit, ErrorDomain>
}
