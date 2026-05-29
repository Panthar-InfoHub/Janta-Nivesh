package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.as_on_pan
import jantanivesh.shared.generated.resources.continue_text
import jantanivesh.shared.generated.resources.full_name_placeholder
import jantanivesh.shared.generated.resources.pan_full_name
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterNameFromPanEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterNameFromPanUiState

@Composable
fun EnterNameFromPanScreen(
    state: EnterNameFromPanUiState,
    onEvent: (EnterNameFromPanEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.dp24)
        ) {
            Text(
                text = "Your full name as per PAN/" + stringResource(Res.string.pan_full_name),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = Spacing.dp16)
            )

            Column(
                modifier = Modifier.padding(bottom = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
            ) {
                Text(
                    text = "As it appear on your Pan Card/" + stringResource(Res.string.as_on_pan),
                    color = GreyText,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            AppTextField(
                value = state.fullName,
                onValueChange = { onEvent(EnterNameFromPanEvent.OnNameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(Res.string.full_name_placeholder),
                        style = MaterialTheme.typography.labelMedium,
                        color = GreyText
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )
            AppButton(
                text = stringResource(Res.string.continue_text),
                onClick = { onEvent(EnterNameFromPanEvent.OnContinueClicked) },
                enabled = state.isNextEnabled,
                loading = state.isLoading,
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp40)
            )
        }
    }
}

@Preview(showBackground = true, locale = "hi")
@Composable
fun EnterNameFromPanScreenPreview() {
    JantaNiveshTheme {
        EnterNameFromPanScreen(
            state = EnterNameFromPanUiState(fullName = "Raju Rastogi"),
            onEvent = {}
        )
    }
}