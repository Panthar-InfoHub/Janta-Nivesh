package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class GetAllBundledFundsUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        page: Int?=null,
        limit: Int?=null
    ): NetworkResponse<List<BundledMutualFundDomain>, ErrorDomain> {
        return repository.getAllBundledFunds(
            page=page,
            limit=limit
        )
    }
}