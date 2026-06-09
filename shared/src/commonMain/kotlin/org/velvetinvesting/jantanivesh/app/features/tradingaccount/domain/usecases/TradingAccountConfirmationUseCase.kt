package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.repository.TradingAccountRepo

class TradingAccountConfirmationUseCase(
    private val repository: TradingAccountRepo,
) {
    suspend operator fun invoke(
        taxStatus: String,
        holdingNature: String,
        jointHolderName1: String,
        jointHolderName2: String,
        guardianName: String,
        isMinor: Boolean
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.tradingAccountConfirmation(
            taxStatus = taxStatus,
            holdingNature = holdingNature,
            jointHolderName1 = jointHolderName1,
            jointHolderName2 = jointHolderName2,
            guardianName = guardianName,
            isMinor = isMinor
        )
    }
}
