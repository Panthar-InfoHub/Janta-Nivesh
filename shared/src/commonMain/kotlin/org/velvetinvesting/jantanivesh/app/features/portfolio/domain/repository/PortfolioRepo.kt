package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.ActiveSipDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositTransactionDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FolioFundDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MfRedemption
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingOrderDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.models.InvestMoreDto

interface PortfolioRepo {
    suspend fun getPortfolio(): NetworkResponse<PortfolioDomain, ErrorDomain>

    suspend fun getFDPortfolioById(id:String): NetworkResponse<FixedDepositTransactionDomain, ErrorDomain>

    suspend fun getFDRedirectUrl(id:String, event: String): NetworkResponse<String, ErrorDomain>
    suspend fun exportReport(
        type: String,
        year: Int? = null,
        folio: String? = null,
        expand: Int? = null
    ): NetworkResponse<String, ErrorDomain>

    suspend fun getPendingOrders(): NetworkResponse<List<PendingOrderDomain>, ErrorDomain>

    /**
     * The running SIPs, from `GET /mf/purchase-plan`. Separate from [getPortfolio] because the
     * portfolio payload reports holdings, not standing instructions.
     */
    suspend fun getActiveSips(): NetworkResponse<ActiveSipDomain, ErrorDomain>

    suspend fun getFolioFunds(folioId: String): NetworkResponse<List<FolioFundDomain>, ErrorDomain>

    suspend fun investMoreLumpsum(body: InvestMoreDto): NetworkResponse<String, ErrorDomain>
    
    suspend fun cancelLumpSumOrder(orderId: String): NetworkResponse<Unit, ErrorDomain>

    suspend fun cancelSipOrder(xsipRegNo: String): NetworkResponse<Unit, ErrorDomain>

    // ── Redemption ───────────────────────────────────────────────────────────────────────────
    // create -> poll until the gateway settles -> OTP request -> OTP verify. Every step after
    // the first is keyed on the `fp_id` the create returned.

    suspend fun createRedemptionByAmount(
        holdingId: String,
        amount: Double
    ): NetworkResponse<MfRedemption, ErrorDomain>

    suspend fun createRedemptionByUnits(
        holdingId: String,
        units: Double
    ): NetworkResponse<MfRedemption, ErrorDomain>

    suspend fun getRedemption(redemptionId: String): NetworkResponse<MfRedemption, ErrorDomain>

    suspend fun requestRedemptionOtp(redemptionId: String): NetworkResponse<Unit, ErrorDomain>

    suspend fun verifyRedemptionOtp(
        redemptionId: String,
        otp: String
    ): NetworkResponse<Unit, ErrorDomain>
}