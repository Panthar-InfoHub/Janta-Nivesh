package org.velvetinvesting.jantanivesh.app.features.portfolio.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorType
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.cancelorder.CancelOrderRequestDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.cancelorder.CancelOrderResponseDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.cancelorder.CancelXsipRequestDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.cancelorder.CancelXsipResponseDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.fdportfoliobyid.FDPortFolioById
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.fdredirect.FDRedirectDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.fdredirect.RedirectBody
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.investmore.InvestMoreLumpsumResponseDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.pendingorders.PendingOrdersDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio.FolioFundsDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio.UserPortFolioDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.report.ReportExportDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositTransactionDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FolioFundDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingOrderDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.models.InvestMoreDto

class PortfolioRepoImpl(
    private val client: HttpClient
): PortfolioRepo {

    override suspend fun getPortfolio(): NetworkResponse<PortfolioDomain, ErrorDomain> {
        val response= safeRequest<UserPortFolioDto> {
            client.get(getUrl("/user/portfolio"))
        }

        return when(response){
            is NetworkResponse.Error->{
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success ->{
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
        }
    }

    override suspend fun getFDPortfolioById(id: String): NetworkResponse<FixedDepositTransactionDomain, ErrorDomain> {
        val response = safeRequest<FDPortFolioById> {
            client.get(getUrl("/fd/transactions/$id"))
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
        }
    }

    override suspend fun getFDRedirectUrl(
        id: String,
        event: String
    ): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<FDRedirectDto> {
            client.post(
                getUrl("/fd/redirect-url")
            ) {
                setBody(
                    RedirectBody(
                        fd_trans_id = id,
                        event = event
                    )
                )
            }
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.data.redirectionUrl)
            }
        }
    }


    override suspend fun exportReport(
        type: String,
        year: Int?,
        folio: String?,
        expand: Int?
    ): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<ReportExportDto> {
            client.get(getUrl("/report")) {
                parameter("type", type)
                year?.let { parameter("year", it) }
                folio?.let { parameter("folio", it) }
                expand?.let { parameter("expand", it) }
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                val url = response.data.data.result
                if (url==null){
                     NetworkResponse.Error(ErrorDomain(
                        code = -1,
                        message = "No Data Found",
                        type = ErrorType.UNKNOWN
                    ))
                }
                else
                NetworkResponse.Success(url)
            }
        }
    }

    override suspend fun getPendingOrders(): NetworkResponse<List<PendingOrderDomain>, ErrorDomain> {
        val response = safeRequest<PendingOrdersDto> {
            client.get(getUrl("/user/pending-orders"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.data?.pending_orders?.map { it.toDomain() } ?: emptyList()
                )
            }
        }
    }

    override suspend fun getFolioFunds(folioId: String): NetworkResponse<List<FolioFundDomain>, ErrorDomain> {
        val response = safeRequest<FolioFundsDto> {
            client.get(getUrl("/user/portfolio/$folioId"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.data.map { it.toDomain() }
                )
            }
        }
    }

    override suspend fun investMoreLumpsum(body: InvestMoreDto): NetworkResponse<String, ErrorDomain> {
        val response= safeRequest<InvestMoreLumpsumResponseDto> {
            client.post(getUrl("/mf/invest-more")) {
                setBody(body)
            }
        }
        when(response){
            is NetworkResponse.Error -> {
                return NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                return NetworkResponse.Success(response.data.paymentUrl)
            }
        }
    }

    override suspend fun cancelLumpSumOrder(orderId: String): NetworkResponse<Unit, ErrorDomain> {
        val response = safeRequest<CancelOrderResponseDto> {
            client.post(getUrl("/mf/cancel-order")) {
                setBody(CancelOrderRequestDto(order_no = orderId))
            }
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun cancelSipOrder(xsipRegNo: String): NetworkResponse<Unit, ErrorDomain> {
        val response = safeRequest<CancelXsipResponseDto> {
            client.post(getUrl("/mf/cancel-xsip")) {
                setBody(CancelXsipRequestDto(xsip_reg_no = xsipRegNo))
            }
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

}