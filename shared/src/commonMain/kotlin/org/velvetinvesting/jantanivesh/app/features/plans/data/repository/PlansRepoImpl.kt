package org.velvetinvesting.jantanivesh.app.features.plans.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorType
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.core.networking.safeUnitRequest
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.CreatePurchasePlanBody
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.CreatePurchasePlanResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.PurchasePlanListResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.PurchasePlanResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.SchemePlanResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.VerifyOtpBody
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.toDomain
import org.velvetinvesting.jantanivesh.app.features.plans.data.source.OfferedSchemes
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.repository.PlansRepo

class PlansRepoImpl(
    private val client: HttpClient
) : PlansRepo {

    /**
     * Each offered scheme is a separate `GET /mf-scheme/{isin}`, fetched concurrently. A scheme
     * that fails is dropped rather than failing the screen; only an all-empty result is an error,
     * since that means nothing can be shown at all.
     */
    override suspend fun getSchemePlans(): NetworkResponse<List<SchemePlan>, ErrorDomain> =
        coroutineScope {
            val results = OfferedSchemes.ISINS
                .map { isin -> async { fetchScheme(isin) } }
                .awaitAll()

            val schemes = results.filterIsInstance<NetworkResponse.Success<SchemePlan>>()
                .map { it.data }

            if (schemes.isNotEmpty()) {
                NetworkResponse.Success(schemes)
            } else {
                val firstError = results.filterIsInstance<NetworkResponse.Error<ErrorDomain>>()
                    .firstOrNull()
                    ?.error
                NetworkResponse.Error(
                    firstError ?: ErrorDomain(
                        code = -1,
                        message = "Could not load the available funds",
                        type = ErrorType.SERVER
                    )
                )
            }
        }

    private suspend fun fetchScheme(isin: String): NetworkResponse<SchemePlan, ErrorDomain> {
        val response = safeRequest<SchemePlanResponseDto> {
            client.get(getUrl("/mf-scheme/$isin"))
        }

        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)

            is NetworkResponse.Success -> response.data.toDomain()
                ?.let { NetworkResponse.Success(it) }
                ?: NetworkResponse.Error(
                    ErrorDomain(
                        code = -1,
                        message = "Fund $isin is unavailable",
                        type = ErrorType.SERVER
                    )
                )
        }
    }

    override suspend fun getPurchasePlans(): NetworkResponse<List<PurchasePlan>, ErrorDomain> {
        val response = safeRequest<PurchasePlanListResponseDto> {
            client.get(getUrl("/mf-purchase-plan/"))
        }
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun createPurchasePlan(
        scheme: String,
        amount: Int,
        frequency: String,
        installmentDay: Int,
        folioNumber: String
    ): NetworkResponse<PurchasePlan, ErrorDomain> {
        val response = safeRequest<CreatePurchasePlanResponseDto> {
            client.post(getUrl("/mf-purchase-plan/")) {
                setBody(
                    CreatePurchasePlanBody(
                        scheme = scheme,
                        amount = amount,
                        frequency = frequency,
                        installment_day = installmentDay,
                        folio_number = folioNumber
                    )
                )
            }
        }

        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)

            is NetworkResponse.Success -> response.data.toDomain()
                ?.let { NetworkResponse.Success(it) }
                ?: NetworkResponse.Error(
                    ErrorDomain(
                        code = -1,
                        message = response.data.message ?: "Could not create your SIP",
                        type = ErrorType.SERVER
                    )
                )
        }
    }

    override suspend fun getPurchasePlan(
        planId: String
    ): NetworkResponse<PurchasePlan, ErrorDomain> {
        val response = safeRequest<PurchasePlanResponseDto> {
            client.get(getUrl("/mf-purchase-plan/$planId"))
        }

        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)

            is NetworkResponse.Success -> response.data.toDomain()
                ?.let { NetworkResponse.Success(it) }
                ?: NetworkResponse.Error(
                    ErrorDomain(
                        code = -1,
                        message = response.data.message ?: "Could not read your SIP",
                        type = ErrorType.SERVER
                    )
                )
        }
    }

    override suspend fun requestPurchasePlanOtp(planId: String): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/mf-purchase-plan/$planId/confirm/request-otp"))
        }
    }

    override suspend fun verifyPurchasePlanOtp(
        planId: String,
        otp: String
    ): NetworkResponse<PurchasePlan, ErrorDomain> {
        val response = safeRequest<PurchasePlanResponseDto> {
            client.post(getUrl("/mf-purchase-plan/$planId/confirm/verify-otp")) {
                setBody(VerifyOtpBody(otp = otp))
            }
        }

        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)

            is NetworkResponse.Success -> response.data.toDomain()
                ?.let { NetworkResponse.Success(it) }
                ?: NetworkResponse.Error(
                    ErrorDomain(
                        code = -1,
                        message = response.data.message ?: "Could not confirm your SIP",
                        type = ErrorType.SERVER
                    )
                )
        }
    }
}
