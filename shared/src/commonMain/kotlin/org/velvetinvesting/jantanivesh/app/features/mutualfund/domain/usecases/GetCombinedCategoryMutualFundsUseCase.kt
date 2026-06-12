package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CombinedFundsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class GetCombinedCategoryMutualFundsUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(): NetworkResponse<CombinedFundsDomain, ErrorDomain> {
        return repository.getCombinedCategoryMutualFunds()
    }
}
