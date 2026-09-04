package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.MfFilterIds
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

/**
 * The four funds the explore tab leads with.
 *
 * `GET /mf/funds` has no sort of its own, so "top" is whatever the popular section returns first
 * — the server owns that ordering now, rather than this asking for a 3-year sort.
 */
class GetMutualFundTopPicksUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(): NetworkResponse<List<MutualFundDomain>, ErrorDomain> {
        val response = repository.getFunds(
            tag = MfFilterIds.TAG_POPULAR,
            page = 1,
            limit = TOP_PICKS_COUNT
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

    private companion object {
        const val TOP_PICKS_COUNT = 4
    }
}
