package org.sharad.velvetinvestment.presentation.portfolio.models

sealed interface SelectedPortfolio {
    data object Dashboard: SelectedPortfolio
    data object MutualFunds: SelectedPortfolio
    data object ActiveSIP: SelectedPortfolio
    data object FixedDeposits: SelectedPortfolio

    companion object {
        val tabs = listOf(Dashboard, MutualFunds, ActiveSIP, FixedDeposits)
    }
}

fun SelectedPortfolio.label(): String = when (this) {
    SelectedPortfolio.Dashboard -> "Dashboard"
    SelectedPortfolio.MutualFunds -> "Mutual Funds"
    SelectedPortfolio.ActiveSIP -> "Active SIP"
    SelectedPortfolio.FixedDeposits -> "Fixed Deposits"
}
