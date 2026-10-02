package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository


import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CategoryMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundGraphDomain

interface MutualFundRepository {
    suspend fun getCategoryMutualFunds(): NetworkResponse<List<CategoryMutualFundDomain>, ErrorDomain>

    /**
     * `GET /mf/funds` — the browse-and-search list.
     *
     * Every filter is optional and omitted from the query when null, which lets the server apply
     * its own defaults (`tag=popular`, `category=all`) rather than this layer guessing them.
     */
    suspend fun getFunds(
        tag: String? = null,
        category: String? = null,
        amountType: String? = null,
        search: String? = null,
        page: Int? = null,
        limit: Int? = null
    ): NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain>


    suspend fun getMutualFundDetails(
        id: String
    ): NetworkResponse<MutualFundDetailsDomain, ErrorDomain>

    suspend fun getMutualFundGraph(
        id: String,
        period:String
    ): NetworkResponse<MutualFundGraphDomain, ErrorDomain>

    suspend fun redeemPartialFund(data: PartialRedemptionRequestDto): NetworkResponse<String, ErrorDomain>
    suspend fun redeemFullFund(data: FullRedemptionRequestDto): NetworkResponse<String, ErrorDomain>

}

