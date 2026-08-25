package org.velvetinvesting.jantanivesh.app.features.plans.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan

@Serializable
data class VerifyOtpBody(
    val otp: String
)

@Serializable
data class CreatePurchasePlanBody(
    val scheme: String,
    val amount: Int,
    val frequency: String,
    val installment_day: Int,
    /** Blank for a fresh purchase; an existing folio number tops that folio up instead. */
    val folio_number: String
)

/**
 * `POST /mf-purchase-plan/` keyed on the product id. A monthly SIP debits on a fixed day, so it
 * carries `installment_day`; the daily body omits the field entirely rather than sending null,
 * which the shared Json config would serialise (`explicitNulls` is on).
 */
@Serializable
data class CreateMonthlySipPlanBody(
    val mf_product_id: String,
    val amount: Int,
    val frequency: String,
    val installment_day: Int,
    /** Blank for a fresh purchase; an existing folio number tops that folio up instead. */
    val folio_number: String
)

/** The daily variant: no debit day, and the gateway assigns the folio. */
@Serializable
data class CreateDailySipPlanBody(
    val mf_product_id: String,
    val amount: Int,
    val frequency: String
)

/**
 * `POST /mf-purchase-plan/` returns the gateway object directly rather than the stored record,
 * so `amount` arrives as a number here while the stored form returns it as a string. That one
 * field is why this cannot share [PurchasePlanDataDto].
 */
@Serializable
data class CreatePurchasePlanResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: CreatedPurchasePlanDataDto? = null
)

@Serializable
data class CreatedPurchasePlanDataDto(
    val id: String? = null,
    val state: String? = null,
    val scheme: String? = null,
    val folio_number: String? = null,
    val amount: Double? = null,
    val frequency: String? = null,
    val installment_day: Int? = null,
    val number_of_installments: Int? = null,
    val remaining_installments: Int? = null,
    val start_date: String? = null
)

fun CreatePurchasePlanResponseDto.toDomain(): PurchasePlan? {
    val data = this.data ?: return null
    val id = data.id ?: return null

    return PurchasePlan(
        id = id,
        state = data.state.orEmpty(),
        scheme = data.scheme.orEmpty(),
        folioNumber = data.folio_number,
        // Trailing ".0" would show up in the UI, so whole rupees are rendered as integers.
        amount = data.amount?.let { amount ->
            if (amount % 1.0 == 0.0) amount.toLong().toString() else amount.toString()
        }.orEmpty(),
        frequency = data.frequency.orEmpty(),
        installmentDay = data.installment_day,
        numberOfInstallments = data.number_of_installments,
        remainingInstallments = data.remaining_installments,
        startDate = data.start_date
    )
}

@Serializable
data class PurchasePlanResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: PurchasePlanDataDto? = null
)

/**
 * The confirm response echoes the whole gateway object; unknown keys are ignored by the shared
 * Json config, so only the fields the app uses are declared.
 */
@Serializable
data class PurchasePlanDataDto(
    val id: String? = null,
    /** Gateway id (`mfpp_…`) on a fetched plan. */
    val fp_plan_id: String? = null,
    /** Gateway id (`mfpp_…`) on a listed or confirmed plan. */
    val fp_purchase_plan_id: String? = null,
    val state: String? = null,
    val scheme: String? = null,
    val folio_number: String? = null,
    val amount: String? = null,
    val frequency: String? = null,
    val installment_day: Int? = null,
    val number_of_installments: Int? = null,
    val remaining_installments: Int? = null,
    val start_date: String? = null,
    val systematic: Boolean = false
)

/** `GET /mf-purchase-plan/` — the list form, which nests the array one level deeper. */
@Serializable
data class PurchasePlanListResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: PurchasePlanListDataDto? = null
)

@Serializable
data class PurchasePlanListDataDto(
    val purchase_plans: List<PurchasePlanDataDto> = emptyList()
)

fun PurchasePlanResponseDto.toDomain(): PurchasePlan? = data?.toDomain()

fun PurchasePlanListResponseDto.toDomain(): List<PurchasePlan> =
    data?.purchase_plans.orEmpty().mapNotNull { it.toDomain() }

fun PurchasePlanDataDto.toDomain(): PurchasePlan? {
    // The stored record carries two ids: its own database key and the gateway's. Only the
    // gateway id is accepted by the confirm endpoints, so that is what the domain exposes.
    val id = fp_plan_id ?: fp_purchase_plan_id ?: this.id ?: return null

    return PurchasePlan(
        id = id,
        state = state.orEmpty(),
        scheme = scheme.orEmpty(),
        folioNumber = folio_number,
        amount = amount.orEmpty(),
        frequency = frequency.orEmpty(),
        installmentDay = installment_day,
        numberOfInstallments = number_of_installments,
        remainingInstallments = remaining_installments,
        startDate = start_date
    )
}
