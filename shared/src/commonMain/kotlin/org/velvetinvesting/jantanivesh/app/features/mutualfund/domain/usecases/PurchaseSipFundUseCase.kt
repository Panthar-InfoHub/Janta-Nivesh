package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class PurchaseSipFundUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain> {
        return repository.purchaseSipFund(mandateId=mandateId, sipItems=sipItems)
    }
}