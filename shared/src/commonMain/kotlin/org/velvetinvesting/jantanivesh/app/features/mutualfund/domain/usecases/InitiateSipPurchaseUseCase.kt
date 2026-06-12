package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundPurchaseInitiateDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class InitiateSipPurchaseUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(sipData: List<SipItemDomain>): NetworkResponse<MutualFundPurchaseInitiateDomain, ErrorDomain> {
        return repository.initiateSipPurchase(sipData=sipData)
    }
}
