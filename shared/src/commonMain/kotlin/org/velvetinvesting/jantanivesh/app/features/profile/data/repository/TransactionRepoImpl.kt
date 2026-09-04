package org.velvetinvesting.jantanivesh.app.features.profile.data.repository

import io.ktor.client.HttpClient
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionHistoryItem
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionType
import org.velvetinvesting.jantanivesh.app.features.profile.domain.repository.TransactionRepo

class TransactionRepoImpl(
    private val client: HttpClient
) : TransactionRepo {
    override suspend fun getTransactionHistory(type: TransactionType): NetworkResponse<List<TransactionHistoryItem>, ErrorDomain> {
        // Mocking for now as per instructions
        return NetworkResponse.Success(emptyList())
    }
}
