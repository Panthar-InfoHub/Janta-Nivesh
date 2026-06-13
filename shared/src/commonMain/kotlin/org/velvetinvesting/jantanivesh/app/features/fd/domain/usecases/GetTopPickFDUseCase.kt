package org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FixedDepositDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.repository.FixedDepositRepository
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse

class GetTopPickFDUseCase(
    private val repository: FixedDepositRepository
) {
    suspend operator fun invoke(): NetworkResponse<List<FixedDepositDomain>, ErrorDomain> {
        val response = repository.getFDSearchResult(
            tenure = "1y",
            limit = 4,
            page = 1
        )
        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.items)
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }
}
