package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class GetMutualFundTopPicksUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(): NetworkResponse<List<MutualFundDomain>, ErrorDomain> {
        val response= repository.getMutualFundsBySearch(
            search = null,
            page = 1,
            limit = 4,
            sort = "3y",
            risk = null,
            category = null,
            fundCategory = null
        )
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.items)
            }

            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }
}
