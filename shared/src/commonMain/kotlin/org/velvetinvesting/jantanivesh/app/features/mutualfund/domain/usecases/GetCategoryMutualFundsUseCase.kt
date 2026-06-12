package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class GetCategoryMutualFundsUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke():
            NetworkResponse<List<BundledMutualFundDomain>, ErrorDomain> {
        return repository.getCategoryMutualFunds()
    }
}
