package org.velvetinvesting.jantanivesh.app.features.splashscreen.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/** Taps in a row that open the dialog. */
private const val TAPS_TO_REVEAL = 7

/** A pause longer than this starts the count over, so stray taps never add up. */
private val TAP_STREAK_TIMEOUT = 2.seconds

/**
 * The author's easter egg: [content] shows as it always does, and [TAPS_TO_REVEAL] quick taps on it
 * open a small signed note.
 *
 * Kept as a wrapper so the content itself is never changed — whatever is wrapped here gains the
 * trigger, and every other use of the same composable stays exactly as it was. The taps are read
 * with a raw gesture detector rather than `clickable`, so the wrapped content shows no ripple and
 * is not announced as a button.
 */
@Composable
fun SavyEasterEgg(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var tapCount by remember { mutableIntStateOf(0) }
    var lastTap by remember { mutableStateOf<TimeSource.Monotonic.ValueTimeMark?>(null) }

    Box(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    val now = TimeSource.Monotonic.markNow()
                    val inStreak = lastTap?.let { now - it <= TAP_STREAK_TIMEOUT } == true
                    tapCount = if (inStreak) tapCount + 1 else 1
                    lastTap = now

                    if (tapCount >= TAPS_TO_REVEAL) {
                        tapCount = 0
                        lastTap = null
                        showDialog = true
                    }
                }
            )
        }
    ) {
        content()
    }

    if (showDialog) {
        SavyEasterEggDialog(onDismiss = { showDialog = false })
    }
}

@Composable
private fun SavyEasterEggDialog(onDismiss: () -> Unit) {
    // Started hidden and flipped at once, so the card animates in on first composition.
    val visibility = remember { MutableTransitionState(false).apply { targetState = true } }

    Dialog(onDismissRequest = onDismiss) {
        AnimatedVisibility(
            visibleState = visibility,
            enter = fadeIn() + scaleIn(initialScale = 0.85f)
        ) {
            SavyEasterEggCard(onDismiss = onDismiss)
        }
    }
}

@Composable
private fun SavyEasterEggCard(onDismiss: () -> Unit) {
    val shape = RoundedCornerShape(Spacing.dp24)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(White)
    ) {
        // The banner: the codename, framed like a source file.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Primary, Secondary)))
                .padding(vertical = Spacing.dp24, horizontal = Spacing.dp20),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
        ) {
            Text(
                text = "// you found it",
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = White.copy(alpha = 0.75f)
            )
            Text(
                text = "Savy",
                fontSize = 44.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Black,
                color = White
            )
            Text(
                text = "</>",
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = White.copy(alpha = 0.9f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            // The pun, set as a block of code.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Spacing.dp12))
                    .background(SelectedBoxColor)
                    .padding(Spacing.dp16),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
            ) {
                Text(
                    text = "Built with 1000+ problems, but a bug ain't one.",
                    fontSize = 17.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Text(
                    text = "Okay, maybe one.",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    color = Secondary
                )
            }

            Text(
                text = "— S.P.S.",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = GreyText,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(
                text = "exit(0)",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun SavyEasterEggCardPreview() {
    JantaNiveshTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SavyEasterEggCard(onDismiss = {})
        }
    }
}
