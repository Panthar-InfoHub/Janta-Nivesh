package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.jantanivesh_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterOtpEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterOtpEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterOtpUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterOtpViewModel
import org.velvetinvesting.jantanivesh.app.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.TextFieldBorder

// --- ROUTE ---
@Composable
fun EnterOtpRoute(
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EnterOtpViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Listen to one-time effects from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                EnterOtpEffect.NavigateToNextScreen -> onNavigateNext()
                EnterOtpEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    // Render the stateless screen
    EnterOtpScreen(
        state = uiState,
        onEvent = viewModel::handleEvent,
        modifier = modifier
    )
}

// --- STATELESS SCREEN ---
@Composable
fun EnterOtpScreen(
    state: EnterOtpUiState,
    onEvent: (EnterOtpEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // FocusRequester is UI-specific state, so it stays in the Composable
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                    stepCount = 2,
                    totalSteps = 5,
                    onBack = { onEvent(EnterOtpEvent.OnBackClicked) }
                )

                Text(
                    text = "Enter the Code",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Spacing.dp16)
                )

                Text(
                    text = "A verification code has been sent to",
                    color = GreyText
                )
                Text(
                    text = state.phoneNumber,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W700),
                    modifier = Modifier.padding(bottom = 24.dp, top = Spacing.dp8)
                )

                OtpInputField(
                    otpValue = state.otpValue,
                    onValueChange = { onEvent(EnterOtpEvent.OnOtpChanged(it)) },
                    focusRequester = focusRequester,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Text(
                    text = "You can resend the code in ${state.resendTimerSeconds} seconds",
                    color = GreyText,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .clickable {
                            if (state.resendTimerSeconds == 0) {
                                onEvent(EnterOtpEvent.OnResendClicked)
                            }
                        }
                )

                AppButton(
                    text = "Next",
                    onClick = { onEvent(EnterOtpEvent.OnNextClicked) },
                    modifier = Modifier.fillMaxWidth()
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

// --- SUB-COMPOSABLE ---
@Composable
internal fun OtpInputField(
    otpValue: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val shape = CircleShape

    val onBoxClick: () -> Unit = remember {
        {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    val otpTextStyle = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.Medium,
    )

    BasicTextField(
        value = otpValue,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .focusRequester(focusRequester)
            .testTag("otp_basic_text_field")
            .semantics { contentDescription = "Enter Otp through Keyboard" },
        decorationBox = {},
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("otp_input_row"),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
    ) {
        repeat(5) { index ->
            val char = otpValue.getOrNull(index)
            // Optional: You can keep this if you still want the cursor box to be highlighted too
            val isFocused = index == otpValue.length

            Box(
                modifier = Modifier
                    .size(53.dp)
                    .clip(shape)
                    .border(
                        width = 1.dp,
                        // CHANGE: Color now depends on whether a character is present
                        color = if (char != null || isFocused) TextFieldBorder else BoxBorder,
                        shape = shape,
                    )
                    .clickable(onClick = onBoxClick),
                contentAlignment = Alignment.Center,
            ) {
                if (char != null) {
                    Text(
                        text = char.toString(),
                        style = otpTextStyle,
                        color = GreyText,
                    )
                }
            }
        }
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun EnterOtpScreenPreview() {
    JantaNiveshTheme {
        EnterOtpScreen(
            state = EnterOtpUiState(
                otpValue = "123",
                phoneNumber = "+91 9876543210",
                resendTimerSeconds = 15
            ),
            onEvent = {}
        )
    }
}