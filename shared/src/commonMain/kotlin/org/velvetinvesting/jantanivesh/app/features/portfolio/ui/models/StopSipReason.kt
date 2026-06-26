package org.sharad.velvetinvestment.presentation.portfolio.models

data class StopSipReason(
    val id: Int,
    val title: String
)

val stopSipReasons = listOf(
    StopSipReason(1, "Non availability of Funds"),
    StopSipReason(2, "Scheme not performing"),
    StopSipReason(3, "Service issue"),
    StopSipReason(4, "Load Revised"),
    StopSipReason(5, "Wish to invest in other schemes"),
    StopSipReason(6, "Change in Fund Manager"),
    StopSipReason(7, "Goal Achieved"),
    StopSipReason(8, "Not comfortable with market volatility"),
    StopSipReason(9, "Will be restarting SIP after few months"),
    StopSipReason(10, "Modifications in bank/mandate/date etc"),
    StopSipReason(11, "I have decided to invest elsewhere"),
    StopSipReason(12, "Rate is not the right size"),
    StopSipReason(13, "Others (pls specify the reason)")
)

