package org.sharad.velvetinvestment.presentation.portfolio.models

sealed interface SelectedPortfolio {
    data object Dashboard: SelectedPortfolio
    data object MutualFunds: SelectedPortfolio
    data object ActiveSIP: SelectedPortfolio
    data object FixedDeposits: SelectedPortfolio

    companion object {
        // ActiveSIP is out of the tab strip until `GET /user/portfolio` reports active SIPs —
        // the tab it drives has nothing to show. Put it back between MutualFunds and
        // FixedDeposits when it does; the pager reads its page count from this list.
        val tabs = listOf(Dashboard, MutualFunds, /* ActiveSIP, */ FixedDeposits)
    }
}

fun SelectedPortfolio.label(): String = when (this) {
    SelectedPortfolio.Dashboard -> "Dashboard"
    SelectedPortfolio.MutualFunds -> "Mutual Funds"
    SelectedPortfolio.ActiveSIP -> "Active SIP"
    SelectedPortfolio.FixedDeposits -> "Fixed Deposits"
}
