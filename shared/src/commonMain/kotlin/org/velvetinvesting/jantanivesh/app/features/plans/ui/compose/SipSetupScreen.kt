package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.ic_jagged_arrow
import jantanivesh.shared.generated.resources.tac_dialog_cancel
import jantanivesh.shared.generated.resources.try_again
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InvertedAppButton
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.SipSetupStage
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.SipSetupUiState

/**
 * What the user watches between approving their mandate and typing the OTP. There is nothing to
 * do here, so the screen only reports which step is running — and, when a step gives up, offers
 * to run the chain again rather than sending the user back to re-enter the amount.
 */
@Composable
fun SipSetupScreen(
    state: SipSetupUiState,
    schemeName: String,
    onRetryClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = Spacing.dp24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.error == null) {
            MandateLoader()

            Text(
                text = state.stage.message,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = Spacing.dp32)
            )

            Text(
                text = "Please stay on this screen — this can take up to a minute.",
                style = MaterialTheme.typography.titleSmall,
                color = GreyText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = Spacing.dp8)
            )
        } else {
            Text(
                text = schemeName.ifBlank { "Your SIP" },
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = state.error,
                style = MaterialTheme.typography.titleSmall,
                color = GreyText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = Spacing.dp12, bottom = Spacing.dp32)
            )

            AppButton(
                text = "Try Again/ " + stringResource(Res.string.try_again),
                onClick = onRetryClick,
                modifier = Modifier.fillMaxWidth()
            )

            InvertedAppButton(
                text = "Cancel/ " + stringResource(Res.string.tac_dialog_cancel),
                onClick = onCancelClick,
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp12)
            )
        }
    }
}

@Composable
private fun MandateLoader(){
    Box(
        contentAlignment = Alignment.Center
    )
    {

        CircularProgressIndicator(
            modifier = Modifier.size(92.dp).graphicsLayer { scaleX = -1f },
            color = Secondary,
            strokeWidth = 4.dp
        )
        CircularProgressIndicator(
            modifier = Modifier.size(108.dp),
            color = Primary,
            strokeWidth = 4.dp
        )

        Box(
            modifier = Modifier.size(72.dp)
                .clip(CircleShape)
                .background(Primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_jagged_arrow),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SipSetupScreenPreview() {
    JantaNiveshTheme {
        SipSetupScreen(
            state = SipSetupUiState(stage = SipSetupStage.AWAITING_REVIEW),
            schemeName = "SBI Gold Fund",
            onRetryClick = {},
            onCancelClick = {}
        )
    }
}

@Preview(showBackground = true, locale = "hi")
@Composable
private fun SipSetupScreenErrorPreview() {
    JantaNiveshTheme {
        SipSetupScreen(
            state = SipSetupUiState(
                stage = SipSetupStage.CONFIRMING_MANDATE,
                error = "Your autopay mandate has not been approved yet. Please try again in a " +
                        "moment."
            ),
            schemeName = "SBI Gold Fund",
            onRetryClick = {},
            onCancelClick = {}
        )
    }
}
