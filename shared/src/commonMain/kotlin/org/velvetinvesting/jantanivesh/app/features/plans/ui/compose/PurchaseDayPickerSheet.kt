package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SipThreshold

/**
 * Debit-day picker, laid out as a month grid so the choice reads like a calendar date rather
 * than a number between 1 and 28.
 *
 * The grid runs the full 31 days because a shorter one would look broken, but only the days the
 * gateway accepts are selectable — everything past the 28th is inert by regulation, and a fund
 * may narrow the list further (some accept only 1, 7, 10, 14, 20, 21 and 28).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseDayPickerSheet(
    selectedDay: Int?,
    allowedDays: List<Int>,
    onDaySelected: (Int) -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Text(
                text = "Choose your debit day",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Black
            )

            DayGrid(
                selectedDay = selectedDay,
                allowedDays = allowedDays,
                onDaySelected = onDaySelected
            )

            Text(
                text = "SIPs debit on the same day every month. Days after the 28th are not " +
                        "available, so every month has the day you pick.",
                style = MaterialTheme.typography.labelMedium,
                color = GreyText
            )
        }
    }
}

@Composable
private fun DayGrid(
    selectedDay: Int?,
    allowedDays: List<Int>,
    onDaySelected: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
        (MIN_DAY..MAX_CALENDAR_DAY).toList()
            .chunked(DAYS_PER_ROW)
            .forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
                ) {
                    week.forEach { day ->
                        DayCell(
                            day = day,
                            selected = day == selectedDay,
                            enabled = day in allowedDays,
                            onClick = { onDaySelected(day) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // The last row is short; blank weights keep the columns aligned.
                    repeat(DAYS_PER_ROW - week.size) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
    }
}

@Composable
private fun DayCell(
    day: Int,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                when {
                    selected -> Secondary
                    !enabled -> White
                    else -> GreyBox
                }
            )
            .clickable(
                enabled = enabled,
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            ),
            color = when {
                selected -> White
                !enabled -> GreyText.copy(alpha = DISABLED_ALPHA)
                else -> Primary
            },
            textAlign = TextAlign.Center
        )
    }
}

private const val MIN_DAY = 1

/** A full month is drawn so the grid does not look truncated, even though 29-31 never enable. */
private const val MAX_CALENDAR_DAY = 31
private const val DAYS_PER_ROW = 7
private const val DISABLED_ALPHA = 0.4f

@Preview(showBackground = true)
@Composable
private fun DayGridPreview() {
    JantaNiveshTheme {
        Column(modifier = Modifier.background(SelectedBoxColor).padding(Spacing.dp20)) {
            DayGrid(
                selectedDay = 13,
                allowedDays = SipThreshold.ALL_DEBIT_DAYS,
                onDaySelected = {}
            )
        }
    }
}
