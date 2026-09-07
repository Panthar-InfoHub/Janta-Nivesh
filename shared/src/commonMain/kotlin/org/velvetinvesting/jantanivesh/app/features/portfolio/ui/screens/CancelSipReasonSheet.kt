package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.cancel_sip_confirm
import jantanivesh.shared.generated.resources.cancel_sip_keep
import jantanivesh.shared.generated.resources.cancel_sip_subtitle
import jantanivesh.shared.generated.resources.cancel_sip_title
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.models.SipCancellationReason

/**
 * Collects why the user is stopping a SIP, as a sheet rather than a screen: it is one required
 * choice, and keeping it over the order details means backing out costs nothing and leaves the
 * figures they were reading still on screen behind it.
 *
 * Confirming is blocked until a reason is picked — the gateway will not take a cancellation
 * without one of its codes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelSipReasonSheet(
    sheetState: SheetState,
    selectedReason: SipCancellationReason?,
    isSubmitting: Boolean,
    onReasonSelected: (SipCancellationReason) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp20)
                .padding(bottom = Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                Text(
                    text = "Why are you cancelling?/" +
                            stringResource(Res.string.cancel_sip_title),
                    style = MaterialTheme.typography.titleLarge
                        .copy(fontWeight = FontWeight.Bold),
                    color = Black
                )
                Text(
                    text = "Pick a reason to cancel this SIP. This cannot be undone./" +
                            stringResource(Res.string.cancel_sip_subtitle),
                    style = MaterialTheme.typography.labelMedium,
                    color = GreyText
                )
            }

            // Capped rather than free-flowing: the list is long enough to push the confirm
            // button off a short screen otherwise.
            LazyColumn(
                modifier = Modifier.heightIn(max = Spacing.dp285),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
            ) {
                items(SipCancellationReason.entries) { reason ->
                    ReasonRow(
                        reason = reason,
                        isSelected = reason == selectedReason,
                        onClick = { onReasonSelected(reason) }
                    )
                }
            }

            AppButton(
                text = "Cancel SIP/" + stringResource(Res.string.cancel_sip_confirm),
                onClick = onConfirm,
                enabled = selectedReason != null && !isSubmitting,
                loading = isSubmitting,
                style = AppButtonDefaults.style(containerColor = appRed),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Keep it running/" + stringResource(Res.string.cancel_sip_keep),
                style = MaterialTheme.typography.titleMedium,
                color = Secondary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(
                        onClick = onDismiss,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .padding(Spacing.dp8)
            )
        }
    }
}

@Composable
private fun ReasonRow(
    reason: SipCancellationReason,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .border(
                width = if (isSelected) Spacing.dp2 else Spacing.dp1,
                color = if (isSelected) Secondary else BoxBorder,
                shape = RoundedCornerShape(Spacing.dp12)
            )
            .clickable(onClick = onClick)
            .padding(end = Spacing.dp12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = Secondary)
        )
        Text(
            text = reason.fallbackLabel + "/" + stringResource(reason.labelRes),
            style = MaterialTheme.typography.bodyLarge,
            color = Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, locale = "hi")
@Composable
private fun CancelSipReasonSheetPreview() {
    JantaNiveshTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
            SipCancellationReason.entries.take(3).forEach { reason ->
                ReasonRow(
                    reason = reason,
                    isSelected = reason == SipCancellationReason.INVEST_LATER,
                    onClick = {}
                )
            }
        }
    }
}
