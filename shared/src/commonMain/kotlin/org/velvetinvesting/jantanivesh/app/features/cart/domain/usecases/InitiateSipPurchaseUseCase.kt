package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.MutualFundPurchaseInitiateDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class InitiateSipPurchaseUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(sipData: List<SipItemDomain>): NetworkResponse<MutualFundPurchaseInitiateDomain, ErrorDomain> {
        return repository.initiateSipPurchase(sipData=sipData)
    }
}
