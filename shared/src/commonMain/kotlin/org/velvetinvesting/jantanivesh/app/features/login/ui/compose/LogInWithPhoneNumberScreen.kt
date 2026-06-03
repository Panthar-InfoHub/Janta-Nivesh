package org.velvetinvesting.jantanivesh.app.features.login.ui.compose

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.india_code
import jantanivesh.shared.generated.resources.janta_nivesh_logo_desc
import jantanivesh.shared.generated.resources.jantanivesh_logo
import jantanivesh.shared.generated.resources.login_mobile_number
import jantanivesh.shared.generated.resources.otp_verify_identity
import jantanivesh.shared.generated.resources.verify
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.JantaNiveshAndVelvetLogo
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberEvent
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberUiState

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
                AppBackButton(onClick = { onEvent(LoginWithPhoneNumberEvent.OnBackClicked) })

                Text(
                    text = "Log in with your mobile number/" + stringResource(Res.string.login_mobile_number),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = Spacing.dp16)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
                    modifier = Modifier.padding(bottom = Spacing.dp24)
                ) {
                    Text(
                        text = "We'll send a 4 digit OTP to verify your identity. / "+ stringResource(Res.string.otp_verify_identity),
                        color = GreyText,
                        style = MaterialTheme.typography.titleSmall
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
                                modifier = Modifier.height(Spacing.dp24)
                                    .padding(horizontal = Spacing.dp4),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )

                AppButton(
                    text = stringResource(Res.string.verify),
                    onClick = { onEvent(LoginWithPhoneNumberEvent.OnVerifyClicked) },
                    enabled = state.isNextEnabled,
                    loading = state.isLoading,
                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp40)
                )
            }

            JantaNiveshAndVelvetLogo()

        }
    }
}

@Preview(showBackground = true, locale = "te")
@Composable
fun LoginWithPhoneNumberPreview() {
    JantaNiveshTheme {
        LoginWithPhoneNumberScreen(
            state = LoginWithPhoneNumberUiState(phoneNumber = "9876543210"),
            onEvent = {}
        )
    }
}