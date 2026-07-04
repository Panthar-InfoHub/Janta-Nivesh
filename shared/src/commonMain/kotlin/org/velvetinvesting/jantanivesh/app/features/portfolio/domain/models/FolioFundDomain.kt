package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models

data class FolioFundDomain(
    val id: String,
    val schemeId: Int,
    val title: String,
    val category: String,
    val amount: Long,
    val isSip: Boolean,
    val startDate: String,
    val returnPercentage: String,
    val `return`: Double,
    val xirr: String,
    val currentNav: Double,
    val avgNav: Double,
    val folio: String,
    val balanceUnits: Double,
    val imgUrl: String,
    val orderId: String,
    val actualFolio: String
)