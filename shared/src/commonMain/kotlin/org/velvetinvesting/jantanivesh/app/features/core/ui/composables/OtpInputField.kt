package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.otp_input_desc
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.TextFieldBorder
import org.velvetinvesting.jantanivesh.app.features.core.ui.otp.OtpUiState

@Composable
fun OtpInputField(
    otpValue: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    otpLength: Int = OtpUiState.DEFAULT_OTP_LENGTH,
    shape: androidx.compose.ui.graphics.Shape = CircleShape
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val onBoxClick: () -> Unit = remember {
        {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    val otpTextStyle = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.Medium,
    )

    val otpInputDesc = stringResource(Res.string.otp_input_desc)

    BasicTextField(
        value = otpValue,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .focusRequester(focusRequester)
            .testTag("otp_basic_text_field")
            .semantics { contentDescription = otpInputDesc },
        decorationBox = {},
    )

    Row(
        modifier = modifier
            .testTag("otp_input_row"),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
    ) {
        repeat(otpLength) { index ->
            val char = otpValue.getOrNull(index)

            Box(
                modifier = Modifier
                    .size(Spacing.dp53)
                    .clip(shape)
                    .border(
                        width = Spacing.dp1,
                        color = if (char != null) TextFieldBorder else BoxBorder,
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