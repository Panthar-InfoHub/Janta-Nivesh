package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.email_add_text
import jantanivesh.shared.generated.resources.email_placeholder
import jantanivesh.shared.generated.resources.janta_nivesh_logo_desc
import jantanivesh.shared.generated.resources.jantanivesh_logo
import jantanivesh.shared.generated.resources.skip
import jantanivesh.shared.generated.resources.transaction_updates
import jantanivesh.shared.generated.resources.verify
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
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
                .padding(paddingValues)
                .padding(horizontal = Spacing.dp24)
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Box {
                    TopAppBarWithBackButtonAndStepCount(
                        stepCount = 3,
                        totalSteps = 3,
                        onBack = { onEvent(AddYourEmailEvent.OnBackClicked) }
                    )
                    Text(
                        stringResource(Res.string.skip),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = -Spacing.dp16, y = -Spacing.dp8)
                            .clickable { onEvent(AddYourEmailEvent.OnSkipClicked) }
                    )
                }

                Text(
                    text = "Add your email for updates/" +
                            stringResource(Res.string.email_add_text),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Spacing.dp16)
                )
            Column(
                modifier = Modifier.padding(bottom = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                Text(
                    text = "We’ll send transaction updates here",
                    color = GreyText,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(Res.string.transaction_updates),
                    color = GreyText,
                    style = MaterialTheme.typography.titleMedium
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
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp40)
            )
        }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.dp16),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.jantanivesh_logo),
                    contentDescription = stringResource(Res.string.janta_nivesh_logo_desc),
                    modifier = Modifier.height(Spacing.dp58)
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "te")
@Composable
fun AddYourEmailScreenPreview() {
    JantaNiveshTheme {
        AddYourEmailScreen(
            state = AddYourEmailUiState(email = "test@example.com"),
            onEvent = {}
        )
    }
}