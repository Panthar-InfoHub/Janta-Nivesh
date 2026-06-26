package org.sharad.velvetinvestment.presentation.portfolio.models

data class FDDetailsUiModel(
    val bankName: String,
    val fdAccountNumber: String,

    val principalAmount: String,
    val interestRate: String,
    val tenure: String,
    val maturityAmount: String,
    val interestEarnedTillDate: String,

    val nominees: List<FDNomineeUiModel>,

    val startDate: String,
    val maturityDate: String,
    val daysRemaining: String
)

data class FDNomineeUiModel(
    val nomineeId: String,
    val fullName: String,
    val relationship: String,
    val dateOfBirth: String
)
