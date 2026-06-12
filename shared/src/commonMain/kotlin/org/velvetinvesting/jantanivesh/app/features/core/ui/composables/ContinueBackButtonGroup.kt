package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme

@Composable
fun ContinueBackButtonFooter(
    continueText: String = "Continue",
    backText: String = "Back",
    onContinue: () -> Unit,
    onBack: () -> Unit,
    pv: PaddingValues,
    enabled: Boolean = true,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .imePadding()
            .shadow(elevation = 28.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 16.dp + pv.calculateBottomPadding()
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InvertedAppButton(
                modifier = Modifier.weight(1f),
                onClick = onBack,
                text = backText
            )
            AppButton(
                modifier = Modifier.weight(1f),
                onClick = onContinue,
                text = continueText,
                enabled = enabled
            )
        }

    }
}

@Preview
@Composable
fun ContinueBackButtonFooterPreview() {
    JantaNiveshTheme {
        ContinueBackButtonFooter(
            onContinue = {},
            onBack = {},
            pv = PaddingValues(0.dp)
        )
    }
}
