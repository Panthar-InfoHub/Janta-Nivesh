package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.dob_dropdown_icon
import jantanivesh.shared.generated.resources.jantanivesh_logo // Ensure this exists
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.features.core.composables.ScreenWideButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.Primary
import org.velvetinvesting.jantanivesh.app.theme.TextFieldBorder

@Preview
@Composable
private fun EnterYourDOBScreen(modifier: Modifier = Modifier) {
    var dob by remember { mutableStateOf("") }
    // Interaction source to handle clicks on the entire TextField area if you want to open a DatePicker later
    val interactionSource = remember { MutableInteractionSource() }

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
                    stepCount = 4,
                    totalSteps = 5,
                    onBack = { /* TODO: handle back navigation */ }
                )

                Text(
                    text = "Enter your date of birth/\nआपका जन्म तारीख प्रवेश करे",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Column(modifier = Modifier.padding(bottom = 24.dp)) {
                    Text(
                        text = "Please provide your date of birth for identity verification.",
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "पहचान सत्यापन के लिए कृपया अपनी जन्मतिथि प्रदान करें",
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Date of Birth Input Field
                OutlinedTextField(
                    value = dob,
                    onValueChange = { /* Usually read-only when using a DatePicker */ },
                    readOnly = true, // Set to true if you plan to use a DatePickerDialog
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            /* TODO: Open DatePicker */
                        },
                    shape = RoundedCornerShape(12.dp),
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
                        focusedBorderColor = Primary
                    ),
                    interactionSource = interactionSource
                )

                Spacer(modifier = Modifier.height(32.dp))

                ScreenWideButton(
                    buttonText = "Verify",
                    onClick = { /* TODO: Handle verify action */ },
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
                    modifier = Modifier.height(53.dp)
                )
            }
        }
    }
}