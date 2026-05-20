package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.jantanivesh_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.features.core.composables.ScreenWideButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.LoginWithPhoneNumberViewModel
import org.velvetinvesting.jantanivesh.app.theme.Black
import org.velvetinvesting.jantanivesh.app.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.Primary
import org.velvetinvesting.jantanivesh.app.theme.TextFieldBorder

@Preview
@Composable
private fun LoginWithPhoneNumberScreen(
    // viewModel: LoginWithPhoneNumberViewModel
) {
    // val state by viewModel.uiState.collectAsState()
    var phoneNumber by remember { mutableStateOf("") } // Temporary state for preview

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            // Top Section (Header & Inputs)
            Column(modifier = Modifier.weight(1f)) {
                TopAppBarWithBackButtonAndStepCount(
                    stepCount = 1,
                    totalSteps = 5,
                    onBack = {} // TODO: implement back navigation
                )

                Text(
                    text = "Log in with your mobile number/ अपने मोबाइल नंबर से लॉग इन करें",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 24.dp)) {
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
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = BoxBorder,
                        focusedBorderColor = TextFieldBorder
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Text(
                                text = "+91",
                                style = MaterialTheme.typography.labelLarge,
                                color = Black
                            )
                            VerticalDivider(
                                modifier = Modifier.height(24.dp).padding(horizontal = 4.dp),
                                color = Primary
                            )
                        }
                    }
                )

                ScreenWideButton(
                    buttonText = "Verify",
                    onClick = {}, //TODO implement navigation and viewModel
                    color = Primary,
                    modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
                )

            }

            // Bottom Section (Logo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Assuming you have the logo exported as a vector/image in resources
                Image(
                    painter = painterResource(Res.drawable.jantanivesh_logo),
                    contentDescription = "Janta Nivesh Logo",
                    modifier = Modifier.height(58.dp)
                )
            }
        }
    }
}