package org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FixedDepositDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.fd.domain.repository.FixedDepositRepository
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse

class GetFixedDepositsSearchResultUseCase(
    private val repository: FixedDepositRepository,
) {

    suspend operator fun invoke(
        page: Int? = 1,
        limit: Int? = 30,
        tenure: String? = null,
        payoutFrequency: String? = null,
        minDeposit: Double? = null,
        maxDeposit: Double? = null,
        search: String? = null
    ): NetworkResponse<PaginatedData<FixedDepositDomain>, ErrorDomain> {

        return repository.getFDSearchResult(
            page = page,
            limit = limit,
            tenure = tenure,
            payoutFrequency = payoutFrequency,
            minDeposit = minDeposit,
            maxDeposit = maxDeposit,
            search = search
        )
    }
}
