package org.velvetinvesting.jantanivesh.app.features.portfolio.data.mapper

import org.velvetinvesting.jantanivesh.app.core.utils.isoUtcToDisplayDate
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.purchaseplan.MfPurchasePlanDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.purchaseplan.MfPurchasePlansDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.ActiveSipDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.ActiveSipItemDomain

/**
 * The purchase plans, narrowed to the ones the Active SIP tab is about.
 *
 * A plan only belongs on that tab when it is both `systematic` — a one-off purchase is an order,
 * not a SIP — and still running. A cancelled or completed plan is history, and the tab has no
 * way to say so.
 */
fun MfPurchasePlansDto.toActiveSipDomain(): ActiveSipDomain {
    val plans = data?.purchase_plans.orEmpty()
        .filter { it.systematic && it.isRunning() }
        .map { it.toDomain() }

    val (daily, rest) = plans.partition { it.frequency.equals("daily", ignoreCase = true) }

    return ActiveSipDomain(
        totalInstallmentAmount = plans.sumOf { it.installmentAmount },
        monthlySips = rest,
        dailySips = daily
    )
}

private fun MfPurchasePlanDto.isRunning(): Boolean =
    when (state?.uppercase()) {
        "ACTIVE", "RUNNING", "SUCCESSFUL", "SUBMITTED", "PENDING", "CREATED" -> true
        // An unreported state is treated as running rather than dropped: the plan exists, and
        // hiding it would leave the user with no sign of a debit that is still scheduled.
        null, "" -> true
        else -> false
    }

fun MfPurchasePlanDto.toDomain(): ActiveSipItemDomain = ActiveSipItemDomain(
    id = id,
    fundName = mf_product?.name.orEmpty(),
    fundCategory = mf_product?.scheme_plan?.fund_category.orEmpty().toDisplayCase(),
    fundType = mf_product?.scheme_plan?.sub_category.orEmpty().toDisplayCase(),
    installmentAmount = amount?.toDoubleOrNull() ?: 0.0,
    frequency = frequency.orEmpty().toDisplayCase(),
    nextDueDate = next_installment_date?.takeIf { it.isNotBlank() }?.isoUtcToDisplayDate().orEmpty(),
    folioNumber = folio_number.orEmpty(),
    isin = mf_product?.isin ?: scheme.orEmpty(),
    totalInstallments = number_of_installments,
    remainingInstallments = remaining_installments,
    latestNav = mf_product?.latest_nav?.toDoubleOrNull() ?: 0.0,
    iconUrl = mf_product?.img_url.orEmpty()
)

/** "EQUITY FUND" and "monthly" both arrive from the gateway; the screen wants "Equity Fund". */
private fun String.toDisplayCase(): String = split(' ')
    .filter { it.isNotBlank() }
    .joinToString(" ") { word -> word.lowercase().replaceFirstChar { it.uppercase() } }
