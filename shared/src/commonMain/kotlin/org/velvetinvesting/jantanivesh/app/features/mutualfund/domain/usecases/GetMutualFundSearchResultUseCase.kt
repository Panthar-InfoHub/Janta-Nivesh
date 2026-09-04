package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

/**
 * The fund list behind the search-results screen: `GET /mf/funds`.
 *
 * [tag] is the sub-category section, [category] the asset-class chip and [amountType] the
 * minimum-installment chip. Leaving one null means "do not filter on it" — the server's own
 * defaults then apply, so a bare call returns the popular list.
 */
class GetMutualFundSearchResultUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        search: String? = null,
        tag: String? = null,
        category: String? = null,
        amountType: String? = null,
        page: Int? = 1,
        limit: Int? = 20
    ): NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain> {

        return repository.getFunds(
            tag = tag,
            category = category,
            amountType = amountType,
            search = search,
            page = page,
            limit = limit
        )
    }
}
