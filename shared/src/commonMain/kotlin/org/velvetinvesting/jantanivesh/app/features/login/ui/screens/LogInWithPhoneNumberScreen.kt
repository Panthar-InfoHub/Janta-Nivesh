package org.velvetinvesting.jantanivesh.app.features.login.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.india_code
import jantanivesh.shared.generated.resources.login_mobile_number
import jantanivesh.shared.generated.resources.login_mobile_ownership_declaration
import jantanivesh.shared.generated.resources.login_prompt_action
import jantanivesh.shared.generated.resources.login_prompt_question
import jantanivesh.shared.generated.resources.login_terms_acceptance
import jantanivesh.shared.generated.resources.otp_verify_identity
import jantanivesh.shared.generated.resources.signup_mobile_number
import jantanivesh.shared.generated.resources.signup_prompt_action
import jantanivesh.shared.generated.resources.signup_prompt_question
import jantanivesh.shared.generated.resources.verify
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AgreementCheckBoxCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.CheckBoxCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.JantaNiveshAndVelvetLogo
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberEvent
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberUiState

@Composable
fun LoginWithPhoneNumberScreen(
    state: LoginWithPhoneNumberUiState,
    onEvent: (LoginWithPhoneNumberEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier
        .clearFocusOnTap()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.dp24, vertical = Spacing.dp16)
        ) {

            AppBackButton(onClick = { onEvent(LoginWithPhoneNumberEvent.OnBackClicked) })

            Text(
                text = if (state.isSignUpMode) {
                    "Sign up with your mobile number/" +
                            stringResource(Res.string.signup_mobile_number)
                } else {
                    "Log in with your mobile number/" +
                            stringResource(Res.string.login_mobile_number)
                },
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = Spacing.dp16)
            )

            // Everything between the header and the logo scrolls, so the consent cards stay
            // reachable on short screens and with the keyboard up.
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                item {
                    Text(
                        text = "We'll send a 4 digit OTP to verify your identity. / " + stringResource(
                            Res.string.otp_verify_identity
                        ),
                        color = GreyText,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(bottom = Spacing.dp8)
                    )
                }

                item {
                    AppTextField(
                        value = state.phoneNumber,
                        onValueChange = {
                            onEvent(LoginWithPhoneNumberEvent.OnPhoneNumberChanged(it))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = Spacing.dp16)
                            ) {
                                Text(
                                    text = stringResource(Res.string.india_code),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Black,
                                    modifier = Modifier.padding(end = Spacing.dp8)
                                )
                                VerticalDivider(
                                    modifier = Modifier.height(Spacing.dp24)
                                        .padding(horizontal = Spacing.dp4),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    )
                }

                // Consents belong to account creation only; logging in reuses what was
                // already agreed to at sign up.
                if (state.isSignUpMode) {
                    item {
                        AgreementCheckBoxCard(
                            text = "I declare that this mobile number belongs to me and is " +
                                    "registered in my own name./ " +
                                    stringResource(Res.string.login_mobile_ownership_declaration),
                            isConsentChecked = state.isOwnershipDeclared,
                            onConsentChange = {
                                onEvent(LoginWithPhoneNumberEvent.OnOwnershipDeclarationChanged(it))
                            }
                        )
                    }

                    item {
                        AgreementCheckBoxCard(
                            text = "I have read and accept the Terms & Conditions and the Privacy " +
                                    "Policy of Janta Nivesh./ " +
                                    stringResource(Res.string.login_terms_acceptance),
                            isConsentChecked = state.areTermsAccepted,
                            onConsentChange = {
                                onEvent(LoginWithPhoneNumberEvent.OnTermsAcceptanceChanged(it))
                            }
                        )
                    }
                }

                item {
                    AppButton(
                        text = stringResource(Res.string.verify),
                        onClick = { onEvent(LoginWithPhoneNumberEvent.OnVerifyClicked) },
                        enabled = state.isNextEnabled,
                        loading = state.isLoading,
                        modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp24)
                    )
                }

                item {
                    AuthModeSwitchPrompt(
                        isSignUpMode = state.isSignUpMode,
                        onClick = { onEvent(LoginWithPhoneNumberEvent.OnAuthModeToggled) }
                    )
                }
            }

            JantaNiveshAndVelvetLogo()

        }
    }
}

/**
 * Separates the two flows: the question stays plain text and only the action word is
 * tappable, on both the English and the localized line.
 */
@Composable
private fun AuthModeSwitchPrompt(
    isSignUpMode: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questionEnglish = if (isSignUpMode) "Already have an account?" else "Don't have an account?"
    val actionEnglish = if (isSignUpMode) "Log in" else "Sign up"
    val questionLocalized = stringResource(
        if (isSignUpMode) Res.string.login_prompt_question else Res.string.signup_prompt_question
    )
    val actionLocalized = stringResource(
        if (isSignUpMode) Res.string.login_prompt_action else Res.string.signup_prompt_action
    )

    Column(
        modifier = modifier.fillMaxWidth().padding(top = Spacing.dp16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
    ) {
        AuthModeSwitchLine(questionEnglish, actionEnglish, onClick)
        AuthModeSwitchLine(questionLocalized, actionLocalized, onClick)
    }
}

@Composable
private fun AuthModeSwitchLine(
    question: String,
    action: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = question,
            color = GreyText,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = action,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(start = Spacing.dp4)
                .clickable(onClick = onClick)
        )
    }
}

@Preview(showBackground = true, locale = "hi")
@Composable
fun LoginWithPhoneNumberPreview() {
    JantaNiveshTheme {
        LoginWithPhoneNumberScreen(
            state = LoginWithPhoneNumberUiState(phoneNumber = "9876543210"),
            onEvent = {}
        )
    }
}