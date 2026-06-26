package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.core.utils.decrementSlashDateYearYear
import org.velvetinvesting.jantanivesh.app.core.utils.incrementSlashDateYearYear
import org.velvetinvesting.jantanivesh.app.core.utils.toSlashDate
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.initiatemfpurchase.body.Item
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.initiatemfpurchase.body.MFInitiatePurchaseBody
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SipItemDomain


fun List<SipItemDomain>.toInitiateBodyDto(): MFInitiatePurchaseBody {
    return MFInitiatePurchaseBody(
        items = map { sip ->
            Item(
                amc_code = sip.amcCode,
                prod_code = sip.prodCode,
                sip_freq = sip.sipDetails.frequency,
                sip_amt = sip.sipDetails.sipAmount,
                folio = sip.folio ?: "",
                sip_st_date = sip.sipDetails.startDate.toSlashDate(),
                sip_en_date = sip.sipDetails.endDate.toSlashDate(),
                step_up_required = if (sip.stepUpRequired) "Y" else "N",
                step_up_amount = if (sip.stepUpRequired) sip.stepUpAmount.toString() else "",
                step_up_start_date = if (sip.stepUpRequired) sip.sipDetails.startDate.toSlashDate().incrementSlashDateYearYear() else "",
                step_up_end_date = if (sip.stepUpRequired) sip.sipDetails.endDate.toSlashDate().decrementSlashDateYearYear() else ""
            )
        }
    )
}