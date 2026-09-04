package org.velvetinvesting.jantanivesh.app.features.profile.domain.model

data class TransactionHistoryItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val date: String,
    val status: TransactionStatus,
    val type: TransactionType,
    val isCredit: Boolean = false
)

enum class TransactionStatus {
    SUCCESSFUL, PENDING, FAILED
}

enum class TransactionType {
    MUTUAL_FUND, FIXED_DEPOSIT
}

data class TransactionGroup(
    val dateHeader: String,
    val transactions: List<TransactionHistoryItem>
)
