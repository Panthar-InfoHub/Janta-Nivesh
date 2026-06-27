package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositTransactionDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FolioFundDomain
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

    suspend fun getFolioFunds(folioId: String): NetworkResponse<List<FolioFundDomain>, ErrorDomain>

    suspend fun investMoreLumpsum(body: InvestMoreDto): NetworkResponse<String, ErrorDomain>
    
    suspend fun cancelLumpSumOrder(orderId: String): NetworkResponse<Unit, ErrorDomain>

    suspend fun cancelSipOrder(xsipRegNo: String): NetworkResponse<Unit, ErrorDomain>

}