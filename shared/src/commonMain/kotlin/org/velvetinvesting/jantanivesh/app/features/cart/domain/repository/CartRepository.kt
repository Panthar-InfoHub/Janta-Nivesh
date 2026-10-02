package org.velvetinvesting.jantanivesh.app.features.cart.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.bundlecart.AddBundleLumpsumRequest
import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.bundlecart.AddBundleSipRequest
import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.cartaddsip.AddCartSipRequest
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.MutualFundPurchaseInitiateDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.SIPStatus
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.UserCartDomain

interface CartRepository {
    /** `GET /user/cart` */
    suspend fun getCart(): NetworkResponse<UserCartDomain, ErrorDomain>

    /** `POST /mf/cart` — a one-time fund. */
    suspend fun addToCartLumpsum(mfProductId: String, amount: Long): NetworkResponse<Unit, ErrorDomain>

    /** `POST /mf/cart` — a SIP fund. */
    suspend fun addToCartSip(request: AddCartSipRequest): NetworkResponse<Unit, ErrorDomain>

    /** `POST /mf/cart/bundle` — a one-time bundle. */
    suspend fun addBundleToCartLumpsum(request: AddBundleLumpsumRequest): NetworkResponse<Unit, ErrorDomain>

    /** `POST /mf/cart/bundle` — a SIP bundle. */
    suspend fun addBundleToCartSip(request: AddBundleSipRequest): NetworkResponse<Unit, ErrorDomain>

    /** `DELETE /mf/cart/{id}` */
    suspend fun deleteCartItem(id: String): NetworkResponse<Unit, ErrorDomain>

    /** `DELETE /mf/cart` */
    suspend fun clearCart(): NetworkResponse<Unit, ErrorDomain>

    suspend fun purchaseLumpSum(): NetworkResponse<String, ErrorDomain>

    suspend fun initiateSipPurchase(sipData: List<SipItemDomain>): NetworkResponse<MutualFundPurchaseInitiateDomain, ErrorDomain>

    suspend fun checkSipPurchaseStatus(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain>

    suspend fun purchaseSip(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain>
}
