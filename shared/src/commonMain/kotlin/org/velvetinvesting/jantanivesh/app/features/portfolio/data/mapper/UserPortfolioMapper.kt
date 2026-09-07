package org.velvetinvesting.jantanivesh.app.features.portfolio.data.mapper

import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.userportfolio.PortfolioFixedDepositDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.userportfolio.PortfolioMutualFundDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.userportfolio.UserPortfolioResponseDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.InvestedAmountBreakdownDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioAllocationDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioAllocationItemDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDashboardDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.TotalInvestmentsDomain

/**
 * `GET /user/portfolio` -> the state the portfolio screen renders. **This is the mapper in use.**
 *
 * Missing sections map to zeroes rather than throwing: the payload is still settling, and a
 * portfolio with one absent block is better shown partially than not at all.
 */
fun UserPortfolioResponseDto.toDomain(): PortfolioDomain {

    val totals = data?.total_investments
    val breakdown = data?.invested_amount_breakdown
    val summary = data?.mf_summary
    val allocation = totals?.allocation

    return PortfolioDomain(

        dashboard = PortfolioDashboardDomain(
            currentValue = totals?.current_value ?: 0.0,
            investedAmount = breakdown?.invested_amount ?: 0.0,
            totalReturns = (totals?.total_returns ?: 0.0).toInt(),
            returnPercent = totals?.return_percent ?: 0.0
        ),

        totalInvestments = TotalInvestmentsDomain(
            currentValue = totals?.current_value ?: 0.0,
            totalReturns = totals?.total_returns ?: 0.0,
            returnPercent = totals?.return_percent ?: 0.0,
            allocation = PortfolioAllocationDomain(
                mutualFunds = PortfolioAllocationItemDomain(
                    value = allocation?.mutual_funds?.value ?: 0.0,
                    percent = allocation?.mutual_funds?.percent ?: 0.0,
                    investedAmount = allocation?.mutual_funds?.invested_amount ?: 0.0,
                    totalReturns = allocation?.mutual_funds?.total_returns ?: 0.0,
                    returnPercent = allocation?.mutual_funds?.return_percent ?: 0.0,
                    oneDayReturn = allocation?.mutual_funds?.one_day_return ?: 0.0,
                    oneDayReturnPercent = allocation?.mutual_funds?.one_day_return_percent ?: 0.0,
                    xirr = allocation?.mutual_funds?.xirr ?: 0.0
                ),
                fixedDeposits = PortfolioAllocationItemDomain(
                    value = allocation?.fixed_deposits?.value ?: 0.0,
                    percent = allocation?.fixed_deposits?.percent ?: 0.0
                )
            )
        ),

        investedAmountBreakdown = InvestedAmountBreakdownDomain(
            investedAmount = breakdown?.invested_amount ?: 0.0,
            investedItemsCount = breakdown?.invested_items_count ?: 0,
            returnsAmount = breakdown?.returns_amount ?: 0.0,
            returnsPercent = breakdown?.returns_percent ?: 0.0
        ),

        mutualFunds = data?.mutual_funds.orEmpty().map { it.toDomain() },

        fixedDeposits = data?.fixed_deposits.orEmpty().map { it.toDomain() },

        // Summed server-side now, so the screen no longer recomputes it from the holdings.
        mutualFundSummary = MutualFundSummaryDomain(
            investedAmount = summary?.invested_amount ?: 0.0,
            currentValue = summary?.current_value ?: 0.0,
            returnsAmount = summary?.total_returns ?: 0.0,
            returnsPercent = summary?.return_percent ?: 0.0,
            oneDayReturn = summary?.one_day_return ?: 0.0,
            oneDayReturnPercent = summary?.one_day_return_percent ?: 0.0,
            xirr = summary?.xirr ?: 0.0
        )
    )
}

/**
 * This endpoint carries neither a scheme id nor the transaction rules the older one did, so
 * those stay at zero — nothing on the portfolio screen reads them, and the screens that do get
 * them from their own calls.
 */
fun PortfolioMutualFundDto.toDomain(): MutualFundPortfolioDomain {
    val folio = folio.orEmpty()

    return MutualFundPortfolioDomain(
        id = id,
        title = title.orEmpty(),
        category = category.orEmpty(),
        amount = amount,
        currentValue = current_value,
        returnAmount = `return`,
        returnPercentage = return_percentage.orEmpty(),
        folio = folio,
        // The two folio fields diverged in the old payload; here the same number serves both.
        actualFolio = folio,
        icon = img_url.orEmpty(),
        minSipAmount = 0L,
        minLumpSumAmount = 0L,
        schemeId = 0,
        balanceUnits = bal_units,
        isSip = is_sip == true,
        subCategory = sub_category.orEmpty(),
        xirr = xirr ?: 0.0,
        navAsOn = nav_as_on,
        folios = folios,
        currentNav = curr_nav,
        avgNav = avg_nav
    )
}

fun PortfolioFixedDepositDto.toDomain(): FixedDepositPortfolioDomain {
    return FixedDepositPortfolioDomain(
        id = id,
        amount = amount.toString(),
        roiAtBooking = roi.toString(),
        tenureAtBooking = tenure_days,
        fdIssuedAt = start_date,
        status = status,
        maturityAmount = maturity_amount.toString(),
        // Not reported per holding — the portfolio is the signed-in user's by definition.
        userId = "",
        userFullName = "",
        userEmail = "",
        issuerLogoUrl = issuer_logo.orEmpty(),
        issuerDisplayName = title.orEmpty(),
        maturityDate = maturity_date
    )
}
