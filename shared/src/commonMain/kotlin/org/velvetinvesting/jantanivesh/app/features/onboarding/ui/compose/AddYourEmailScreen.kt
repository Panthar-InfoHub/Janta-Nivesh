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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.email_add_text
import jantanivesh.shared.generated.resources.email_placeholder
import jantanivesh.shared.generated.resources.transaction_updates
import jantanivesh.shared.generated.resources.verify
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailUiState

@Composable
fun AddYourEmailScreen(
    state: AddYourEmailUiState,
    onEvent: (AddYourEmailEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.dp24)
        ) {
            Text(
                text = "Add your email for updates/" +
                        stringResource(Res.string.email_add_text),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = Spacing.dp16)
            )
            Column(
                modifier = Modifier.padding(bottom = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                Text(
                    text = "We’ll send transaction updates here/" + stringResource(Res.string.transaction_updates),
                    color = GreyText,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            AppTextField(
                value = state.email,
                onValueChange = { onEvent(AddYourEmailEvent.OnEmailChanged(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(Res.string.email_placeholder),
                        style = MaterialTheme.typography.labelMedium,
                        color = GreyText
                    )
                }
            )
            AppButton(
                text = stringResource(Res.string.verify),
                onClick = { onEvent(AddYourEmailEvent.OnVerifyClicked) },
                enabled = state.isNextEnabled,
                loading = state.isLoading,
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp40)
            )
        }
    }
}

@Preview(showBackground = true, locale = "hi")
@Composable
fun AddYourEmailScreenPreview() {
    JantaNiveshTheme {
        AddYourEmailScreen(
            state = AddYourEmailUiState(email = "test@example.com"),
            onEvent = {}
        )
    }
}