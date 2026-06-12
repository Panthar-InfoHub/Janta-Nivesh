package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundGraphDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class GetMutualFundGraphUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        id: String,
        selectedYear: String
    ): NetworkResponse<MutualFundGraphDomain, ErrorDomain> {
        return repository.getMutualFundGraph(id,selectedYear)
    }
}
