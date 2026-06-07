package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.PANVerifyDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountPrefilledDomain

interface TradingAccountRepo {
    suspend fun getTradingAccountPrefilledData(): NetworkResponse<TradingAccountPrefilledDomain, ErrorDomain>
    suspend fun  verifyPAN(pan: String): NetworkResponse<PANVerifyDomain, ErrorDomain>
    suspend fun submitTradingAccountForm(data: TradingAccountFormDomain) : NetworkResponse<String, ErrorDomain>
    suspend fun tradingAccountConfirmation(
        taxStatus: String,
        holdingNature: String,
        jointHolderName1: String,
        jointHolderName2: String,
        guardianName: String,
        isMinor: Boolean
    ): NetworkResponse<Unit, ErrorDomain>
}