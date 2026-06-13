package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository


import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SIPStatus
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundlecart.AddBundleSipRequest
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartaddsip.AddCartSipRequest
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CombinedFundsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundGraphDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundPurchaseInitiateDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.UserCartDomain

interface MutualFundRepository {
    suspend fun getCategoryMutualFunds(
        page: Int? = null,
        limit: Int? = null
    ): NetworkResponse<List<BundledMutualFundDomain>, ErrorDomain>

    suspend fun getMutualFundsBySearch(
        search: String?,
        page:Int?,
        limit:Int?,
        sort:String?,
        risk:Int?,
        category:String?,
        fundCategory:String?
    ): NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain>


    suspend fun getMutualFundDetails(
        id: String
    ): NetworkResponse<MutualFundDetailsDomain, ErrorDomain>

    suspend fun getMutualFundGraph(
        id: String,
        period:String
    ): NetworkResponse<MutualFundGraphDomain, ErrorDomain>

    suspend fun getMutualFundCart() : NetworkResponse<UserCartDomain, ErrorDomain>

    suspend fun deleteCartItem(id: String) : NetworkResponse<Unit, ErrorDomain>

    suspend fun clearCart() : NetworkResponse<Unit, ErrorDomain>


    suspend fun addToCartLumSumFund(id: String, amount: Long, folioId: String?): NetworkResponse<Unit, ErrorDomain>
    suspend fun addToCartSipFund(request: AddCartSipRequest): NetworkResponse<Unit, ErrorDomain>

    suspend fun purchaseLumSumFund(): NetworkResponse<String, ErrorDomain>

    suspend fun initiateSipPurchase(sipData: List<SipItemDomain>): NetworkResponse<MutualFundPurchaseInitiateDomain, ErrorDomain>
    suspend fun checkSipPurchaseStatus(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain>

    suspend fun purchaseSipFund(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain>

    suspend fun getCombinedCategoryMutualFunds(): NetworkResponse<CombinedFundsDomain, ErrorDomain>

    suspend fun getBundleFunds(bundleKey: String): NetworkResponse<BundledMutualFundDomain, ErrorDomain>

    suspend fun getAllBundledFunds(
        page: Int?,
        limit: Int?
    ): NetworkResponse<List<BundledMutualFundDomain>, ErrorDomain>

    suspend fun addBundleToCartLumpsum(
        bundleId: String,
        amount: Long
    ): NetworkResponse<Unit, ErrorDomain>

    suspend fun addBundleToCartSip(
        request: AddBundleSipRequest
    ): NetworkResponse<Unit, ErrorDomain>

    suspend fun redeemPartialFund(data: PartialRedemptionRequestDto): NetworkResponse<String, ErrorDomain>
    suspend fun redeemFullFund(data: FullRedemptionRequestDto): NetworkResponse<String, ErrorDomain>

}

