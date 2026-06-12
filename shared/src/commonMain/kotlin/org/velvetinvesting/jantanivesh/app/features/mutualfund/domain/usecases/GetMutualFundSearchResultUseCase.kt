package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class GetMutualFundSearchResultUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        search: String?=null,
        page:Int?=1,
        limit:Int?=20,
        sort:String?=null,
        risk:Int?=null,
        category:String?=null,
        fundCategory:String?=null
    ): NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain> {

        return repository.getMutualFundsBySearch(search, page, limit, sort, risk, category,fundCategory)
    }
}
