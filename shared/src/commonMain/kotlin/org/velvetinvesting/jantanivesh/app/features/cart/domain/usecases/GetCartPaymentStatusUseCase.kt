package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.CartPaymentStatusDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class GetCartPaymentStatusUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(paymentId: String): NetworkResponse<CartPaymentStatusDomain, ErrorDomain> {
        return repository.getPaymentStatus(paymentId)
    }
}
