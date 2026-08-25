package org.velvetinvesting.jantanivesh.app.features.plans.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorType
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.core.networking.safeUnitRequest
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.CreateDailySipPlanBody
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.CreateMfPurchaseBody
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.ConfirmMfPurchaseResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.CreateMfPurchaseResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.CreateMonthlySipPlanBody
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.MandateListResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.MfPurchaseResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.CreatePurchasePlanResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.PurchasePlanListResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.PurchasePlanResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.SchemePlanResponseDto
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.VerifyOtpBody
import org.velvetinvesting.jantanivesh.app.features.plans.data.model.toDomain
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MandateOption
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchaseConfirmation
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.repository.PlansRepo

class PlansRepoImpl(
    private val client: HttpClient
) : PlansRepo {

    override suspend fun getSchemePlan(isin: String): NetworkResponse<SchemePlan, ErrorDomain> {
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

    override suspend fun getPurchasePlan(
        planId: String
    ): NetworkResponse<PurchasePlan, ErrorDomain> {
        val response = safeRequest<PurchasePlanResponseDto> {
            client.get(getUrl("/mf/purchase-plan/$planId"))
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
            client.post(getUrl("/mf/purchase-plan/$planId/confirm/request-otp"))
        }
    }

    override suspend fun verifyPurchasePlanOtp(
        planId: String,
        otp: String
    ): NetworkResponse<PurchasePlan, ErrorDomain> {
        val response = safeRequest<PurchasePlanResponseDto> {
            client.post(getUrl("/mf/purchase-plan/$planId/confirm/verify-otp")) {
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

    /**
     * The daily and monthly bodies differ in shape, not just in value: a daily SIP has no debit
     * day, and sending `installment_day: null` is not the same as leaving it out.
     */
    override suspend fun createSipPlan(
        mfProductId: String,
        amount: Int,
        frequency: String,
        installmentDay: Int?,
        folioNumber: String
    ): NetworkResponse<PurchasePlan, ErrorDomain> {
        val response = safeRequest<CreatePurchasePlanResponseDto> {
            client.post(getUrl("/mf/purchase-plan/")) {
                if (installmentDay != null) {
                    setBody(
                        CreateMonthlySipPlanBody(
                            mf_product_id = mfProductId,
                            amount = amount,
                            frequency = frequency,
                            installment_day = installmentDay,
                            folio_number = folioNumber
                        )
                    )
                } else {
                    setBody(
                        CreateDailySipPlanBody(
                            mf_product_id = mfProductId,
                            amount = amount,
                            frequency = frequency
                        )
                    )
                }
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

    override suspend fun createMfPurchase(
        mfProductId: String,
        amount: Int,
        folioNumber: String
    ): NetworkResponse<MfPurchase, ErrorDomain> {
        val response = safeRequest<CreateMfPurchaseResponseDto> {
            client.post(getUrl("/mf/purchase/")) {
                setBody(
                    CreateMfPurchaseBody(
                        mf_product_id = mfProductId,
                        amount = amount,
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
                        message = response.data.message ?: "Could not create your purchase",
                        type = ErrorType.SERVER
                    )
                )
        }
    }

    override suspend fun getMfPurchase(
        purchaseId: String
    ): NetworkResponse<MfPurchase, ErrorDomain> {
        val response = safeRequest<MfPurchaseResponseDto> {
            client.get(getUrl("/mf/purchase/$purchaseId"))
        }

        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)

            is NetworkResponse.Success -> response.data.toDomain()
                ?.let { NetworkResponse.Success(it) }
                ?: NetworkResponse.Error(
                    ErrorDomain(
                        code = -1,
                        message = response.data.message ?: "Could not read your purchase",
                        type = ErrorType.SERVER
                    )
                )
        }
    }

    override suspend fun requestMfPurchaseOtp(
        purchaseId: String
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/mf/purchase/$purchaseId/confirm/request-otp"))
        }
    }

    /**
     * The response carries the payment link, so it is parsed rather than discarded: the purchase
     * is only authorised at this point, and the caller has to send the user to that link next.
     */
    override suspend fun verifyMfPurchaseOtp(
        purchaseId: String,
        otp: String
    ): NetworkResponse<MfPurchaseConfirmation, ErrorDomain> {
        val response = safeRequest<ConfirmMfPurchaseResponseDto> {
            client.post(getUrl("/mf/purchase/$purchaseId/confirm/verify-otp")) {
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
                        message = response.data.message ?: "Could not confirm your purchase",
                        type = ErrorType.SERVER
                    )
                )
        }
    }

    override suspend fun getMandates(): NetworkResponse<List<MandateOption>, ErrorDomain> {
        val response = safeRequest<MandateListResponseDto> {
            client.get(getUrl("/mandate/"))
        }

        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
        }
    }
}
