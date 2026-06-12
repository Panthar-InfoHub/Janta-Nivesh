package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.usercart.LumpSumItem
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.usercart.SipItem
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.usercart.UserCartDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CartType
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.LumpSumItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SipDetails
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.UserCartDomain

fun UserCartDto.toDomain(): UserCartDomain {
    return UserCartDomain(
        sipItems = data.sip_items.map { it.toSipDomain() },
        lumpSumItems = data.lump_sum_items.map { it.toLumpSumDomain() }
    )
}

fun SipItem.toSipDomain(): SipItemDomain {
    return SipItemDomain(
        id = id,
        inv_id=inv_id,
        amcName = amc_name,
        amcCode = amc_code,
        productName = prod_name,
        amount = sip_amt.toLongOrNull() ?: 0,
        type = CartType.SIP,
        date = sip_st_date,
        sipDetails = SipDetails(
            startDate = sip_st_date,
            endDate = sip_en_date,
            frequency = sip_freq,
            day = sip_day.toIntOrNull() ?: 0,
            sipAmount = sip_amt.toLongOrNull() ?: 0
        ),
        imageUrl = img_url?:"",
        prodCode= prod_code,
        folio = folio,
        minStepUpAmount = min_step_up_amt?: 0,
        minStepUpPercent = min_step_up_percent,
    )
}

fun LumpSumItem.toLumpSumDomain(): LumpSumItemDomain {
    return LumpSumItemDomain(
        id = id,
        amcName = amc_name,
        amcCode = amc_code,
        productName = prod_name,
        amount = txn_amount.toLongOrNull() ?: 0,
        type = CartType.LUMPSUM,
        date = adddate,
        imageUrl = img_url?:"",
        inv_id = inv_id,
        prodCode = prod_code,
        folio = folio
    )
}