package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.back_arrow
import jantanivesh.shared.generated.resources.dob_dropdown_icon
import jantanivesh.shared.generated.resources.dropdown_icon
import jantanivesh.shared.generated.resources.ic_graph
import jantanivesh.shared.generated.resources.icon_arrow_right
import jantanivesh.shared.generated.resources.wallet_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MandateOption
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchaseMode
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SipThreshold
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.FundPurchaseEvent
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.FundPurchaseUiState
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.KEYPAD_BACKSPACE

/**
 * One screen for all three ways of buying a fund. The mode tabs change what the middle of the
 * screen asks for — a monthly SIP needs a debit day, the other two do not — while the amount
 * keypad and the confirm button stay put at the bottom, so the primary action never moves.
 */
@Composable
fun FundPurchaseScreen(
    state: FundPurchaseUiState,
    handleEvent: (FundPurchaseEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.showDayPicker) {
        PurchaseDayPickerSheet(
            selectedDay = state.installmentDay,
            allowedDays = state.debitDays,
            onDaySelected = { handleEvent(FundPurchaseEvent.OnDaySelected(it)) },
            onDismiss = { handleEvent(FundPurchaseEvent.OnDayPickerDismiss) }
        )
    }

    if (state.showMandateSheet) {
        SelectMandateSheet(
            mandates = state.mandates,
            selectedMandateId = state.selectedMandateId,
            isLoading = state.isLoadingMandates,
            onMandateSelected = { handleEvent(FundPurchaseEvent.OnMandateSelected(it)) },
            onAddMandateClick = { handleEvent(FundPurchaseEvent.OnAddMandateClick) },
            onConfirm = { handleEvent(FundPurchaseEvent.OnMandateConfirm) },
            onDismiss = { handleEvent(FundPurchaseEvent.OnMandateSheetDismiss) }
        )
    }

    if (state.showOtpSheet) {
        PurchaseOtpSheet(
            state = state,
            handleEvent = handleEvent
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GreyBox)
    ) {
        PurchaseHeader(
            title = state.headerTitle,
            subtitle = state.fundSubtitle,
            modes = state.availableModes,
            selectedMode = state.mode,
            onModeSelected = { handleEvent(FundPurchaseEvent.OnModeSelected(it)) },
            onBackClick = onBackClick
        )

        when {
            state.isLoadingScheme -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }

            state.loadError != null -> ErrorScreen(
                state.loadError,
                onRetryClick = { handleEvent(FundPurchaseEvent.OnRetryLoad) }
            )

            else -> PurchaseForm(
                state = state,
                handleEvent = handleEvent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PurchaseForm(
    state: FundPurchaseUiState,
    handleEvent: (FundPurchaseEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.dp16, vertical = Spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            AmountCard(
                amount = state.amount,
                suggestedAmounts = state.suggestedAmounts,
                onSuggestedAmountClick = {
                    handleEvent(FundPurchaseEvent.OnSuggestedAmountClick(it))
                }
            )

            if (state.mode.needsInstallmentDay) {
                DebitDayCard(
                    day = state.installmentDay,
                    expectedNavDate = state.expectedNavDate.toShortDateLabel(),
                    onClick = { handleEvent(FundPurchaseEvent.OnDayFieldClick) }
                )
            }

            // A one-time buy is settled then and there, so there is no mandate to debit.
            if (state.mode.isSip) {
                MandateCard(
                    mandate = state.selectedMandate,
                    onClick = { handleEvent(FundPurchaseEvent.OnMandateFieldClick) }
                )
            }

            state.amountError?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = Spacing.dp4)
                )
            }
        }

        AmountKeypad(
            onKeyPress = { handleEvent(FundPurchaseEvent.OnKeypadPress(it)) },
            modifier = Modifier.padding(horizontal = Spacing.dp16)
        )

        AppButton(
            text = state.submitLabel,
            onClick = { handleEvent(FundPurchaseEvent.OnSubmitClick) },
            loading = state.isSubmitting,
            enabled = state.canSubmit,
            style = AppButtonDefaults.style(height = Spacing.dp58),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Spacing.dp16,
                    end = Spacing.dp16,
                    top = Spacing.dp16,
                    bottom = Spacing.dp16
                )
        )
    }
}

