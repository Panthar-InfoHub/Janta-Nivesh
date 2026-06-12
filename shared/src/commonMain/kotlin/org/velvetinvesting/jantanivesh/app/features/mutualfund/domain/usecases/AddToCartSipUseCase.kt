package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.AddCartSipRequest
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class AddToCartSipUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        request: AddCartSipRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.addToCartSipFund(request)
    }
}