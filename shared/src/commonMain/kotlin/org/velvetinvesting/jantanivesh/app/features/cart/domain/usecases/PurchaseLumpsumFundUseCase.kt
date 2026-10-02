package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class PurchaseLumpsumFundUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(): NetworkResponse<String, ErrorDomain> {
        return repository.purchaseLumpSum()
    }
}