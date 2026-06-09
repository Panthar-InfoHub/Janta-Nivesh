package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountPrefilledDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.repository.TradingAccountRepo


class GetTradingAccountPrefilledDataUseCase(
    private val repository: TradingAccountRepo
) {
    suspend operator fun invoke(): NetworkResponse<TradingAccountPrefilledDomain, ErrorDomain> {
        return repository.getTradingAccountPrefilledData()
    }
}