package org.velvetinvesting.jantanivesh.app.features.login.ui.compose

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
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.enter_code
import jantanivesh.shared.generated.resources.janta_nivesh_logo_desc
import jantanivesh.shared.generated.resources.jantanivesh_logo
import jantanivesh.shared.generated.resources.next
import jantanivesh.shared.generated.resources.otp_input_desc
import jantanivesh.shared.generated.resources.resend_code_timer
import jantanivesh.shared.generated.resources.verification_code_sent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.TextFieldBorder
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpEvent
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpUiState

@Composable
fun EnterOtpScreen(
    state: EnterOtpUiState,
    onEvent: (EnterOtpEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.dp24, vertical = Spacing.dp16)
        ) {

            Column(modifier = Modifier.weight(1f)) {
                AppBackButton(onClick = { onEvent(EnterOtpEvent.OnBackClicked) })

                Text(
                    text = "Enter the Code/"+stringResource(Res.string.enter_code),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Spacing.dp12)
                )

                Text(
                    text ="A verification code has been sent to/"+ stringResource(Res.string.verification_code_sent),
                    color = GreyText,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = "+91 "+state.phoneNumber,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = Spacing.dp24, top = Spacing.dp4)
                )

                OtpInputField(
                    otpValue = state.otpValue,
                    onValueChange = { onEvent(EnterOtpEvent.OnOtpChanged(it)) },
                    focusRequester = focusRequester,
                    modifier = Modifier.padding(bottom = Spacing.dp24)
                )

                Text(
                    text = "You can resend the code in ${state.resendTimerSeconds} seconds/"+stringResource(Res.string.resend_code_timer, state.resendTimerSeconds),
                    color = GreyText,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(bottom = Spacing.dp24)
                        .clickable {
                            if (state.resendTimerSeconds == 0) {
                                onEvent(EnterOtpEvent.OnResendClicked)
                            }
                        }
                )

                AppButton(
                    text = stringResource(Res.string.next),
                    onClick = { onEvent(EnterOtpEvent.OnNextClicked) },
                    enabled = state.isNextEnabled,
                    loading = state.isLoading,
                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp40)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.dp16),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.jantanivesh_logo),
                    contentDescription = stringResource(Res.string.janta_nivesh_logo_desc),
                    modifier = Modifier.height(Spacing.dp58)
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
            .fillMaxWidth()
            .testTag("otp_input_row"),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
    ) {
        repeat(5) { index ->
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

@Preview(showBackground = true, locale = "hi")
@Composable
fun EnterOtpScreenPreview() {
    JantaNiveshTheme {
        EnterOtpScreen(
            state = EnterOtpUiState(
                otpValue = "123",
                phoneNumber = "9876543210",
                resendTimerSeconds = 15
            ),
            onEvent = {}
        )
    }
}