package org.velvetinvesting.jantanivesh.app.features.profile.domain.usecase

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionHistoryItem
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionType
import org.velvetinvesting.jantanivesh.app.features.profile.domain.repository.TransactionRepo

class GetTransactionsUseCase(
    private val repository: TransactionRepo
) {
    suspend operator fun invoke(type: TransactionType): NetworkResponse<List<TransactionHistoryItem>, ErrorDomain> {
        return repository.getTransactionHistory(type)
    }
}
