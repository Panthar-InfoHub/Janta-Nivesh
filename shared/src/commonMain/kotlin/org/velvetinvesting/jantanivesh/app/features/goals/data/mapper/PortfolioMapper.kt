package org.velvetinvesting.jantanivesh.app.features.goals.data.mapper

import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.portfolio.UserPortFolioDto
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.FixedDepositPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.InvestedAmountBreakdownDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.MutualFundPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.PortfolioAllocationDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.PortfolioAllocationItemDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.PortfolioDashboardDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.TotalInvestmentsDomain
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.portfolio.MutualFund
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.portfolio.FdTransaction as FdTransactionDto

fun UserPortFolioDto.toDomain(): PortfolioDomain {

    val totalInvestments = data.total_investments
    val investedBreakdown = data.invested_amount_breakdown

    return PortfolioDomain(

        dashboard = PortfolioDashboardDomain(
            currentValue = totalInvestments.current_value,
            investedAmount = investedBreakdown.invested_amount,
            totalReturns = totalInvestments.total_returns.toInt(),
            returnPercent = totalInvestments.return_percent
        ),

        totalInvestments = TotalInvestmentsDomain(
            currentValue = totalInvestments.current_value,

            totalReturns = totalInvestments
                .total_returns,

            returnPercent = totalInvestments.return_percent,

            allocation = PortfolioAllocationDomain(

                mutualFunds = PortfolioAllocationItemDomain(
                    value = totalInvestments
                        .allocation
                        .mutual_funds
                        .value,

                    percent = totalInvestments
                        .allocation
                        .mutual_funds
                        .percent
                ),

                fixedDeposits = PortfolioAllocationItemDomain(
                    value = totalInvestments
                        .allocation
                        .fixed_deposits
                        .value,

                    percent = totalInvestments
                        .allocation
                        .fixed_deposits
                        .percent
                )
            )
        ),

        investedAmountBreakdown = InvestedAmountBreakdownDomain(
            investedAmount = investedBreakdown
                .invested_amount,

            investedItemsCount = investedBreakdown
                .invested_items_count,

            returnsAmount = investedBreakdown
                .returns_amount,

            returnsPercent = investedBreakdown
                .returns_percent
        ),

        mutualFunds = data.mutual_funds.map {
            it.toDomain()
        },

        fixedDeposits = data.fixed_deposits.map {
            it.toDomain()
        }
    )
}
fun MutualFund.toDomain(): MutualFundPortfolioDomain {
    return MutualFundPortfolioDomain(
        id = id,
        title = title,
        category = category,
        amount = amount,
        currentValue = current_value,
        returnAmount = `return`,
        returnPercentage = return_percentage,
        folio = folio,
        icon = img_url.orEmpty(),
        minSipAmount = transaction_rules.min_sip_amount.toLongOrNull() ?: 0L,
        minLumpSumAmount = transaction_rules.min_lump_sum_amount.toLongOrNull() ?: 0L,
        schemeId=scheme_id,
        balanceUnits=bal_units
    )
}
fun FdTransactionDto.toDomain(): FixedDepositPortfolioDomain {
    return FixedDepositPortfolioDomain(
        id = id,
        amount = amount.toString(),
        roiAtBooking = roi.toString(),
        tenureAtBooking = tenure_days,
        fdIssuedAt = start_date,
        status = status,
        maturityAmount = maturity_amount.toString(),
        userId = "",
        userFullName = "",
        userEmail = "",
        issuerLogoUrl = issuer_logo,
        issuerDisplayName = title,
        maturityDate = maturity_date
    )
}
