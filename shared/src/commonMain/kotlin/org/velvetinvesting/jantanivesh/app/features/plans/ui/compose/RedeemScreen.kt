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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.info_filled_icon
import jantanivesh.shared.generated.resources.monument_icon
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
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.RedeemEvent
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.RedeemHolding
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.RedeemMode
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.RedeemUiState

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

        when {
            state.isLoading -> LoadingHoldings(modifier = Modifier.weight(1f))

            state.isEmpty -> Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp20),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
            ) {
                SelectHoldingBanner()
                EmptyHoldings(modifier = Modifier.weight(1f))
            }

            else -> LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp20),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
                contentPadding = PaddingValues(bottom = Spacing.dp16)
            ) {
                item { SelectHoldingBanner() }

                items(state.holdings.size) { index ->
                    val holding = state.holdings[index]
                    HoldingCard(
                        holding = holding,
                        isSelected = holding.planId == state.selectedHolding?.planId,
                        // A single holding is implicitly the one being redeemed.
                        isSelectable = state.holdings.size > 1,
                        onClick = { handleEvent(RedeemEvent.OnHoldingSelected(holding.planId)) }
                    )
                }

                // Without a folio there is nothing to redeem from, so the controls stay hidden
                // and the reason is stated instead.
                if (state.canRedeemSelected) {
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
                } else {
                    item {
                        InfoBanner(
                            text = "This SIP does not have a folio yet, so there is nothing to " +
                                    "redeem from. A folio is assigned once the first instalment " +
                                    "is processed."
                        )
                    }
                }
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
    val holding = state.selectedHolding ?: return

    when (state.mode) {
        RedeemMode.AMOUNT -> TitledAppTextField(
            title = "Amount to redeem",
            value = state.amountInput,
            onValueChange = { handleEvent(RedeemEvent.OnAmountChange(it)) },
            placeholder = "0",
            mandatory = true,
            keyboardType = KeyboardType.Decimal,
            prefix = {
                Text("₹ ", style = MaterialTheme.typography.titleMedium, color = Black)
            }
        )

        RedeemMode.UNITS -> TitledAppTextField(
            title = "Units to redeem",
            value = state.unitsInput,
            onValueChange = { handleEvent(RedeemEvent.OnUnitsChange(it)) },
            placeholder = "0.000",
            mandatory = true,
            keyboardType = KeyboardType.Decimal,
            // The unit balance is not on this endpoint, so no "available" hint is shown.
            supportingText = holding.availableUnits?.let { units ->
                {
                    Text(
                        text = "Available: $units",
                        style = MaterialTheme.typography.labelSmall,
                        color = GreyText
                    )
                }
            }
        )

        RedeemMode.ALL_UNITS -> InfoBanner(
            text = holding.availableUnits
                ?.let { "Redeeming all $it units from folio ${holding.folioNumber}." }
                ?: "Redeeming all units from folio ${holding.folioNumber}."
        )
    }
}

@Composable
private fun SelectHoldingBanner() {
    InfoBanner(
        text = "Select the holding you want to redeem from. Money goes back to your registered " +
                "bank account only."
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

@Composable
private fun HoldingCard(
    holding: RedeemHolding,
    isSelected: Boolean,
    isSelectable: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .border(
                width = if (isSelected && isSelectable) Spacing.dp2 else Spacing.dp1,
                color = if (isSelected && isSelectable) Secondary else BoxBorder,
                shape = RoundedCornerShape(Spacing.dp12)
            )
            .then(if (isSelectable) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(Spacing.dp20),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        Text(
            text = holding.scheme,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Black
        )

        StatePill(state = holding.state)

        HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)

        // Folio always shows — its absence is the reason redeeming is blocked, so it is worth
        // stating. The remaining rows are drawn only when the endpoint returned them.
        HoldingRow(
            label = "Folio Number",
            value = holding.folioNumber ?: FOLIO_NOT_AVAILABLE
        )
        HoldingRow(label = "Available Units", value = holding.availableUnits)
        HoldingRow(label = "Monthly SIP", value = holding.monthlyAmount)
        HoldingRow(label = "Current Value", value = holding.currentValue)
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

/** Shown in place of a folio the gateway has not assigned yet. */
private const val FOLIO_NOT_AVAILABLE = "Not available"

/** Renders nothing when the endpoint had no value for this field. */
@Composable
private fun HoldingRow(label: String, value: String?) {
    if (value.isNullOrBlank()) return

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

@Composable
private fun LoadingHoldings(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Primary)
    }
}

@Composable
private fun EmptyHoldings(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(Spacing.dp58)
                .clip(RoundedCornerShape(Spacing.dp16))
                .background(GoalIconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.monument_icon),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(Spacing.dp24)
            )
        }
        Text(
            text = "No holdings yet",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Black
        )
        Text(
            text = "Start a SIP and your holdings will show up here once the first instalment " +
                    "is processed.",
            style = MaterialTheme.typography.labelMedium,
            color = GreyText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Spacing.dp24)
        )
    }
}

/** Mirrors what the endpoint really returns today: a plan with no folio assigned yet. */
private val sampleHolding = RedeemHolding(
    planId = "cmsjk25uf0000lsriagsp9kmz",
    scheme = "INF209K01RU9",
    folioNumber = null,
    availableUnits = null,
    currentValue = null,
    monthlyAmount = "₹2500",
    state = "created"
)

@Preview(showBackground = true)
@Composable
private fun RedeemScreenEmptyPreview() {
    JantaNiveshTheme {
        RedeemScreen(
            state = RedeemUiState(),
            handleEvent = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RedeemScreenPopulatedPreview() {
    JantaNiveshTheme {
        RedeemScreen(
            state = RedeemUiState(
                holdings = listOf(sampleHolding.copy(folioNumber = "910123456789")),
                selectedPlanId = sampleHolding.planId,
                mode = RedeemMode.AMOUNT,
                amountInput = "1000"
            ),
            handleEvent = {},
            onBack = {}
        )
    }
}
