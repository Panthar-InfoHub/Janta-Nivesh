package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.UserCartDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class GetUserCartUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(): NetworkResponse<UserCartDomain, ErrorDomain> {
        return repository.getMutualFundCart()
    }
}
