package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper.*
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.allbundles.*
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundlecart.AddBundleLumpsumRequest
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundlecart.AddBundleSipRequest
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfundbyid.BundledFundByIdDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfunds.BundledFundsDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartaddlumpsum.AddCartLumpSumRequest
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartaddlumpsum.AddCartLumpSumResponseDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartaddsip.AddCartSipRequest as AddCartSipRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartaddsip.AddCartSipResponseDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartpurchase.CartPurchaseLumpSumDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.cartpurchase.CartPurchaseSIPDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.response.FundRedeemDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.getmf.MutualFundDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.initiatemfpurchase.InitiateMFPurchaseDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfdetails.MutualFundsDetailDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfgraph.MFGraphDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfpurchasemandatestatus.CheckMFPurchaseMandateStatusDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mutualfundcombined.CombinedFundsDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.usercart.UserCartDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SIPStatus
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CombinedFundsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundGraphDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundPurchaseInitiateDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.UserCartDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository
import org.velvetinvesting.jantanivesh.app.features.mutualfund.CartInfo
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorType
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.core.networking.safeUnitRequest

class MutualFundRepo(
    private val client: HttpClient
): MutualFundRepository {

    override suspend fun getCategoryMutualFunds(
        page: Int?,
        limit: Int?
    ): NetworkResponse<List<BundledMutualFundDomain>, ErrorDomain> {

        val response = safeRequest<BundledFundsDto> {
            client.get(getUrl("/bundles")) {
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
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

    override suspend fun getMutualFundsBySearch(
        search: String?,
        page: Int?,
        limit: Int?,
        sort: String?,
        risk: Int?,
        category: String?,
        fundCategory: String?
    ):NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain> {
        val response = safeRequest<MutualFundDto> {
            client.get(getUrl("/mf")) {
                search?.let { parameter("search", it) }
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
                sort?.let { parameter("sort_by", it) }
                risk?.let { parameter("risk", it) }
                category?.let { parameter("category", it) }
                fundCategory?.let { parameter("fund_category", it) }
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

    override suspend fun getMutualFundCart(): NetworkResponse<UserCartDomain, ErrorDomain> {
        val response= safeRequest<UserCartDto> {
            client.get(getUrl("/user/cart"))
        }

        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                val domain= response.data.toDomain()
                CartInfo.updateFundAmount(domain.sipItems.size+ domain.lumpSumItems.size)
                NetworkResponse.Success(domain)
            }
        }
    }

    override suspend fun deleteCartItem(id: String): NetworkResponse<Unit, ErrorDomain> {
        val response= safeUnitRequest {
            client.delete(getUrl("/mf/remove-cart-item")) {
                parameter("cart_item_id", id)
            }
        }
        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun clearCart(): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.delete(getUrl("/mf/clear-cart"))
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

    override suspend fun addToCartLumSumFund(
        id: String,
        amount: Long,
        folioId: String?,
    ): NetworkResponse<Unit, ErrorDomain> {
        val response= safeRequest<AddCartLumpSumResponseDto> {
            client.post(getUrl("/mf/lumpsum-cart")){
                setBody(
                    AddCartLumpSumRequest(
                        amount = amount,
                        mf_product_id = id,
                        folio= folioId?:""
                    )
                )
            }
        }

        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun addToCartSipFund(request: AddCartSipRequestDto): NetworkResponse<Unit, ErrorDomain> {
        val response= safeRequest<AddCartSipResponseDto> {
            client.post(getUrl("/mf/sip-cart")){
                setBody(
                    AddCartSipRequestDto(
                        amount = request.amount,
                        mf_product_id = request.mf_product_id,
                        sip_st_date = request.sip_st_date,
                        sip_en_date = request.sip_en_date,
                        sip_day = request.sip_day,
                        sip_amt = request.sip_amt,
                        folio = request.folio
                    )
                )
            }
        }

        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun purchaseLumSumFund(): NetworkResponse<String, ErrorDomain> {
        val response=safeRequest<CartPurchaseLumpSumDto> {
            client.post(getUrl("/mf/purchase-lumpsum"))
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data)
            }
        }
    }

    override suspend fun initiateSipPurchase(sipData: List<SipItemDomain>): NetworkResponse<MutualFundPurchaseInitiateDomain, ErrorDomain> {
        val response = safeRequest<InitiateMFPurchaseDto> {
            client.post(getUrl("/mf/initiate-sip")){
                setBody(
                    sipData.toInitiateBodyDto()
                )
            }
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.toDomain())
            }
        }
    }

    override suspend fun checkSipPurchaseStatus(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain> {
        val response=safeRequest<CheckMFPurchaseMandateStatusDto> {
            client.get(getUrl("/mf/mandate-status")){
                parameter("mandate_id", mandateId)
            }
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                val status= SIPStatus.getStatus(response.data.data.enach_status)
                if (status != null){
                    NetworkResponse.Success(status)
                }else {
                    NetworkResponse.Error(ErrorDomain(0, response.data.data.enach_status, ErrorType.UNKNOWN))
                }
            }
        }
    }

    override suspend fun purchaseSipFund(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain> {
        val response=safeRequest<CartPurchaseSIPDto> {
            client.post(getUrl("/mf/purchase-sip")){
                setBody(
                    sipItems.toInitiateBodyDto()
                )
            }
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.xsip_short_url)
            }
        }
    }

    override suspend fun getCombinedCategoryMutualFunds(): NetworkResponse<CombinedFundsDomain, ErrorDomain> {
        val response = safeRequest<CombinedFundsDto> {
            client.get(getUrl("/frontend/mf-data"))
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }
        }
    }

    override suspend fun getBundleFunds(bundleKey: String): NetworkResponse<BundledMutualFundDomain, ErrorDomain> {
        val response = safeRequest<BundledFundByIdDto> {
            client.get(getUrl("/bundles/$bundleKey"))
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }
        }
    }

    override suspend fun getAllBundledFunds(
        page: Int?,
        limit: Int?
    ): NetworkResponse<List<BundledMutualFundDomain>, ErrorDomain> {
        val response = safeRequest<AllBundlesDto> {
            client.get(getUrl("/bundles")){
                parameter("page",page?:1)
                parameter("limit", limit?:20)
            }
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success->{
                NetworkResponse.Success(response.data.toDomain())
            }
        }
    }

    override suspend fun addBundleToCartLumpsum(
        bundleId: String,
        amount: Long
    ): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.post(getUrl("/mf/bundle-cart")) {
                setBody(
                    AddBundleLumpsumRequest(
                        bundle_id = bundleId,
                        amount = amount
                    )
                )
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

    override suspend fun addBundleToCartSip(
        request: AddBundleSipRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.post(getUrl("/mf/bundle-cart")) {
                setBody(request)
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
