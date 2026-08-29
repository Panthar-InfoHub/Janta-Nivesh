package org.velvetinvesting.jantanivesh.app.features.auth.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.jantanivesh_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.EnterPinEvent
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.EnterPinUiState
import org.velvetinvesting.jantanivesh.app.features.core.biometric.rememberBiometricAuthenticator
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.OtpInputField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState

@Composable
fun EnterPinScreen(
    state: EnterPinUiState,
    onEvent: (EnterPinEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val biometricAuthenticator = rememberBiometricAuthenticator()
    val currentOnEvent by rememberUpdatedState(onEvent)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(state.biometricPromptRequested) {
        if (!state.biometricPromptRequested) return@LaunchedEffect
        currentOnEvent(EnterPinEvent.OnBiometricPromptShown)
        if (biometricAuthenticator.isBiometricAvailable()) {
            biometricAuthenticator.authenticate(
                title = "Authentication Required",
                subtitle = "Authenticate to access Janta Nivesh",
                description = "Use your biometrics to continue",
                onSuccess = { currentOnEvent(EnterPinEvent.OnBiometricSuccess) },
                onError = { message -> currentOnEvent(EnterPinEvent.OnBiometricError(message)) }
            )
        }
    }

    LazyColumn (
        modifier = modifier
            .fillMaxSize()
            .clearFocusOnTap()
            .padding(horizontal = Spacing.dp24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp36),
        contentPadding = PaddingValues(top= Spacing.dp64)
    ) {

        item{
            Image(
                painter = painterResource(Res.drawable.jantanivesh_logo),
                contentDescription = null,
                modifier = Modifier.size(width = 180.dp, height = 60.dp)
            )
        }


        item{
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text = if (state.userName.isBlank()) "Hi" else "Hi, ${state.userName}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = state.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }


        item{
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
            ) {
                OtpInputField(
                    otpValue = state.otp.otpValue,
                    onValueChange = { onEvent(EnterPinEvent.OnPinChanged(it)) },
                    focusRequester = focusRequester,
                    otpLength = state.otp.otpLength,
                    shape = RoundedCornerShape(Spacing.dp12),
                    modifier = Modifier
                )
                if (state.verifying) {
                    CircularProgressIndicator(modifier = Modifier.size(Spacing.dp24))
                }
                state.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


        if (state.biometricsAllowed) item {
            Text(
                text = "Use Biometrics",
                color = Secondary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.clickable {
                    onEvent(EnterPinEvent.OnUseBiometricsClicked)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EnterPinScreenPreview() {
    JantaNiveshTheme {
        EnterPinScreen(
            state = EnterPinUiState(
                userName = "Ankit Bose",
                otp = OtpUiState(otpValue = "12", otpLength = 4),
                subtitle = "Enter your Janta Nivesh PIN"
            ),
            onEvent = {}
        )
    }
}
