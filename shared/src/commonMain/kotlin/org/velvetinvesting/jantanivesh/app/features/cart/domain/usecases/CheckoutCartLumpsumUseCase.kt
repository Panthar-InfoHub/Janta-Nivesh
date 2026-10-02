package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.CartCheckoutDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class CheckoutCartLumpsumUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(): NetworkResponse<CartCheckoutDomain, ErrorDomain> {
        return repository.checkoutLumpsum()
    }
}
