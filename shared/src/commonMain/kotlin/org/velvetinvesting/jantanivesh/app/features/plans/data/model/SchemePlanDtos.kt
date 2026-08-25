package org.velvetinvesting.jantanivesh.app.features.plans.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SipThreshold

@Serializable
data class SchemePlanResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: SchemePlanDataDto? = null
)

/**
 * `GET /mf-scheme/{isin}`.
 *
 * The limits arrive flattened — one `<transaction>_<field>` column per value rather than the
 * `thresholds` array the gateway itself returns — and every amount comes back as a string, since
 * some of them (withdrawal multiples of `0.01`) are fractional. Each transaction type is gated by
 * its own `_allowed` flag, which is what decides whether that mode is offered at all.
 */
@Serializable
data class SchemePlanDataDto(
    /**
     * The product id. This — not the ISIN — is what the purchase and SIP endpoints take as
     * `mf_product_id`.
     */
    val id: String? = null,
    val isin: String? = null,
    val scheme_name: String? = null,
    val fund_name: String? = null,
    val gateway: String? = null,
    val plan_type: String? = null,
    val option: String? = null,
    val idcw_option: String? = null,
    val active: Boolean = false,

    val lumpsum_allowed: Boolean = false,
    val lumpsum_amount_min: String? = null,
    val lumpsum_amount_max: String? = null,
    val lumpsum_amount_multiples: String? = null,
    /** Minimum for a top-up into an existing folio, which can differ from the first purchase. */
    val lumpsum_additional_amount_min: String? = null,

    val sip_daily_allowed: Boolean = false,
    val sip_daily_amount_min: String? = null,
    val sip_daily_amount_max: String? = null,
    val sip_daily_amount_multiples: String? = null,
    val sip_daily_installments_min: Int? = null,

    val sip_monthly_allowed: Boolean = false,
    val sip_monthly_amount_min: String? = null,
    val sip_monthly_amount_max: String? = null,
    val sip_monthly_amount_multiples: String? = null,
    val sip_monthly_installments_min: Int? = null,
    val sip_monthly_dates: List<Int> = emptyList()
)

fun SchemePlanResponseDto.toDomain(): SchemePlan? {
    val data = this.data ?: return null
    val isin = data.isin ?: return null

    return SchemePlan(
        id = data.id.orEmpty(),
        isin = isin,
        schemeName = data.scheme_name.orEmpty(),
        fundName = data.fund_name.orEmpty(),
        option = data.option.orEmpty(),
        planType = data.plan_type.orEmpty(),
        isActive = data.active,
        monthlySip = data.monthlySipThreshold(),
        dailySip = data.dailySipThreshold(),
        lumpsum = data.lumpsumThreshold()
    )
}

/** A monthly SIP debits on a fixed day, so its allowed dates are part of the threshold. */
private fun SchemePlanDataDto.monthlySipThreshold(): SipThreshold? {
    if (!sip_monthly_allowed) return null

    return SipThreshold(
        amountMin = sip_monthly_amount_min.toAmountInt(DEFAULT_SIP_MINIMUM),
        amountMax = sip_monthly_amount_max.toAmountLong(),
        amountMultiples = sip_monthly_amount_multiples.toMultiples(),
        installmentsMin = sip_monthly_installments_min ?: 0,
        // An empty list from the server means "no restriction", not "no days available".
        dates = sip_monthly_dates.ifEmpty { SipThreshold.ALL_DEBIT_DAYS }
    )
}

/** A daily SIP has no debit day; the dates list is filled in only so callers need not null-check. */
private fun SchemePlanDataDto.dailySipThreshold(): SipThreshold? {
    if (!sip_daily_allowed) return null

    return SipThreshold(
        amountMin = sip_daily_amount_min.toAmountInt(DEFAULT_SIP_MINIMUM),
        amountMax = sip_daily_amount_max.toAmountLong(),
        amountMultiples = sip_daily_amount_multiples.toMultiples(),
        installmentsMin = sip_daily_installments_min ?: 0,
        dates = SipThreshold.ALL_DEBIT_DAYS
    )
}

private fun SchemePlanDataDto.lumpsumThreshold(): SipThreshold? {
    if (!lumpsum_allowed) return null

    return SipThreshold(
        amountMin = lumpsum_amount_min.toAmountInt(DEFAULT_LUMPSUM_MINIMUM),
        amountMax = lumpsum_amount_max.toAmountLong(),
        amountMultiples = lumpsum_amount_multiples.toMultiples(),
        // A one-time buy has no instalments to require a minimum count of.
        installmentsMin = 0,
        dates = emptyList(),
        additionalAmountMin = lumpsum_additional_amount_min.toAmountInt(0).takeIf { it > 0 }
    )
}

/**
 * Amounts are strings and may carry decimals. Every purchase on this screen is in whole rupees,
 * so a fractional minimum rounds up — rounding down would offer an amount the gateway rejects.
 */
private fun String?.toAmountInt(fallback: Int): Int {
    val parsed = this?.toDoubleOrNull() ?: return fallback
    if (parsed <= 0.0) return fallback

    val whole = parsed.toInt()
    return if (parsed > whole) whole + 1 else whole
}

private fun String?.toAmountLong(): Long =
    this?.toDoubleOrNull()?.toLong()?.takeIf { it > 0 } ?: Long.MAX_VALUE

/**
 * Multiples below one rupee (the `0.01` the withdrawal limits use) mean "no whole-rupee step" as
 * far as this screen is concerned, so they collapse to 1 rather than to 0, which would disable
 * the check entirely.
 */
private fun String?.toMultiples(): Int {
    val parsed = this?.toDoubleOrNull() ?: return 1
    return parsed.toInt().coerceAtLeast(1)
}

/** Only used when the server omits a limit it declared as allowed. */
private const val DEFAULT_SIP_MINIMUM = 100
private const val DEFAULT_LUMPSUM_MINIMUM = 500
