package org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem

import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode

/**
 * The `investment_mode` filter of `GET /mf/funds`: only funds that can be bought as a SIP of that
 * frequency. Unlike the `amount_type` chips it sets no minimum installment, which is what a bundle
 * slot needs — any fund the mode allows, not only the ₹10/₹100 starters.
 *
 * A lumpsum has no [id]: every fund can be bought outright, so nothing is sent.
 */
enum class InvestmentModeFilter(val id: String?) {
    DAILY_SIP("sip_daily"),
    MONTHLY_SIP("sip"),
    LUMPSUM(null);

    companion object {
        fun from(mode: PurchaseMode): InvestmentModeFilter = when (mode) {
            PurchaseMode.DAILY -> DAILY_SIP
            PurchaseMode.MONTHLY -> MONTHLY_SIP
            PurchaseMode.ONE_TIME -> LUMPSUM
        }
    }
}
