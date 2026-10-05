package org.velvetinvesting.jantanivesh.app.features.portfolio.data.mapper

import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.fdportfoliobyid.FDPortFolioById
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.pendingorders.PendingOrderDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio.FolioFundDataDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FDStatus
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositTransactionDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FolioFundDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingAction
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingOrderDomain

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

