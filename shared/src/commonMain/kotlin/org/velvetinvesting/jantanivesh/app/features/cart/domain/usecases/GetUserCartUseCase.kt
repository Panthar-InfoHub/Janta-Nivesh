package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.CartCountController
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.UserCartDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class GetUserCartUseCase(
    private val repository: CartRepository
) {

    /** Fetches the cart, and on success refreshes [CartCountController] for every screen showing it. */
    suspend operator fun invoke(): NetworkResponse<UserCartDomain, ErrorDomain> {
        val response = repository.getCart()
        if (response is NetworkResponse.Success) CartCountController.update(response.data)
        return response
    }
}
