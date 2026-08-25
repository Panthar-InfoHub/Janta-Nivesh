package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.check_circle_outline_icon
import jantanivesh.shared.generated.resources.wallet_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBoxDivider
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MandateOption

/**
 * Picks the autopay mandate the SIP debits against.
 *
 * The rows name mandates, not bank accounts: a user can hold several mandates with different
 * limits, and it is the mandate the gateway attaches the SIP to. Only approved mandates are
 * listed — anything still with the bank cannot carry a debit, so offering it would only produce
 * a rejection later.
 *
 * There is no select-mandate call yet, so choosing one is a local change: the row and the
 * payment card update, and nothing is sent until that API exists.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMandateSheet(
    mandates: List<MandateOption>,
    selectedMandateId: String?,
    isLoading: Boolean,
    onMandateSelected: (String) -> Unit,
    onAddMandateClick: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
        shape = RoundedCornerShape(topStart = Spacing.dp24, topEnd = Spacing.dp24)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Spacing.dp20,
                    end = Spacing.dp20,
                    bottom = Spacing.dp32
                ),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
        ) {
            SheetHeader()

            when {
                isLoading && mandates.isEmpty() -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Spacing.dp72),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }

                mandates.isEmpty() -> NoMandatesState()

                else -> Column {
                    mandates.forEachIndexed { index, mandate ->
                        MandateRow(
                            mandate = mandate,
                            selected = mandate.id == selectedMandateId,
                            onClick = { onMandateSelected(mandate.id) }
                        )
                        if (index != mandates.lastIndex) {
                            HorizontalDivider(
                                thickness = Spacing.dp1,
                                color = GreyBoxDivider
                            )
                        }
                    }
                }
            }

            if (mandates.isEmpty() && !isLoading) {
                AppButton(
                    text = "Set up autopay",
                    onClick = onAddMandateClick,
                    style = AppButtonDefaults.style(height = Spacing.dp58),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                AppButton(
                    text = "Confirm",
                    onClick = onConfirm,
                    enabled = selectedMandateId != null,
                    style = AppButtonDefaults.style(height = Spacing.dp58),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Add another mandate",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = onAddMandateClick,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                )
            }
        }
    }
}

@Composable
private fun SheetHeader() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Spacing.dp36)
                .clip(CircleShape)
                .background(SelectedBoxColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.check_circle_outline_icon),
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(Spacing.dp20)
            )
        }
        Text(
            text = "Select mandate",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Black
        )
    }
}

/** Shown when the user holds no approved mandate — the only way forward is to set one up. */
@Composable
private fun NoMandatesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.dp12),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        Text(
            text = "No approved mandate yet",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Black
        )
        Text(
            text = "A SIP debits through a UPI autopay mandate. Set one up once and your " +
                    "instalments run on their own.",
            style = MaterialTheme.typography.bodyMedium,
            color = GreyText,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MandateRow(
    mandate: MandateOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = Spacing.dp16),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Spacing.dp40)
                .clip(RoundedCornerShape(Spacing.dp10))
                .background(if (selected) SelectedBoxColor else GreyBox),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.wallet_icon),
                contentDescription = null,
                tint = if (selected) Secondary else Primary,
                modifier = Modifier.size(Spacing.dp20)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = mandate.displayName,
                style = MaterialTheme.typography.bodyLarge,
                color = Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = mandate.subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = GreyText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        RadioDot(selected = selected)
    }
}

/** Single-choice marker, drawn rather than themed so it matches the rest of the sheet. */
@Composable
private fun RadioDot(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(Spacing.dp24)
            .clip(CircleShape)
            .border(
                width = Spacing.dp2,
                color = if (selected) Secondary else BoxBorder,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp12)
                    .clip(CircleShape)
                    .background(Secondary)
            )
        }
    }
}

/** "Up to ₹1,00,000 per debit · Cybrillapoa" — the limit is what tells two mandates apart. */
internal val MandateOption.subtitle: String
    get() = listOfNotNull(
        limit?.let { "Up to ₹${it.toInt().withThousandSeparators()} per debit" },
        providerName.takeIf { it.isNotBlank() }?.lowercase()?.replaceFirstChar { it.uppercase() }
    ).joinToString(" · ").ifBlank { "Autopay mandate" }

@Preview(showBackground = true)
@Composable
private fun MandateRowPreview() {
    JantaNiveshTheme {
        Column(modifier = Modifier.background(White).padding(Spacing.dp20)) {
            MandateRow(
                mandate = MandateOption(
                    id = "32",
                    mandateType = "UPI",
                    providerName = "CYBRILLAPOA",
                    status = MandateOption.APPROVED,
                    limit = 100_000,
                    umrn = "HDFC0000123456",
                    startDate = "2026-08-07T00:00:00.000Z"
                ),
                selected = true,
                onClick = {}
            )
            MandateRow(
                mandate = MandateOption(
                    id = "33",
                    mandateType = "UPI",
                    providerName = "CYBRILLAPOA",
                    status = MandateOption.APPROVED,
                    limit = 25_000,
                    umrn = null,
                    startDate = null
                ),
                selected = false,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoMandatesStatePreview() {
    JantaNiveshTheme {
        Column(modifier = Modifier.background(White).padding(Spacing.dp20)) {
            SheetHeader()
            NoMandatesState()
        }
    }
}
