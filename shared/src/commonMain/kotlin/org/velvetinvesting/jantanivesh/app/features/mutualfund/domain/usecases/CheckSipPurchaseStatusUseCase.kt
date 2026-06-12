package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SIPStatus
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class CheckSipPurchaseStatusUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain> {
        return repository.checkSipPurchaseStatus(mandateId)
    }
}