/** Back arrow, fund identity and the mode tabs — the white block pinned above the form. */
@Composable
private fun PurchaseHeader(
    title: String,
    subtitle: String,
    modes: List<PurchaseMode>,
    selectedMode: PurchaseMode,
    onModeSelected: (PurchaseMode) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = Spacing.dp16, vertical = Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp36)
                    .clip(CircleShape)
                    .background(GreyBox)
                    .clickable(
                        onClick = onBackClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.back_arrow),
                    contentDescription = "Back",
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp16)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = GreyText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        PurchaseModeTabs(
            modes = modes,
            selectedMode = selectedMode,
            onModeSelected = onModeSelected
        )
    }
}

/** Segmented control: one filled pill for the active mode, the rest on a plain grey track. */
@Composable
private fun PurchaseModeTabs(
    modes: List<PurchaseMode>,
    selectedMode: PurchaseMode,
    onModeSelected: (PurchaseMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        modes.forEach { mode ->
            val selected = mode == selectedMode

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(LocalShapes.current.circle)
                    .background(if (selected) Primary else GreyBox)
                    .clickable(
                        onClick = { onModeSelected(mode) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mode.label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                    ),
                    color = if (selected) White else Gray444,
                    modifier = Modifier.padding(horizontal = Spacing.dp8, vertical = Spacing.dp8)
                )
            }
        }
    }
}

/** The amount the keypad feeds, with the quick-pick chips derived from the fund's minimum. */
@Composable
private fun AmountCard(
    amount: String,
    suggestedAmounts: List<Int>,
    onSuggestedAmountClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(Spacing.dp16))
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .padding(vertical = Spacing.dp20, horizontal = Spacing.dp16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        Text(
            text = "Investment Amount",
            style = MaterialTheme.typography.bodyMedium,
            color = GreyText
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "₹",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary
            )
            Text(
                // An empty field still reads as an amount rather than as a blank line.
                text = amount.ifEmpty { "0" },
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp
                ),
                color = if (amount.isEmpty()) Secondary else Primary
            )
        }

        AmountChips(
            amounts = suggestedAmounts,
            selectedAmount = amount.toIntOrNull(),
            onClick = onSuggestedAmountClick
        )
    }
}

/**
 * Chips wrap onto a second row rather than scrolling sideways, which is how the four defaults
 * end up as three-then-one on a narrow screen.
 */
@Composable
private fun AmountChips(
    amounts: List<Int>,
    selectedAmount: Int?,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        amounts.chunked(CHIPS_PER_ROW).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                row.forEach { amount ->
                    AmountChip(
                        amount = amount,
                        selected = amount == selectedAmount,
                        onClick = { onClick(amount) }
                    )
                }
            }
        }
    }
}

private const val CHIPS_PER_ROW = 3

@Composable
private fun AmountChip(
    amount: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (selected) SelectedBoxColor else White)
            .border(
                width = Spacing.dp1,
                color = if (selected) Secondary else BoxBorder,
                shape = CircleShape
            )
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(horizontal = Spacing.dp20, vertical = Spacing.dp10),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "₹${amount.withThousandSeparators()}",
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) Secondary else Black
        )
    }
}

/** Selected debit day, with the NAV date it implies shown underneath. */
@Composable
private fun DebitDayCard(
    day: Int?,
    expectedNavDate: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(Spacing.dp16))
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .padding(Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Spacing.dp12))
                .border(
                    width = Spacing.dp1,
                    color = Secondary,
                    shape = RoundedCornerShape(Spacing.dp12)
                )
                .clickable(onClick = onClick)
                .padding(horizontal = Spacing.dp12, vertical = Spacing.dp14),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp32)
                    .clip(RoundedCornerShape(Spacing.dp8))
                    .background(SelectedBoxColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.dob_dropdown_icon),
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(Spacing.dp16)
                )
            }

            Text(
                text = day?.let { "Monthly on ${it.withOrdinalSuffix()}" } ?: "Choose a debit day",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (day == null) GreyText else Primary,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(Res.drawable.dropdown_icon),
                contentDescription = null,
                tint = Gray444,
                modifier = Modifier.size(Spacing.dp16)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = Spacing.dp4)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_graph),
                contentDescription = null,
                tint = GreyText,
                modifier = Modifier.size(Spacing.dp16)
            )
            Text(
                text = "Expected NAV date: $expectedNavDate",
                style = MaterialTheme.typography.labelMedium,
                color = GreyText
            )
        }
    }
}

