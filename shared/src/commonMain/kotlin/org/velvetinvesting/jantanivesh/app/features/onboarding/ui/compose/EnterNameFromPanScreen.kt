package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.jantanivesh_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.features.core.composables.ScreenWideButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterNameFromPanEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterNameFromPanEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterNameFromPanUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterNameFromPanViewModel
import org.velvetinvesting.jantanivesh.app.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.Primary
import org.velvetinvesting.jantanivesh.app.theme.TextFieldBorder

// --- ROUTE ---
@Composable
fun EnterNameFromPanRoute(
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EnterNameFromPanViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Listen for navigation effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                EnterNameFromPanEffect.NavigateToNextScreen -> onNavigateNext()
                EnterNameFromPanEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    // Render the stateless screen
    EnterNameFromPanScreen(
        state = uiState,
        onEvent = viewModel::handleEvent,
        modifier = modifier
    )
}

// --- STATELESS SCREEN ---
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
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            // Top Section (Header, Texts & Inputs)
            Column(modifier = Modifier.weight(1f)) {

                TopAppBarWithBackButtonAndStepCount(
                    stepCount = 3,
                    totalSteps = 5,
                    onBack = { onEvent(EnterNameFromPanEvent.OnBackClicked) }
                )

                Text(
                    text = "Your full name as per PAN/\nपैन कार्ड के अनुसार आपका पूरा नाम",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Column(modifier = Modifier.padding(bottom = 24.dp)) {
                    Text(
                        text = "As it appear on your Pan Card",
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "जैसा कि आपके पैन कार्ड पर दिखाई देता है",
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                OutlinedTextField(
                    value = state.fullName,
                    onValueChange = { onEvent(EnterNameFromPanEvent.OnNameChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            text = "Full Name",
                            style = MaterialTheme.typography.labelMedium,
                            color = GreyText
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BoxBorder,
                        focusedBorderColor = TextFieldBorder
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                ScreenWideButton(
                    buttonText = "Continue",
                    onClick = { onEvent(EnterNameFromPanEvent.OnContinueClicked) },
                    color = Primary,
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
                )
            }

            // Bottom Section (Logo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun EnterNameFromPanScreenPreview() {
    EnterNameFromPanScreen(
        state = EnterNameFromPanUiState(fullName = "Raju Rastogi"),
        onEvent = {}
    )
}