package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.arrowback_icon
import jantanivesh.shared.generated.resources.jantanivesh_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.features.core.composables.ScreenWideButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.Primary
import org.velvetinvesting.jantanivesh.app.theme.TextFieldBorder

@Preview
@Composable
private fun AddYourEmailScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }

    Scaffold(modifier = modifier) { paddingValues ->
        Column(verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            // Custom Top Bar with Skip Button
            Column(modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)) {
                Box {
                    TopAppBarWithBackButtonAndStepCount(
                        stepCount = 5,
                        totalSteps = 5,
                        onBack = {}
                    )
                    Text(
                        "Skip",
                        style = MaterialTheme.typography.labelMedium,
                        color = Primary,
                        modifier = Modifier.align(Alignment.TopEnd).offset(x = (-16).dp, y = 17.dp)
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
                    value = email,
                    onValueChange = { email = it },
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