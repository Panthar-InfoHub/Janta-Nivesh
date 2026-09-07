package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.info_filled_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBoxDivider
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.RedeemEvent
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.RedeemHolding
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.RedeemMode
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.RedeemUiState

/**
 * How much of one holding to take out.
 *
 * The holding arrives from the order-details screen, so there is nothing to load and nothing to
 * choose between — the screen opens straight on the controls.
 */
@Composable
fun RedeemScreen(
    state: RedeemUiState,
    handleEvent: (RedeemEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .clearFocusOnTap()
            .imePadding()
    ) {
        BackHeader(
            title = "Redeem",
            onBack = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp20)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp20),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
            contentPadding = PaddingValues(bottom = Spacing.dp16)
        ) {
            item {
                InfoBanner(
                    text = "Money goes back to your registered bank account only."
                )
            }

            item { HoldingCard(holding = state.holding) }

            item {
                RedeemByPicker(
                    selected = state.mode,
                    onSelected = { handleEvent(RedeemEvent.OnModeSelected(it)) }
                )
            }

            item {
                RedeemInput(
                    state = state,
                    handleEvent = handleEvent
                )
            }
        }

        AppButton(
            text = "Redeem Now",
            onClick = { handleEvent(RedeemEvent.OnRedeemClick) },
            loading = state.isRedeeming,
            enabled = state.canRedeem,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp20)
                .padding(bottom = Spacing.dp24)
        )
    }
}

/** The typed field for Amount/Units, or the confirmation note when redeeming everything. */
@Composable
private fun RedeemInput(
    state: RedeemUiState,
    handleEvent: (RedeemEvent) -> Unit
) {
    val holding = state.holding
    val error = state.inputError

    when (state.mode) {
        RedeemMode.AMOUNT -> TitledAppTextField(
            title = "Amount to redeem",
            value = state.amountInput,
            onValueChange = { handleEvent(RedeemEvent.OnAmountChange(it)) },
            placeholder = "0",
            mandatory = true,
            keyboardType = KeyboardType.Decimal,
            isError = error != null,
            prefix = {
                Text("₹ ", style = MaterialTheme.typography.titleMedium, color = Black)
            },
            supportingText = {
                FieldHint(
                    error = error,
                    hint = "Available: ₹${formatWithCommas(holding.currentValue.toLong())}"
                )
            }
        )

        RedeemMode.UNITS -> TitledAppTextField(
            title = "Units to redeem",
            value = state.unitsInput,
            onValueChange = { handleEvent(RedeemEvent.OnUnitsChange(it)) },
            placeholder = "0.000",
            mandatory = true,
            keyboardType = KeyboardType.Decimal,
            isError = error != null,
            supportingText = {
                FieldHint(
                    error = error,
                    hint = "Available: ${holding.availableUnits} units"
                )
            }
        )

        RedeemMode.ALL_UNITS -> InfoBanner(
            text = "Redeeming all ${holding.availableUnits} units from folio " +
                    "${holding.folioNumber}."
        )
    }
}

/** The limit while the field is fine, the reason it is not once it isn't. */
@Composable
private fun FieldHint(error: String?, hint: String) {
    Text(
        text = error ?: hint,
        style = MaterialTheme.typography.labelSmall,
        color = if (error != null) appRed else GreyText
    )
}

@Composable
private fun InfoBanner(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(GoalIconBg)
            .padding(Spacing.dp16),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(Res.drawable.info_filled_icon),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(Spacing.dp20)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = Gray444
        )
    }
}

/** What is being redeemed from, restated so the user is not working from memory. */
@Composable
private fun HoldingCard(holding: RedeemHolding) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .border(
                width = Spacing.dp1,
                color = BoxBorder,
                shape = RoundedCornerShape(Spacing.dp12)
            )
            .padding(Spacing.dp20),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        Text(
            text = holding.scheme,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Black
        )

        // The pill now says what kind of holding this is, which is the thing the user cares
        // about here — the gateway's own state has no bearing on redeeming.
        StatePill(state = if (holding.isSip) "SIP" else "LUMPSUM")

        HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)

        HoldingRow(label = "Folio Number", value = holding.folioNumber)
        HoldingRow(label = "Available Units", value = holding.availableUnits.toString())
        HoldingRow(
            label = "Current Value",
            value = "₹${formatWithCommas(holding.currentValue.toLong())}"
        )
    }
}

@Composable
private fun StatePill(state: String) {
    if (state.isBlank()) return

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(Secondary)
            .padding(horizontal = Spacing.dp12, vertical = Spacing.dp4)
    ) {
        Text(
            text = state.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = White
        )
    }
}

@Composable
private fun HoldingRow(label: String, value: String) {
    if (value.isBlank()) return

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Gray444
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Black,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RedeemByPicker(
    selected: RedeemMode,
    onSelected: (RedeemMode) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
        Text(
            text = "Redeem by",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Black
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Spacing.dp8))
                .border(
                    width = Spacing.dp1,
                    color = BoxBorder,
                    shape = RoundedCornerShape(Spacing.dp8)
                )
        ) {
            RedeemMode.entries.forEach { mode ->
                val isSelected = mode == selected
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isSelected) Primary else White)
                        .clickable { onSelected(mode) }
                        .padding(vertical = Spacing.dp16),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isSelected) White else Black
                    )
                }
            }
        }
    }
}

private val sampleHolding = RedeemHolding(
    holdingId = "cmtl875r60000bbri2ce7qpa9",
    scheme = "Aditya Birla Sun Life Gold Fund-Growth",
    folioNumber = "1051586674",
    availableUnits = 2.311,
    currentValue = 100.0,
    isSip = true
)

@Preview(showBackground = true)
@Composable
private fun RedeemScreenPreview() {
    JantaNiveshTheme {
        RedeemScreen(
            state = RedeemUiState(holding = sampleHolding),
            handleEvent = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RedeemScreenByAmountPreview() {
    JantaNiveshTheme {
        RedeemScreen(
            state = RedeemUiState(
                holding = sampleHolding,
                mode = RedeemMode.AMOUNT,
                amountInput = "1000"
            ),
            handleEvent = {},
            onBack = {}
        )
    }
}
