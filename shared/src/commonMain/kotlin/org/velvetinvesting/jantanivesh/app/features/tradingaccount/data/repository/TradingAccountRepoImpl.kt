package org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.core.networking.safeUnitRequest
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.remote.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.remote.model.PANVerifyDto
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.remote.model.TradingAccountPrefilledResponseDto
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.remote.model.TradingAccountSubmissionDto
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.PANVerifyDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountPrefilledDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.repository.TradingAccountRepo

class TradingAccountRepoImpl(
    private val client: HttpClient
) : TradingAccountRepo {

    override suspend fun getTradingAccountPrefilledData(): NetworkResponse<TradingAccountPrefilledDomain, ErrorDomain> {
        val response = safeRequest<TradingAccountPrefilledResponseDto> {
            client.get(getUrl("/kyc/get-form-data"))
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun verifyPAN(pan: String): NetworkResponse<PANVerifyDomain, ErrorDomain> {
        val response = safeRequest<PANVerifyDto> {
            client.get(getUrl("/kyc/pan-verify")) {
                parameter("pan_number", pan)
            }
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun submitTradingAccountForm(data: TradingAccountFormDomain): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<TradingAccountSubmissionDto> {
            client.post(getUrl("/kyc/trading-account")) {
                setBody(data)
            }
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data.short_url)
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun tradingAccountConfirmation(
        taxStatus: String,
        holdingNature: String,
        jointHolderName1: String,
        jointHolderName2: String,
        guardianName: String,
        isMinor: Boolean
    ): NetworkResponse<Unit, ErrorDomain> {
        val body = buildMap<String, String> {
            put("tax_status", taxStatus)
            put("holding_nature", holdingNature)

            when {
                isMinor -> {
                    put("guardian_name", guardianName)
                }

                holdingNature == "JO" -> {
                    put("jh1_name", jointHolderName1)

                    if (jointHolderName2.isNotBlank()) {
                        put("jh2_name", jointHolderName2)
                    }
                }
            }
        }

        return safeUnitRequest {
            client.post(getUrl("/kyc/trading-confirmation")) {
                setBody(body)
            }
        }
    }
}
