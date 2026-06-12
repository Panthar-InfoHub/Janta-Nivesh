package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class DeleteCartItemUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(id: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.deleteCartItem(id)
    }
}
