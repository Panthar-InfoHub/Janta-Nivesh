package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.bundlecart.AddBundleSipRequest
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class AddBundleToCartSipUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        request: AddBundleSipRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.addBundleToCartSip(request)
    }
}
