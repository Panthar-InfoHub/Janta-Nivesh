package org.velvetinvesting.jantanivesh.app.features.profile.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionHistoryItem
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionType

interface TransactionRepo {
    suspend fun getTransactionHistory(type: TransactionType): NetworkResponse<List<TransactionHistoryItem>, ErrorDomain>
}
