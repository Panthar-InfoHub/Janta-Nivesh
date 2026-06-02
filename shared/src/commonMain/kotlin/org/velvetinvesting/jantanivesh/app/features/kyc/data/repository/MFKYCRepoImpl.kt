package org.velvetinvesting.jantanivesh.app.features.kyc.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.core.networking.safeUnitRequest
import org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.mapper.toDto
import org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.model.ContractPdfDto
import org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.model.DigiLockerDetailsDto
import org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.model.ESignDto
import org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.model.ImageUploadDto
import org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.model.InitiateKycDto
import org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.model.UrlUploadBodyDto
import org.velvetinvesting.jantanivesh.app.features.kyc.data.remote.model.UrlUploadResultDto
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.model.DigiLockerDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.model.KYCInitInfo
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.model.KycFormDataDomain
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.repository.MFKYCRepository

class MFKYCRepoImpl(
    private val client: HttpClient
) : MFKYCRepository {
    override suspend fun initiateKyc(): NetworkResponse<KYCInitInfo, ErrorDomain> {
        val response = safeRequest<InitiateKycDto> {
            client.post(getUrl("/kyc/mf-initiate"))
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun getDigiLockerDetails(): NetworkResponse<DigiLockerDetailsDomain, ErrorDomain> {
        val response = safeRequest<DigiLockerDetailsDto> {
            client.post(getUrl("/kyc/mf-details"))
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun uploadKYCFormData(data: KycFormDataDomain): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/kyc/mf-update")) {
                setBody(data.toDto())
            }
        }
    }

    override suspend fun uploadImage(imageBytes: ByteArray, mimeType: String): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<ImageUploadDto> {
            client.post("https://persist.signzy.tech/api/files/upload") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "file",
                                value = imageBytes,
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, mimeType)
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "form-data; name=\"file\"; filename=\"image.jpg\""
                                    )
                                }
                            )
                            append("ttl", "infinity")
                        }
                    )
                )
            }
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.file.directURL)
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun uploadSignature(imageBytes: ByteArray, mimeType: String): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<ImageUploadDto> {
            client.post("https://persist.signzy.tech/api/files/upload") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "file",
                                value = imageBytes,
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, mimeType)
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "form-data; name=\"file\"; filename=\"image.jpg\""
                                    )
                                }
                            )
                            append("ttl", "infinity")
                        }
                    )
                )
            }
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.file.directURL)
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun uploadImageAndSignatureData(type: String, imgUrl: String): NetworkResponse<Unit, ErrorDomain> {
        val response = safeRequest<UrlUploadResultDto> {
            client.patch(getUrl("/kyc/mf-doc")) {
                setBody(UrlUploadBodyDto(type, imgUrl))
            }
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(Unit)
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun getContractPdf(): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<ContractPdfDto> {
            client.post(getUrl("/kyc/mf-contract"))
        }
        return when (response) {
            is NetworkResponse.Success -> {
                val url = response.data.data.`object`.result.combinedPdf
                NetworkResponse.Success(url)
            }
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun getESignUrl(): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<ESignDto> {
            client.get(getUrl("/kyc/mf-esign"))
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data)
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun finalizeKyc(): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.get(getUrl("/kyc/mf-verify"))
        }
    }
}
