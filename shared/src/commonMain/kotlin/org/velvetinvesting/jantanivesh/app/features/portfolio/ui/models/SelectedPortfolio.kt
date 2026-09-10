package org.sharad.velvetinvestment.presentation.portfolio.models

sealed interface SelectedPortfolio {
    data object Dashboard: SelectedPortfolio
    data object MutualFunds: SelectedPortfolio
    data object ActiveSIP: SelectedPortfolio
    data object FixedDeposits: SelectedPortfolio
    data object Orders: SelectedPortfolio

    companion object {
        /** The pager reads its page count from this list, so order here is page order there. */
        val tabs = listOf(Dashboard, MutualFunds, ActiveSIP, FixedDeposits, Orders)
    }
}

fun SelectedPortfolio.label(): String = when (this) {
    SelectedPortfolio.Dashboard -> "Dashboard"
    SelectedPortfolio.MutualFunds -> "Mutual Funds"
    SelectedPortfolio.ActiveSIP -> "Active SIP"
    SelectedPortfolio.FixedDeposits -> "Fixed Deposits"
    SelectedPortfolio.Orders -> "My Orders"
}