/** The mandate the SIP will be debited against — a mandate, not a bank account. */
@Composable
private fun MandateCard(
    mandate: MandateOption?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(Spacing.dp16))
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .clickable(onClick = onClick)
            .padding(Spacing.dp16),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Spacing.dp40)
                .clip(RoundedCornerShape(Spacing.dp10))
                .background(SelectedBoxColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.wallet_icon),
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(Spacing.dp20)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Pay via Mandate",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Black
            )
            Text(
                // Naming the mandate and its ceiling is what makes a change visible: there is no
                // select-mandate call yet, so this row is the whole feedback for the choice.
                text = mandate?.let { "${it.displayName} · ${it.subtitle}" }
                    ?: "Set up autopay to start a SIP",
                style = MaterialTheme.typography.labelMedium,
                color = GreyText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier
                .size(Spacing.dp24)
                .clip(CircleShape)
                .background(GreyBox),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_arrow_right),
                contentDescription = null,
                tint = Gray444,
                modifier = Modifier.size(Spacing.dp12)
            )
        }
    }
}

/**
 * The on-screen keypad. The amount field is never focusable, so this is the only way to type an
 * amount — which keeps the system IME from covering the summary the user is checking.
 */
@Composable
private fun AmountKeypad(
    onKeyPress: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top= Spacing.dp4),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        KEYPAD_ROWS.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
            ) {
                row.forEach { key ->
                    KeypadKey(
                        key = key,
                        onClick = { onKeyPress(key) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun KeypadKey(
    key: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isBackspace = key == KEYPAD_BACKSPACE

    Box(
        modifier = modifier
            .height(Spacing.dp44)
            .clip(RoundedCornerShape(Spacing.dp10))
            .background(if (isBackspace) BoxBorder else White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isBackspace) "⌫" else key,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Black
        )
    }
}

private val KEYPAD_ROWS = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9"),
    listOf(".", "0", KEYPAD_BACKSPACE)
)

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun FundPurchaseScreenPreview() {
    JantaNiveshTheme {
        FundPurchaseScreen(
            state = FundPurchaseUiState(
                fundName = "SBI Gold Fund",
                fundSubtitle = "High Risk · Commodities · Gold",
                scheme = previewScheme,
                isLoadingScheme = false,
                installmentDay = 13,
                amount = ""
            ),
            handleEvent = {},
            onBackClick = {}
        )
    }
}

private val previewMonthlySip = SipThreshold(
    amountMin = 500,
    amountMax = 999_999_999,
    amountMultiples = 1,
    installmentsMin = 6,
    dates = SipThreshold.ALL_DEBIT_DAYS
)

private val previewDailySip = SipThreshold(
    amountMin = 30,
    amountMax = 999_999_999,
    amountMultiples = 1,
    installmentsMin = 10,
    dates = SipThreshold.ALL_DEBIT_DAYS
)

private val previewLumpsum = SipThreshold(
    amountMin = 100,
    amountMax = 999_999_999,
    amountMultiples = 1,
    installmentsMin = 0,
    dates = emptyList(),
    additionalAmountMin = 100
)

private val previewScheme = SchemePlan(
    id = "cmt83a057005583ricu8zd443",
    isin = "INF209K01RU9",
    schemeName = "SBI Gold Fund",
    fundName = "SBI Mutual Fund",
    option = "growth",
    planType = "regular",
    monthlySip = previewMonthlySip,
    dailySip = previewDailySip,
    lumpsum = previewLumpsum
)
