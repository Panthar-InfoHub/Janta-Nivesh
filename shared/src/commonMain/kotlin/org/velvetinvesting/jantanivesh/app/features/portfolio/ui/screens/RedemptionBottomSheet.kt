package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.subHeadingMedium
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.ShadowlessTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedemptionBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    schemeId: Int,
    folioNo: String,
    selectedRedemptionType: RedemptionType,
    selectedInputType: RedemptionInputType,
    redemptionUnits: String,
    redemptionAmount: String,
    maxUnits: Double,
    maxAmount: Double,
    onRedemptionTypeChange: (RedemptionType) -> Unit,
    onInputTypeChange: (RedemptionInputType) -> Unit,
    onUnitsChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean
) {

    val unitsValue = redemptionUnits.toDoubleOrNull()
    val amountValue = redemptionAmount.toDoubleOrNull()

    val unitsError =
        selectedInputType == RedemptionInputType.UNITS &&
                (unitsValue == null || unitsValue <= 0 || unitsValue > maxUnits)

    val amountError =
        selectedInputType == RedemptionInputType.AMOUNT &&
                (amountValue == null || amountValue <= 0 || amountValue > maxAmount)

    val isValid = when {
        selectedRedemptionType == RedemptionType.FULL -> true

        selectedInputType == RedemptionInputType.UNITS ->
            unitsValue != null &&
                    unitsValue > 0 &&
                    unitsValue <= maxUnits

        selectedInputType == RedemptionInputType.AMOUNT ->
            amountValue != null &&
                    amountValue > 0 &&
                    amountValue <= maxAmount

        else -> false
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Redeem Mutual Fund",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Text(
                    text = "Redemption Type",
                    style = subHeadingMedium,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ToggleChip(
                        text = "Full",
                        selected = selectedRedemptionType == RedemptionType.FULL,
                        onClick = { onRedemptionTypeChange(RedemptionType.FULL) },
                        modifier = Modifier.weight(1f)
                    )

                    ToggleChip(
                        text = "Partial",
                        selected = selectedRedemptionType == RedemptionType.PARTIAL,
                        onClick = { onRedemptionTypeChange(RedemptionType.PARTIAL) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (selectedRedemptionType == RedemptionType.PARTIAL) {

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    Text(
                        text = "Redeem By",
                        style = subHeadingMedium,
                        color = Color.Black
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ToggleChip(
                            text = "Units",
                            selected = selectedInputType == RedemptionInputType.UNITS,
                            onClick = { onInputTypeChange(RedemptionInputType.UNITS) },
                            modifier = Modifier.weight(1f)
                        )

                        ToggleChip(
                            text = "Amount",
                            selected = selectedInputType == RedemptionInputType.AMOUNT,
                            onClick = { onInputTypeChange(RedemptionInputType.AMOUNT) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                when (selectedInputType) {

                    RedemptionInputType.UNITS -> {

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

                            ShadowlessTextField(
                                value = redemptionUnits,
                                onValueChange = onUnitsChange,
                                placeHolder = "Enter units",
                                label = "Units"
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                if (unitsError) {
                                    Text(
                                        text = when {
                                            unitsValue == null || unitsValue <= 0 ->
                                                "Enter valid units"

                                            unitsValue > maxUnits ->
                                                "Units cannot exceed $maxUnits"

                                            else -> ""
                                        },
                                        color = Color.Red,
                                        style = titlesStyle
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Max Units: $maxUnits",
                                    style = titlesStyle,
                                    color = titleColor,
                                    modifier = Modifier
                                )
                            }
                        }
                    }

                    RedemptionInputType.AMOUNT -> {

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

                            ShadowlessTextField(
                                value = redemptionAmount,
                                onValueChange = onAmountChange,
                                placeHolder = "Enter amount",
                                label = "Amount"
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                if (amountError) {
                                    Text(
                                        text = when {
                                            amountValue == null || amountValue <= 0 ->
                                                "Enter valid amount"

                                            amountValue > maxAmount ->
                                                "Amount exceeds max limit"

                                            else -> ""
                                        },
                                        color = Color.Red,
                                        style = titlesStyle
                                    )
                                }

                                Spacer(Modifier.weight(1f))

                                Text(
                                    text = "Max Amount: ₹${formatMoneyAfterL(maxAmount.toLong())}".withInterRupee(),
                                    style = titlesStyle,
                                    color = titleColor
                                )
                            }
                        }
                    }
                }
            }

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Continue",
                onClick = onSubmit,
                loading = loading,
                enabled = isValid
            )
        }
    }
}

enum class RedemptionType {
    FULL,
    PARTIAL
}

enum class RedemptionInputType {
    UNITS,
    AMOUNT
}

@Composable
fun ToggleChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) Secondary else Color.White)
            .border(
                width = 1.dp,
                color = if (selected) Secondary else titleColor.copy(0.2f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = titlesStyle.copy(fontWeight = FontWeight.Medium),
            color = if (selected) Color.White else Primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun RedemptionBottomSheetPreview() {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    RedemptionBottomSheet(
        sheetState = sheetState,
        onDismiss = {},
        schemeId = 19998,
        folioNo = "91078494263",
        selectedRedemptionType = RedemptionType.PARTIAL,
        selectedInputType = RedemptionInputType.AMOUNT,
        redemptionUnits = "1000",
        redemptionAmount = "8700",
        maxUnits = 245.75,
        maxAmount = 215000.0,
        onRedemptionTypeChange = {},
        onInputTypeChange = {},
        onUnitsChange = {},
        onAmountChange = {},
        onSubmit = {},
        loading = false
    )
}