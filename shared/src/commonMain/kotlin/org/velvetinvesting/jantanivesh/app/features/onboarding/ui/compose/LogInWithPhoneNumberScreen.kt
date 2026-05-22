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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.jantanivesh_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.LoginWithPhoneNumberEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.LoginWithPhoneNumberEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.LoginWithPhoneNumberUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.LoginWithPhoneNumberViewModel
import org.velvetinvesting.jantanivesh.app.theme.Black
import org.velvetinvesting.jantanivesh.app.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.TextFieldBorder

@Composable
fun LoginWithPhoneNumberRoute(
    onNavigateToOtp: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginWithPhoneNumberViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Listen to one-time effects from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginWithPhoneNumberEffect.NavigateToOtpScreen -> onNavigateToOtp()
                LoginWithPhoneNumberEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    // Render the stateless screen
    LoginWithPhoneNumberScreen(
        state = uiState,
        onEvent = viewModel::handleEvent,
        modifier = modifier
    )
}
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
                .padding(horizontal = 24.dp, vertical = Spacing.dp16)
        ) {

            // Top Section (Header & Inputs)
            Column(modifier = Modifier.weight(1f)) {
                TopAppBarWithBackButtonAndStepCount(
                    stepCount = 1,
                    totalSteps = 5,
                    onBack = { onEvent(LoginWithPhoneNumberEvent.OnBackClicked) }
                )

                Text(
                    text = "Log in with your mobile number/ अपने मोबाइल नंबर से लॉग इन करें",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Spacing.dp16)
                )

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8), modifier = Modifier.padding(bottom = 24.dp)) {
                    Text(
                        text = "We'll send a 4 digit OTP to verify your identity",
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "हम आपकी पहचान सत्यापित करने के लिए 4 अंकों का OTP भेजेंगे",
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { onEvent(LoginWithPhoneNumberEvent.OnPhoneNumberChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = LocalShapes.current.roundedDp12,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BoxBorder,
                        focusedBorderColor = TextFieldBorder
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = Spacing.dp16)
                        ) {
                            Text(
                                text = "+91",
                                style = MaterialTheme.typography.labelLarge,
                                color = Black,
                                modifier = Modifier.padding(end = Spacing.dp8)
                            )
                            VerticalDivider(
                                modifier = Modifier.height(24.dp).padding(horizontal = Spacing.dp4),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )

                AppButton(
                    text = "Verify",
                    onClick = { onEvent(LoginWithPhoneNumberEvent.OnVerifyClicked) },
                    modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
                )
            }

            // Bottom Section (Logo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.dp16),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.jantanivesh_logo),
                    contentDescription = "Janta Nivesh Logo",
                    modifier = Modifier.height(58.dp)
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