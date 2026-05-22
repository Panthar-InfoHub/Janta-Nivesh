package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.dob_dropdown_icon
import jantanivesh.shared.generated.resources.jantanivesh_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.ScreenWideButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBViewModel
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.TextFieldBorder

// --- ROUTE ---
@Composable
fun EnterYourDOBRoute(
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EnterYourDOBViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Listen for navigation effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                EnterYourDOBEffect.NavigateToNextScreen -> onNavigateNext()
                EnterYourDOBEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    // Render the stateless screen
    EnterYourDOBScreen(
        state = uiState,
        onEvent = viewModel::handleEvent,
        modifier = modifier
    )
}

// --- STATELESS SCREEN ---
@Composable
fun EnterYourDOBScreen(
    state: EnterYourDOBUiState,
    onEvent: (EnterYourDOBEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Interaction source to handle clicks on the entire TextField area
    val interactionSource = remember { MutableInteractionSource() }

    // If state says to show the date picker, render it here!
    if (state.showDatePicker) {
        // TODO: Implement your Material3 DatePickerDialog here.
        // Call onEvent(EnterYourDOBEvent.OnDobSelected(date)) when they confirm.
        // Call onEvent(EnterYourDOBEvent.OnDatePickerDismissed) if they dismiss it.
    }

    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = Spacing.dp16)
        ) {

            // Top Section (Header, Texts & Inputs)
            Column(modifier = Modifier.weight(1f)) {

                TopAppBarWithBackButtonAndStepCount(
                    stepCount = 4,
                    totalSteps = 5,
                    onBack = { onEvent(EnterYourDOBEvent.OnBackClicked) }
                )

                Text(
                    text = "Enter your date of birth/\nआपका जन्म तारीख प्रवेश करे",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Spacing.dp16)
                )

                Column(modifier = Modifier.padding(bottom = 24.dp)) {
                    Text(
                        text = "Please provide your date of birth for identity verification.",
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(Spacing.dp16))
                    Text(
                        text = "पहचान सत्यापन के लिए कृपया अपनी जन्मतिथि प्रदान करें",
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Date of Birth Input Field
                OutlinedTextField(
                    value = state.dob,
                    onValueChange = { /* Read only, handled by DatePicker */ },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onEvent(EnterYourDOBEvent.OnDobFieldClicked)
                        },
                    shape = LocalShapes.current.roundedDp12,
                    placeholder = {
                        Text(
                            text = "Select your DOB",
                            style = MaterialTheme.typography.labelMedium,
                            color = GreyText
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.dob_dropdown_icon),
                            contentDescription = "Select Date",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = TextFieldBorder,
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    interactionSource = interactionSource
                )

                Spacer(modifier = Modifier.height(32.dp))

                ScreenWideButton(
                    buttonText = "Verify",
                    onClick = { onEvent(EnterYourDOBEvent.OnVerifyClicked) },
                    color = MaterialTheme.colorScheme.primary,
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

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun EnterYourDOBScreenPreview() {
    JantaNiveshTheme {
        EnterYourDOBScreen(
            state = EnterYourDOBUiState(dob = "15/08/1947"),
            onEvent = {}
        )
    }
}