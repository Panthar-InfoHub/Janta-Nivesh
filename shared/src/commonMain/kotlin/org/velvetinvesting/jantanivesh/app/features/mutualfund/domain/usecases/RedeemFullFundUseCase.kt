package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class RedeemFullFundUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        data: FullRedemptionRequestDto
    ): NetworkResponse<String, ErrorDomain> {
        return repository.redeemFullFund(data)
    }
}
