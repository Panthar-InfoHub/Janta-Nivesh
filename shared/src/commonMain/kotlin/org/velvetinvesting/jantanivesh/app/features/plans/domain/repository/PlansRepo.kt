package org.velvetinvesting.jantanivesh.app.features.plans.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan

interface PlansRepo {

    /** Schemes the user can start a SIP in, fetched one per ISIN. */
    suspend fun getSchemePlans(): NetworkResponse<List<SchemePlan>, ErrorDomain>

    /** Registers the SIP. The returned plan id keys the OTP confirmation that follows. */
    suspend fun createPurchasePlan(
        scheme: String,
        amount: Int,
        frequency: String,
        installmentDay: Int,
        folioNumber: String
    ): NetworkResponse<PurchasePlan, ErrorDomain>

    /** Every SIP registration the user has, newest state included. */
    suspend fun getPurchasePlans(): NetworkResponse<List<PurchasePlan>, ErrorDomain>

    /** Reads back a single plan by its gateway id, after creation and before confirming it. */
    suspend fun getPurchasePlan(planId: String): NetworkResponse<PurchasePlan, ErrorDomain>

    suspend fun requestPurchasePlanOtp(planId: String): NetworkResponse<Unit, ErrorDomain>

    suspend fun verifyPurchasePlanOtp(
        planId: String,
        otp: String
    ): NetworkResponse<PurchasePlan, ErrorDomain>
}
