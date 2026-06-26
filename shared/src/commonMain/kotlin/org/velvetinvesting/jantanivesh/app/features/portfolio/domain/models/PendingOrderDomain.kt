package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models

data class PendingOrderDomain(
    val id: String,
    val type: String,
    val schemeName: String,
    val amount: Double,
    val date: String,
    val status: String,
    val statusRemark: String,
    val amc: String,
    val frequency: String,
    val startDate: String,
    val icon: String
)