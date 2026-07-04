package org.velvetinvesting.jantanivesh.app.features.portfolio.data.mapper

import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.getmf.MutualFundDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.fdportfoliobyid.FDPortFolioById
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.pendingorders.PendingOrderDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio.FdTransaction
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio.FolioFundDataDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio.MutualFundPortfolioDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio.UserPortFolioDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FDStatus
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositTransactionDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FolioFundDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.InvestedAmountBreakdownDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingAction
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingOrderDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioAllocationDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioAllocationItemDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDashboardDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.TotalInvestmentsDomain
import kotlin.text.category
import kotlin.text.toInt
import kotlin.text.toLong

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

fun MutualFundPortfolioDto.toDomain(): MutualFundPortfolioDomain {
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

fun FdTransaction.toDomain(): FixedDepositPortfolioDomain {
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

fun FDPortFolioById.toDomain(): FixedDepositTransactionDomain {
    val data = this.data
    return FixedDepositTransactionDomain(
        id = data.id,
        userId = data.user_id,
        paymentCompletedAt = data.payment_completed_at,
        isVkycPending = data.is_vkyc_pending,
        amount = data.amount,
        roiAtBooking = data.roi_at_booking,
        tenureAtBooking = data.tenure_at_booking,
        payoutFrequency = data.payout_frequency,
        status = FDStatus.fromValue(data.status),
        maturityAmount = data.maturity_amount,
        maturityDate = data.maturity_date,
        maturityInstruction = data.maturity_instruction,
        paymentTxId = data.payment_tx_id,
        fdAccountNumber = data.fd_account_number,
        onboardedAt = data.onboarded_at,
        vkycCompletedAt = data.vkyc_completed_at,
        fdIssuedAt = data.fd_issued_at,
        refundDate = data.refund_date,
        vkycFailureReason = data.vkyc_failure_reason,
        failureReason = data.failure_reason,
        createdAt = data.createdAt,
        updatedAt = data.updatedAt,
        productId = data.product.id,
        issuerId = data.product.issuer.id,
        issuerFullName = data.product.issuer.full_name,
        issuerDisplayName = data.product.issuer.display_name,
        issuerType = data.product.issuer.issuer_type,
        issuerLogoUrl = data.product.issuer.logo_url,
        issuerBannerUrl = data.product.issuer.banner_url,
        issuerRatingText = data.product.issuer.rating_text,
        pendingAction = PendingAction.fromValue(data.pending_action)
    )
}

fun PendingOrderDto.toDomain(): PendingOrderDomain {
    return PendingOrderDomain(
        id = id ?: "",
        type = type ?: "",
        schemeName = scheme_name ?: "",
        amount = amount ?: 0.0,
        date = if (type == "SIP") {
            DateTimeUtils.formatDate(date ?: "")
        } else {
            date ?: ""
        },
        status = status ?: "",
        statusRemark = status_remark ?: "",
        amc = amc ?: "",
        frequency = frequency ?: "",
        startDate = start_date ?: "",
        icon = img_url ?: "",
    )
}

fun FolioFundDataDto.toDomain(): FolioFundDomain {
    return FolioFundDomain(
        id = id,
        title = title,
        category = category,
        amount = amount.toLong(),
        isSip = is_sip,
        startDate = start_date,
        returnPercentage = return_percentage,
        `return` = `return`,
        xirr = xirr,
        currentNav = current_nav,
        avgNav = avg_nav,
        folio = folio,
        balanceUnits = balance_units,
        imgUrl = img_url,
        schemeId = scheme_id,
        orderId = order_id?:"",
        actualFolio=actual_folio?:""
    )
}
