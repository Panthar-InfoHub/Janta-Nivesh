package org.velvetinvesting.jantanivesh.app.features.bundles.domain.models

import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode

/**
 * This fund's own minimum for [mode], or null when it can't be bought that way. A fund that came
 * with no limits at all can't be bought any way, as nothing is known to work out a minimum from.
 */
fun FundDomain.minAmountFor(mode: PurchaseMode): Long? {
    val rules = transactionRules ?: return null
    return when (mode) {
        PurchaseMode.DAILY -> rules.minDailySipAmount
        PurchaseMode.MONTHLY -> rules.minMonthlySipAmount
        PurchaseMode.ONE_TIME -> rules.minLumpSumAmount
    }
}

/** Whether this fund can be bought as [mode]. */
fun FundDomain.supports(mode: PurchaseMode): Boolean = minAmountFor(mode) != null

/** How [mode] reads in "not available for …" messages. */
val PurchaseMode.investmentName: String
    get() = when (this) {
        PurchaseMode.DAILY -> "Daily SIP"
        PurchaseMode.MONTHLY -> "Monthly SIP"
        PurchaseMode.ONE_TIME -> "One-time investment"
    }

/** The short note shown on a fund that can't be bought as [mode]. */
fun PurchaseMode.unavailableNote(): String = "Not available for $investmentName"
