package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class PurchaseLumpsumFundUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(): NetworkResponse<String, ErrorDomain> {
        return repository.purchaseLumSumFund()
    }
}