package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class RedeemPartialFundUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        data: PartialRedemptionRequestDto
    ): NetworkResponse<String, ErrorDomain> {
        return repository.redeemPartialFund(data)
    }
}
