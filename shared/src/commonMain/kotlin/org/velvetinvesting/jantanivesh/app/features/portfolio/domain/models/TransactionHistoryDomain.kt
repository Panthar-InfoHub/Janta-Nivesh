package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models

import org.velvetinvesting.jantanivesh.app.domain.TransactionStatus

data class TransactionHistoryDomain(
    val title:String,
    val date:String,
    val type: TransactionStatus
)
