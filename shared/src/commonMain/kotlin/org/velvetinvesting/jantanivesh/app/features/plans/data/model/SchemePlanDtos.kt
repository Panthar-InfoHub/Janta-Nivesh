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

@Serializable
data class SchemePlanDataDto(
    val `object`: String? = null,
    val gateway: String? = null,
    val mf_scheme: NamedDto? = null,
    val mf_fund: NamedDto? = null,
    val isin: String? = null,
    val type: String? = null,
    val option: String? = null,
    val idcw_option: String? = null,
    val active: Boolean = false,
    val thresholds: List<ThresholdDto> = emptyList()
)

@Serializable
data class NamedDto(
    val name: String? = null
)

/**
 * One entry of the gateway's `thresholds` array. The array mixes several transaction types with
 * different field sets, so everything past `type` is optional.
 */
@Serializable
data class ThresholdDto(
    val type: String? = null,
    val frequency: String? = null,
    val amount_min: Double? = null,
    val amount_max: Double? = null,
    val amount_multiples: Double? = null,
    val installments_min: Int? = null,
    val dates: List<Int> = emptyList()
)

fun SchemePlanResponseDto.toDomain(): SchemePlan? {
    val data = this.data ?: return null
    val isin = data.isin ?: return null

    return SchemePlan(
        isin = isin,
        schemeName = data.mf_scheme?.name.orEmpty(),
        fundName = data.mf_fund?.name.orEmpty(),
        option = data.option.orEmpty(),
        monthlySip = data.thresholds
            .firstOrNull { it.type == THRESHOLD_SIP && it.frequency == FREQUENCY_MONTHLY }
            ?.toSipThreshold()
    )
}

private fun ThresholdDto.toSipThreshold() = SipThreshold(
    amountMin = amount_min?.toInt() ?: 0,
    amountMax = amount_max?.toLong() ?: Long.MAX_VALUE,
    amountMultiples = amount_multiples?.toInt() ?: 1,
    installmentsMin = installments_min ?: 0,
    // An empty list from the gateway means "no restriction", not "no days available".
    dates = dates.ifEmpty { SipThreshold.ALL_DEBIT_DAYS }
)

private const val THRESHOLD_SIP = "sip"
private const val FREQUENCY_MONTHLY = "monthly"
