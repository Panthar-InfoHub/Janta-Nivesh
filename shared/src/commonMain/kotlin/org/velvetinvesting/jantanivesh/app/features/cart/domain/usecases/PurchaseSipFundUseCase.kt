package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class PurchaseSipFundUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain> {
        return repository.purchaseSip(mandateId=mandateId, sipItems=sipItems)
    }
}