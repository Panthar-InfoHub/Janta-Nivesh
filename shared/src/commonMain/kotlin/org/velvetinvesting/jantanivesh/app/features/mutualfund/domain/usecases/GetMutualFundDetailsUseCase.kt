package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class GetMutualFundDetailsUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        id: String
    ): NetworkResponse<MutualFundDetailsDomain, ErrorDomain> {
        return repository.getMutualFundDetails(id)
    }
}
