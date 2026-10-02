package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.cartaddsip.AddCartSipRequest
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class AddToCartSipUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        request: AddCartSipRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.addToCartSip(request)
    }
}