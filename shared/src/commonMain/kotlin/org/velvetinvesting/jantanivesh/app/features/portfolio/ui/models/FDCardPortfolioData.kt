package org.sharad.velvetinvestment.presentation.portfolio.models

data class FDCardPortfolioData(
    val id: String,
    val icon:String,
    val fundName:String,
    val fundNumber:String,
    val principle:Long,
    val rate: Double,
    val maturity:Long
)
