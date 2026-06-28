package org.sharad.velvetinvestment.presentation.portfolio.models

data class MutualFundDashBoardData(
    val total:Int,
    val currentValue:Long,
    val investedAmount:Long,
    val totalReturns:Long,
    val totalReturnsPercentage:Double,
    val oneDayReturns:Long,
    val oneDayReturnsPercentage:Double,
)
