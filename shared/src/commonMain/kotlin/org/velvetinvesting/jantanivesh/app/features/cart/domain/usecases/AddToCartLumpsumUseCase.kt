package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class AddToCartLumpsumUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        id: String,
        amount: Long
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.addToCartLumpsum(
            mfProductId = id,
            amount = amount
        )
    }
}
