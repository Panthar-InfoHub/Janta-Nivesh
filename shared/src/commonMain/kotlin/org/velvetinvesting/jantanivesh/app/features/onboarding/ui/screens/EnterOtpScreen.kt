package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.jantanivesh_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.features.core.composables.ScreenWideButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.Primary
import org.velvetinvesting.jantanivesh.app.theme.TextFieldBorder

@Preview
@Composable
private fun EnterOtpScreen(modifier: Modifier = Modifier) {
    var otpValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    // Optional: Request focus automatically when the screen opens
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            // Top Section (Header & Inputs)
            Column(modifier = Modifier.weight(1f)) {
                TopAppBarWithBackButtonAndStepCount(
                    stepCount = 2, // Assuming this is step 2
                    totalSteps = 5,
                    onBack = { /* TODO */ }
                )

                Text(
                    text = "Enter the Code",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "A verification code has been sent to",
                    color = GreyText
                )
                Text(
                    text = "+971 1 123 123 1234", // Assuming this will be passed dynamically later
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 24.dp, top = 8.dp)
                )

                // Reusing your provided OtpInputField
                OtpInputField(
                    otpValue = otpValue,
                    onValueChange = { newValue ->
                        if (newValue.length <= 5) { // Enforce max length of 5 as per UI
                            otpValue = newValue
                        }
                    },
                    focusRequester = focusRequester,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Text(
                    text = "You can resend the code in 24 seconds",
                    color = GreyText,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Reusing your provided ScreenWideButton
                ScreenWideButton(
                    buttonText = "Next",
                    onClick = { /* TODO: Verify OTP */ },
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

@Composable
internal fun OtpInputField(
    otpValue: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    // CHANGE 1: Use CircleShape instead of your theme's default textField shape
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

    // Ensure cd_otp_input exists in your string resources
    val otpInputDescription = "Enter Otp through Keyboard"

    BasicTextField(
        value = otpValue,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .focusRequester(focusRequester)
            .testTag("otp_basic_text_field") // Update tags as needed
            .semantics { contentDescription = otpInputDescription },
        decorationBox = {},
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("otp_input_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Or Arrangement.spacedBy as in your original
    ) {
        repeat(5) { index ->
            val char = otpValue.getOrNull(index)
            val isFocused = index == otpValue.length

            Box(
                modifier = Modifier
                    // CHANGE 2: Make it a square for CircleShape to work, e.g., use the default height for both dimensions
                    .size(53.dp)
                    .clip(shape)
                    .border(
                        width = 1.dp,
                        // CHANGE 3: Use the light blue border color as seen in the image for unfocused state
                        color = if (isFocused) TextFieldBorder else BoxBorder,
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