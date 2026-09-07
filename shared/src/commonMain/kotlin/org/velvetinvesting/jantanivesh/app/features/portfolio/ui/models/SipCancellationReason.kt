package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.models

import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.cancel_reason_amc_support
import jantanivesh.shared.generated.resources.cancel_reason_amount_not_available
import jantanivesh.shared.generated.resources.cancel_reason_customer_support
import jantanivesh.shared.generated.resources.cancel_reason_exit_load
import jantanivesh.shared.generated.resources.cancel_reason_fund_manager_changed
import jantanivesh.shared.generated.resources.cancel_reason_goal_complete
import jantanivesh.shared.generated.resources.cancel_reason_invest_later
import jantanivesh.shared.generated.resources.cancel_reason_mandate_not_ready
import jantanivesh.shared.generated.resources.cancel_reason_returns_not_as_expected
import jantanivesh.shared.generated.resources.cancel_reason_switch_scheme
import org.jetbrains.compose.resources.StringResource

/**
 * The reasons `POST /mf/purchase-plan/{id}/cancel` accepts.
 *
 * [code] is sent verbatim — the gateway rejects anything outside this fixed set — while
 * [labelRes] is what the user reads, so the wire value and its wording stay paired here rather
 * than drifting apart in a lookup somewhere else.
 */
enum class SipCancellationReason(
    val code: String,
    val labelRes: StringResource,
    /** English fallback, in the "English/translated" style the rest of the app uses. */
    val fallbackLabel: String
) {
    AMOUNT_NOT_AVAILABLE(
        "amount_not_available",
        Res.string.cancel_reason_amount_not_available,
        "Funds not available"
    ),
    RETURNS_NOT_AS_EXPECTED(
        "investment_returns_not_as_expected",
        Res.string.cancel_reason_returns_not_as_expected,
        "Returns not as expected"
    ),
    AMC_SUPPORT_NOT_SATISFACTORY(
        "amc_support_not_satisfactory",
        Res.string.cancel_reason_amc_support,
        "AMC support not satisfactory"
    ),
    EXIT_LOAD_NOT_AS_EXPECTED(
        "exit_load_not_as_expected",
        Res.string.cancel_reason_exit_load,
        "Exit load not as expected"
    ),
    SWITCH_TO_OTHER_SCHEME(
        "switch_to_other_scheme",
        Res.string.cancel_reason_switch_scheme,
        "Switching to another scheme"
    ),
    FUND_MANAGER_CHANGED(
        "fund_manager_changed",
        Res.string.cancel_reason_fund_manager_changed,
        "Fund manager changed"
    ),
    INVESTMENT_GOAL_COMPLETE(
        "investment_goal_complete",
        Res.string.cancel_reason_goal_complete,
        "Investment goal achieved"
    ),
    MANDATE_NOT_READY(
        "mandate_not_ready",
        Res.string.cancel_reason_mandate_not_ready,
        "Mandate not ready"
    ),
    INVEST_LATER(
        "invest_later",
        Res.string.cancel_reason_invest_later,
        "Want to invest later"
    ),
    CUSTOMER_SUPPORT_NOT_SATISFACTORY(
        "customer_support_not_satisfactory",
        Res.string.cancel_reason_customer_support,
        "Customer support not satisfactory"
    )
}
