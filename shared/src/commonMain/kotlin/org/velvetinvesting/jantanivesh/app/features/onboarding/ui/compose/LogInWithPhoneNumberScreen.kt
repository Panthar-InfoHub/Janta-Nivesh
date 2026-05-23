package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import jantanivesh.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.LoginWithPhoneNumberEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.LoginWithPhoneNumberEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.LoginWithPhoneNumberUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.LoginWithPhoneNumberViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.TextFieldBorder

@Composable
fun LoginWithPhoneNumberScreen(
    state: LoginWithPhoneNumberUiState,
    onEvent: (LoginWithPhoneNumberEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.dp24, vertical = Spacing.dp16)
        ) {

            Column(modifier = Modifier.weight(1f)) {
                TopAppBarWithBackButtonAndStepCount(
                    stepCount = 1,
                    totalSteps = 5,
                    onBack = { onEvent(LoginWithPhoneNumberEvent.OnBackClicked) }
                )

                Text(
                    text = stringResource(Res.string.login_mobile_number) + stringResource(Res.string.login_mobile_number_translated),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Spacing.dp16)
                )

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8), modifier = Modifier.padding(bottom = Spacing.dp24)) {
                    Text(
                        text = stringResource(Res.string.otp_verify_identity),
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(Res.string.otp_verify_identity_translated),
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                AppTextField(
                    value = state.phoneNumber,
                    onValueChange = { onEvent(LoginWithPhoneNumberEvent.OnPhoneNumberChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = Spacing.dp16)
                        ) {
                            Text(
                                text = stringResource(Res.string.india_code),
                                style = MaterialTheme.typography.labelLarge,
                                color = Black,
                                modifier = Modifier.padding(end = Spacing.dp8)
                            )
                            VerticalDivider(
                                modifier = Modifier.height(Spacing.dp24).padding(horizontal = Spacing.dp4),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )

                AppButton(
                    text = stringResource(Res.string.verify),
                    onClick = { onEvent(LoginWithPhoneNumberEvent.OnVerifyClicked) },
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

@Preview(showBackground = true)
@Composable
fun LoginWithPhoneNumberPreview() {
    JantaNiveshTheme {
        LoginWithPhoneNumberScreen(
            state = LoginWithPhoneNumberUiState(phoneNumber = "9876543210"),
            onEvent = {}
        )
    }
}