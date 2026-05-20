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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.jantanivesh_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.features.core.composables.ScreenWideButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailViewModel
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.Primary
import org.velvetinvesting.jantanivesh.app.theme.TextFieldBorder

// --- ROUTE ---
@Composable
fun AddYourEmailRoute(
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddYourEmailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Listen for navigation effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddYourEmailEffect.NavigateToNextScreen -> onNavigateNext()
                AddYourEmailEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    // Render the stateless screen
    AddYourEmailScreen(
        state = uiState,
        onEvent = viewModel::handleEvent,
        modifier = modifier
    )
}

// --- STATELESS SCREEN ---
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
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            // Top Section (Header, Texts & Inputs)
            Column(modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)) {
                Box {
                    TopAppBarWithBackButtonAndStepCount(
                        stepCount = 5,
                        totalSteps = 5,
                        onBack = { onEvent(AddYourEmailEvent.OnBackClicked) }
                    )
                    Text(
                        "Skip",
                        style = MaterialTheme.typography.labelMedium,
                        color = Primary,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-16).dp, y = 17.dp)
                            .clickable { onEvent(AddYourEmailEvent.OnSkipClicked) }
                            .padding(4.dp)
                    )
                }

                Text(
                    text = "Add your email for updates/\nअपडेट पाने के लिए अपना ईमेल पता जोड़ें",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Column(modifier = Modifier.padding(bottom = 24.dp)) {
                    Text(
                        text = "We’ll send transaction updates here",
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "हम यहां लेनदेन संबंधी अपडेट भेजेंगे",
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Email Input Field
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { onEvent(AddYourEmailEvent.OnEmailChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            text = "@gmail.com",
                            style = MaterialTheme.typography.labelMedium,
                            color = GreyText
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = TextFieldBorder,
                        focusedBorderColor = Primary
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                ScreenWideButton(
                    buttonText = "Verify",
                    onClick = { onEvent(AddYourEmailEvent.OnVerifyClicked) },
                    color = Primary,
                    modifier = Modifier.fillMaxWidth()
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
fun AddYourEmailScreenPreview() {
    AddYourEmailScreen(
        state = AddYourEmailUiState(email = "test@example.com"),
        onEvent = {}
    )
}