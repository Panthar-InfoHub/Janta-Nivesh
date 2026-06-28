package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models

data class SIPDetailsDomain(
    val id: String,
    val icon:String,
    val fundName:String,
    val fundCategory:String,
    val amount:String,
    val metadata: List<Pair<String,String>>?,
    val nextInstallment:String,
    val sipId:String,
    val autopayId:String,
    val transactionHistory: List<TransactionHistoryDomain>,
    val bankDetails: BankDetails
)
