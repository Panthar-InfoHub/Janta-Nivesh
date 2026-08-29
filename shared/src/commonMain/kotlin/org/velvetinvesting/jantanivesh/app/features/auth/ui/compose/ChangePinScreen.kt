package org.velvetinvesting.jantanivesh.app.features.auth.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.change_pin_instruction
import jantanivesh.shared.generated.resources.change_pin_screen_title
import jantanivesh.shared.generated.resources.confirm_new_pin
import jantanivesh.shared.generated.resources.hide_pin
import jantanivesh.shared.generated.resources.ic_eye
import jantanivesh.shared.generated.resources.new_pin
import jantanivesh.shared.generated.resources.pin_length_hint
import jantanivesh.shared.generated.resources.pins_do_not_match
import jantanivesh.shared.generated.resources.save_changes
import jantanivesh.shared.generated.resources.show_pin
import jantanivesh.shared.generated.resources.update_your_security_pin
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Gray45
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.profileDividerColor
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.ChangePinEvent
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.ChangePinUiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap

/**
 * Reached only after the user has cleared [org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.EnterPinPurpose.CHANGE_PIN],
 * so the current PIN has already been checked against the server and is not asked for again here.
 */
@Composable
fun ChangePinScreen(
    state: ChangePinUiState,
    onEvent: (ChangePinEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(containerColor = White) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .clearFocusOnTap()
                .padding(horizontal = Spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
            contentPadding = PaddingValues(bottom = Spacing.dp24)
        ) {

            item {
                BackHeader(
                    title = "Change PIN/ " + stringResource(Res.string.change_pin_screen_title),
                    onBack = { onEvent(ChangePinEvent.OnBackClicked) }
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        text = "Update your security PIN/ " +
                            stringResource(Res.string.update_your_security_pin),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Please enter and confirm your new 4-digit PIN./ " +
                            stringResource(Res.string.change_pin_instruction),
                        style = MaterialTheme.typography.bodyMedium,
                        color = GreyText
                    )
                }
            }

            item {
                PinField(
                    title = "New PIN/ " + stringResource(Res.string.new_pin),
                    value = state.newPin,
                    visible = state.newPinVisible,
                    onValueChange = { onEvent(ChangePinEvent.OnNewPinChanged(it)) },
                    onToggleVisibility = { onEvent(ChangePinEvent.OnToggleNewPinVisibility) },
                    supportingText = "Must be exactly 4 digits./ " +
                        stringResource(Res.string.pin_length_hint)
                )
            }

            item {
                PinField(
                    title = "Confirm New PIN/ " + stringResource(Res.string.confirm_new_pin),
                    value = state.confirmPin,
                    visible = state.confirmPinVisible,
                    onValueChange = { onEvent(ChangePinEvent.OnConfirmPinChanged(it)) },
                    onToggleVisibility = { onEvent(ChangePinEvent.OnToggleConfirmPinVisibility) },
                    supportingText = if (state.pinsMismatch) {
                        "PINs do not match/ " + stringResource(Res.string.pins_do_not_match)
                    } else {
                        null
                    },
                    isError = state.pinsMismatch
                )
            }

            item {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = profileDividerColor.copy(alpha = 0.2f)
                )
            }

            item {
                AppButton(
                    text = "Save Changes/ " + stringResource(Res.string.save_changes),
                    onClick = { onEvent(ChangePinEvent.OnSaveClicked) },
                    enabled = state.canSubmit,
                    loading = state.saving,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun PinField(
    title: String,
    value: String,
    visible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    supportingText: String? = null,
    isError: Boolean = false
) {
    TitledAppTextField(
        title = title,
        value = value,
        onValueChange = onValueChange,
        placeholder = "",
        isError = isError,
        keyboardType = KeyboardType.NumberPassword,
        textAlign = TextAlign.Center,
        visualTransformation = if (visible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation(mask = '•')
        },
        trailingIcon = {
            Icon(
                painter = painterResource(Res.drawable.ic_eye),
                contentDescription = stringResource(
                    if (visible) Res.string.hide_pin else Res.string.show_pin
                ),
                tint = if (visible) MaterialTheme.colorScheme.primary else Gray45,
                modifier = Modifier
                    .size(Spacing.dp20)
                    .clickable(onClick = onToggleVisibility)
            )
        },
        supportingText = supportingText?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isError) MaterialTheme.colorScheme.error else GreyText
                )
            }
        }
    )
}

@Preview(locale = "hi", showBackground = true)
@Composable
fun ChangePinScreenPreview() {
    JantaNiveshTheme {
        ChangePinScreen(
            state = ChangePinUiState(newPin = "1234", confirmPin = "12"),
            onEvent = {}
        )
    }
}
