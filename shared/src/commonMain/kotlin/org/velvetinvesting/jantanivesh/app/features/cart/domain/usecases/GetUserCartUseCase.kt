package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.UserCartDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class GetUserCartUseCase(
    private val repository: CartRepository
) {

    suspend operator fun invoke(): NetworkResponse<UserCartDomain, ErrorDomain> {
        return repository.getCart()
    }
}
