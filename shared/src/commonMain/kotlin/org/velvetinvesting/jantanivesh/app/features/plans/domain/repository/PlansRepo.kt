package org.velvetinvesting.jantanivesh.app.features.plans.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MandateOption
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchaseConfirmation
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan

interface PlansRepo {

    /**
     * A single scheme by ISIN. This is what the purchase screen loads: its thresholds decide the
     * minimum amount, the debit days and which of the three purchase modes are offered at all.
     */
    suspend fun getSchemePlan(isin: String): NetworkResponse<SchemePlan, ErrorDomain>

    /**
     * Registers the SIP against a product id rather than an ISIN. [installmentDay] is required
     * for a monthly SIP and must be null for a daily one — the two send different bodies.
     */
    suspend fun createSipPlan(
        mfProductId: String,
        amount: Int,
        frequency: String,
        installmentDay: Int?,
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

    // ── One-time (lumpsum) purchase ──────────────────────────────────────────────────────────
    // Same create → read-back → OTP sequence as a SIP, on its own set of endpoints.

    suspend fun createMfPurchase(
        mfProductId: String,
        amount: Int,
        folioNumber: String
    ): NetworkResponse<MfPurchase, ErrorDomain>

    suspend fun getMfPurchase(purchaseId: String): NetworkResponse<MfPurchase, ErrorDomain>

    suspend fun requestMfPurchaseOtp(purchaseId: String): NetworkResponse<Unit, ErrorDomain>

    /**
     * Authorises the purchase. The response carries the payment link the user still has to
     * complete — confirming is not paying.
     */
    suspend fun verifyMfPurchaseOtp(
        purchaseId: String,
        otp: String
    ): NetworkResponse<MfPurchaseConfirmation, ErrorDomain>

    /** Autopay mandates the SIP can be debited against. */
    suspend fun getMandates(): NetworkResponse<List<MandateOption>, ErrorDomain>
}
