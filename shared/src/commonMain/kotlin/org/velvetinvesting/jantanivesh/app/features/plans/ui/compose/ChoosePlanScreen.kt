package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ListWheelPicker
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.ChoosePlanEvent
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.ChoosePlanUiState

@Composable
fun ChoosePlanScreen(
    state: ChoosePlanUiState,
    handleEvent: (ChoosePlanEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.showDebitDayPicker) {
        ListWheelPicker(
            title = "Select debit day",
            items = state.debitDays,
            selectedItem = state.debitDay,
            onItemSelected = { handleEvent(ChoosePlanEvent.OnDebitDaySelected(it)) },
            onDismiss = { handleEvent(ChoosePlanEvent.OnDebitDayPickerDismiss) }
        )
    }

    if (state.showOtpSheet) {
        ConfirmWithOtpSheet(
            state = state,
            handleEvent = handleEvent
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = Spacing.dp20)
            .clearFocusOnTap()
            .imePadding()
    ) {
        // The funds drive every other input on this screen, so nothing is shown until they land.
        if (state.isLoadingSchemes) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
            contentPadding = PaddingValues(top = Spacing.dp24, bottom = Spacing.dp16)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        text = "Apna plan chuniye /\nअपना प्लान चुनिए",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black
                    )
                    Text(
                        text = "Monthly SIP. Cancel any time, no penalty. / " +
                                "मासिक SIP  कभी भी बंद करें, कोई पेनल्टी नहीं",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreyText
                    )
                }
            }

            items(state.schemes.size) { index ->
                val scheme = state.schemes[index]
                SchemeCard(
                    scheme = scheme,
                    isSelected = scheme.isin == state.selectedScheme?.isin,
                    onClick = { handleEvent(ChoosePlanEvent.OnSchemeSelected(scheme)) }
                )
            }

            // Both inputs are bounded by the chosen fund's thresholds, so they only make sense
            // once one is picked.
            if (state.selectedScheme != null) {
                item {
                    TitledAppTextField(
                        title = "Amount you want to invest (min ₹${state.minimumAmount})",
                        value = state.amount,
                        onValueChange = { handleEvent(ChoosePlanEvent.OnAmountChange(it)) },
                        placeholder = state.minimumAmount.toString(),
                        mandatory = true,
                        keyboardType = KeyboardType.Number,
                        prefix = {
                            Text("₹ ", style = MaterialTheme.typography.titleMedium, color = Black)
                        },
                        isError = state.amount.isNotEmpty() && !state.isAmountValid
                    )
                }

                item {
                    DebitDayField(
                        day = state.debitDay,
                        onClick = { handleEvent(ChoosePlanEvent.OnDebitDayClick) }
                    )
                }
            }

            item {
                Text(
                    text = "Mutual fund investments are subject to market risks. Read all scheme " +
                            "related documents carefully.",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = GreyText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.dp16)
                )
            }
        }

        AppButton(
            // While submitting, the button names the step in flight — the review poll alone can
            // run about twenty seconds, and a bare spinner reads as a hang.
            text = state.submissionStage?.message
                ?: if (state.monthlyAmount > 0) {
                    "Start SIP- ₹${state.monthlyAmount}/month"
                } else {
                    "Start SIP"
                },
            onClick = { handleEvent(ChoosePlanEvent.OnStartSipClick) },
            loading = state.isSubmitting,
            enabled = state.canSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.dp16)
                .genericDropShadow()
        )
    }
}

/** One of the offered funds. Selection is single-choice, so the card doubles as a radio row. */
@Composable
private fun SchemeCard(
    scheme: SchemePlan,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .border(
                width = if (isSelected) Spacing.dp2 else Spacing.dp1,
                color = if (isSelected) Secondary else Color.Transparent,
                shape = RoundedCornerShape(Spacing.dp16)
            )
            .clickable(onClick = onClick)
            .padding(Spacing.dp20),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            Text(
                text = scheme.schemeName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Secondary
            )
            Text(
                text = scheme.fundName,
                style = MaterialTheme.typography.labelSmall,
                color = GreyText
            )
            scheme.monthlySip?.let { sip ->
                Text(
                    text = "Min ₹${sip.amountMin} / month · ${sip.installmentsMin} installments min",
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray444
                )
            }
        }

        RadioDot(isSelected = isSelected)
    }
}

@Composable
private fun RadioDot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(Spacing.dp24)
            .clip(CircleShape)
            .border(
                width = Spacing.dp2,
                color = if (isSelected) Secondary else BoxBorder,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp12)
                    .clip(CircleShape)
                    .background(Secondary)
            )
        }
    }
}

/** Single debit-day control: tapping it opens the wheel picker. */
@Composable
private fun DebitDayField(
    day: Int?,
    onClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
        Text(
            text = "Debit day (1-28)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
            color = Gray444
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Spacing.dp16))
                .border(
                    width = Spacing.dp1,
                    color = BoxBorder,
                    shape = RoundedCornerShape(Spacing.dp16)
                )
                .background(White)
                .clickable(onClick = onClick)
                .padding(horizontal = Spacing.dp16, vertical = Spacing.dp16),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = day?.toString() ?: "Select a day",
                style = MaterialTheme.typography.titleMedium,
                color = if (day == null) GreyText else Black
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
private fun ChoosePlanScreenPreview() {
    JantaNiveshTheme {
        ChoosePlanScreen(
            state = ChoosePlanUiState(debitDay = 5, amount = "2500"),
            handleEvent = {}
        )
    }
}
