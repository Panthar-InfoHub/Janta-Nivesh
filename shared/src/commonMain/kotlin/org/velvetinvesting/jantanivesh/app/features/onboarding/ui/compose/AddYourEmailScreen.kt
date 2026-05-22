package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jantanivesh.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextFieldDefaults
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.TextFieldBorder

@Composable
fun AddYourEmailScreen(
    state: AddYourEmailUiState,
    onEvent: (AddYourEmailEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = Spacing.dp16)
        ) {

            Column(modifier = Modifier.padding(top = Spacing.dp16, bottom = 24.dp)) {
                Box {
                    TopAppBarWithBackButtonAndStepCount(
                        stepCount = 5,
                        totalSteps = 5,
                        onBack = { onEvent(AddYourEmailEvent.OnBackClicked) }
                    )
                    Text(
                        stringResource(Res.string.skip),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-16).dp, y = 17.dp)
                            .clickable { onEvent(AddYourEmailEvent.OnSkipClicked) }
                            .padding(Spacing.dp4)
                    )
                }

                Text(
                    text = stringResource(Res.string.email_add_text) + stringResource(Res.string.email_add_text_translated),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Spacing.dp16)
                )

                Column(modifier = Modifier.padding(bottom = 24.dp)) {
                    Text(
                        text = stringResource(Res.string.transaction_updates),
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(Spacing.dp16))
                    Text(
                        text = stringResource(Res.string.transaction_updates_translated),
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

                Spacer(modifier = Modifier.height(32.dp))

                AppButton(
                    text = stringResource(Res.string.verify),
                    onClick = { onEvent(AddYourEmailEvent.OnVerifyClicked) },
                    modifier = Modifier.fillMaxWidth()
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
                    modifier = Modifier.height(58.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddYourEmailScreenPreview() {
    JantaNiveshTheme{
        AddYourEmailScreen(
            state = AddYourEmailUiState(email = "test@example.com"),
            onEvent = {}
        )
    }
}