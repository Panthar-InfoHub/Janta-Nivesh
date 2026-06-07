package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.repository.TradingAccountRepo

class SubmitTradingAccountFormUseCase(
    private val repository: TradingAccountRepo
) {
    suspend operator fun invoke(data: TradingAccountFormDomain): NetworkResponse<String, ErrorDomain> {
        return repository.submitTradingAccountForm(data)
    }
}
