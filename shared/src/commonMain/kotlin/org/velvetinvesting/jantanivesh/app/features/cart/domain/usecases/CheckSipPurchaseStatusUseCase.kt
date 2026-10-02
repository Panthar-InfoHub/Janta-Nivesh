package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.SIPStatus
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class CheckSipPurchaseStatusUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain> {
        return repository.checkSipPurchaseStatus(mandateId)
    }
}
