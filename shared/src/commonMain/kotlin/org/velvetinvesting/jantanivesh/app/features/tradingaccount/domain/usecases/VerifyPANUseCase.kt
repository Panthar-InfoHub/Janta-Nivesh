package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.PANVerifyDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.repository.TradingAccountRepo

class VerifyPANUseCase(
    private val userAuth: TradingAccountRepo
) {
    suspend operator fun invoke(pan: String): NetworkResponse<PANVerifyDomain, ErrorDomain> {
        return userAuth.verifyPAN(pan)
    }
}