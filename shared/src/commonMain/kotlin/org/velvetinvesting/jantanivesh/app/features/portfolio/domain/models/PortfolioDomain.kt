package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models

data class MutualFundPortfolioDomain(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val currentValue: Double,
    val returnAmount: Double,
    val returnPercentage: String,
    val folio: String,
    val actualFolio: String,
    val icon: String,

    val minSipAmount: Long,
    val minLumpSumAmount: Long,
    val schemeId: Int,
    val balanceUnits: Double,

    /**
     * Whether the holding was built by a SIP. The portfolio payload does not report this yet, so
     * it reads false until it does — the card's tag is driven by it.
     */
    val isSip: Boolean = false,
    val subCategory: String = "",
    val xirr: Double = 0.0,
    val navAsOn: String? = null,
    /** Every folio rolled into this row; [folio] is the one shown. */
    val folios: List<String> = emptyList(),
    val currentNav: Double = 0.0,
    val avgNav: Double = 0.0
)

data class FixedDepositPortfolioDomain(
    val id: String,
    val amount: String,
    val roiAtBooking: String,
    val tenureAtBooking: Int,
    val fdIssuedAt: String?,
    val status: String,
    val maturityAmount: String?,
    val userId: String,
    val userFullName: String,
    val userEmail: String,
    val issuerLogoUrl: String,
    val issuerDisplayName: String,
    val maturityDate: String?
)

data class PortfolioDashboardDomain(
    val currentValue: Double,
    val investedAmount: Double,
    val totalReturns: Int,
    val returnPercent: Double
)

data class PortfolioAllocationDomain(
    val mutualFunds: PortfolioAllocationItemDomain,
    val fixedDeposits: PortfolioAllocationItemDomain
)

data class PortfolioAllocationItemDomain(
    val value: Double,
    val percent: Double,
    /** Reported for the mutual-fund slice only; the fixed-deposit slice is value and percent. */
    val investedAmount: Double = 0.0,
    val totalReturns: Double = 0.0,
    val returnPercent: Double = 0.0,
    val oneDayReturn: Double = 0.0,
    val oneDayReturnPercent: Double = 0.0,
    val xirr: Double = 0.0
)

data class TotalInvestmentsDomain(
    val currentValue: Double,
    val totalReturns: Double,
    val returnPercent: Double,
    val allocation: PortfolioAllocationDomain
)

data class InvestedAmountBreakdownDomain(
    val investedAmount: Double,
    val investedItemsCount: Int,
    val returnsAmount: Double,
    val returnsPercent: Double
)

data class MutualFundSummaryDomain(
    val investedAmount: Double,
    val currentValue: Double,
    val returnsAmount: Double,
    val returnsPercent: Double,
    val oneDayReturn: Double = 0.0,
    val oneDayReturnPercent: Double = 0.0,
    val xirr: Double = 0.0
)

/**
 * One running purchase plan, from `GET /mf/purchase-plan`.
 *
 * That endpoint describes the *instruction*, not a holding: it reports what is debited each
 * cycle and when the next debit falls, but nothing about what the plan has accumulated so far.
 * The model therefore carries [installmentAmount] rather than an invested total, and leaves the
 * installment counts null for a perpetual SIP that has none.
 */
data class ActiveSipItemDomain(
    val id: String,
    val fundName: String,
    /** e.g. "Equity". */
    val fundCategory: String,
    /** e.g. "Equity Fund". */
    val fundType: String,
    /** Debited each cycle. */
    val installmentAmount: Double,
    /** "Monthly", "Daily" — title-cased for display. */
    val frequency: String,
    /** "05 Oct 2026", or blank when the gateway has not scheduled the next debit. */
    val nextDueDate: String,
    val folioNumber: String,
    val isin: String,
    val totalInstallments: Int?,
    val remainingInstallments: Int?,
    val latestNav: Double,
    val iconUrl: String = ""
)

/**
 * The plans split the way the tab's two sub-tabs ask for them. The endpoint's `frequency` is
 * free-form, so anything that is not daily lands in [monthlySips] — a weekly plan is closer to a
 * monthly one than to a daily one, and hiding it entirely would be worse than filing it here.
 */
data class ActiveSipDomain(
    /** Sum of every plan's per-cycle amount — what the SIPs commit, not what they have built. */
    val totalInstallmentAmount: Double,
    val monthlySips: List<ActiveSipItemDomain>,
    val dailySips: List<ActiveSipItemDomain>
) {
    val isEmpty: Boolean get() = monthlySips.isEmpty() && dailySips.isEmpty()

    companion object {
        val EMPTY = ActiveSipDomain(0.0, emptyList(), emptyList())
    }
}

data class PortfolioDomain(
    val dashboard: PortfolioDashboardDomain,
    val totalInvestments: TotalInvestmentsDomain,
    val investedAmountBreakdown: InvestedAmountBreakdownDomain,
    val mutualFunds: List<MutualFundPortfolioDomain>,
    val fixedDeposits: List<FixedDepositPortfolioDomain>,
    val mutualFundSummary: MutualFundSummaryDomain
)