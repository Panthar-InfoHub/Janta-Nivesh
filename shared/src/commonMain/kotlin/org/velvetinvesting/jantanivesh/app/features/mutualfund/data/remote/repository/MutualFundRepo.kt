package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper.toPaginatedDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.response.FundRedeemDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mffunds.MfFundsDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfdetails.MutualFundsDetailDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfgraph.MFGraphDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.frontendmfdata.FrontendMfDataDto
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CategoryMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundGraphDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest

class MutualFundRepo(
    private val client: HttpClient
): MutualFundRepository {

    override suspend fun getCategoryMutualFunds(): NetworkResponse<List<CategoryMutualFundDomain>, ErrorDomain> {

        val response = safeRequest<FrontendMfDataDto> {
            client.get(getUrl("/frontend/mf-data"))
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

    override suspend fun getFunds(
        tag: String?,
        category: String?,
        amountType: String?,
        search: String?,
        page: Int?,
        limit: Int?,
        investmentMode: String?
    ): NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain> {
        val response = safeRequest<MfFundsDto> {
            client.get(getUrl("/mf/funds")) {
                // Blank is not a filter: an empty search or tag would narrow the list to nothing
                // server-side, so those are dropped rather than sent.
                tag?.takeIf { it.isNotBlank() }?.let { parameter("tag", it) }
                category?.takeIf { it.isNotBlank() }?.let { parameter("category", it) }
                amountType?.takeIf { it.isNotBlank() }?.let { parameter("amount_type", it) }
                investmentMode?.takeIf { it.isNotBlank() }?.let { parameter("investment_mode", it) }
                search?.takeIf { it.isNotBlank() }?.let { parameter("search", it) }
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toPaginatedDomain())
            }

            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun getMutualFundDetails(id: String): NetworkResponse<MutualFundDetailsDomain, ErrorDomain> {
        val response = safeRequest< MutualFundsDetailDto> {
            client.get(getUrl("/mf/$id")) {
            }
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

    override suspend fun getMutualFundGraph(
        id: String,
        period: String,
    ): NetworkResponse<MutualFundGraphDomain, ErrorDomain> {
        val response = safeRequest<MFGraphDto> {
            client.get(getUrl("/mf/history/${id}")) {
                parameter("period", period)
            }
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

    override suspend fun redeemPartialFund(data: PartialRedemptionRequestDto): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<FundRedeemDto> {
            client.post(getUrl("/mf/redeem")) { setBody(data) }
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.payment_link)
            }
        }
    }

    override suspend fun redeemFullFund(data: FullRedemptionRequestDto): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<FundRedeemDto> {
            client.post(getUrl("/mf/redeem")) { setBody(data) }
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.payment_link)
            }
        }
    }
}
