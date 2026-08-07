package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.agreement_text
import jantanivesh.shared.generated.resources.dob_as_per_pan
import jantanivesh.shared.generated.resources.name_as_per_pan
import jantanivesh.shared.generated.resources.nsdl_details
import jantanivesh.shared.generated.resources.pan_label
import jantanivesh.shared.generated.resources.verify_your_info
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.JantaNiveshAndVelvetLogo
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.ConfirmYourDetailsEvent
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.ConfirmYourDetailsUiState

@Composable
fun ConfirmYourDetailsScreen(
    state: ConfirmYourDetailsUiState,
    handleEvent: (ConfirmYourDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.dp24)
            .clearFocusOnTap()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp18)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Confirm your details/ " + stringResource(Res.string.nsdl_details),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "We need to verify your information to keep your financial journey secure and restorative./ ." + stringResource(
                            Res.string.verify_your_info
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray444,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                TitledAppTextField(
                    title = "PAN/ " + stringResource(Res.string.pan_label),
                    value = state.pan,
                    onValueChange = { handleEvent(ConfirmYourDetailsEvent.OnPanChange(it)) },
                    placeholder = "",
                    keyboardType = KeyboardType.Text
                )
            }

            item {
                TitledAppTextField(
                    title = "Name (as per PAN)/ " + stringResource(Res.string.name_as_per_pan),
                    value = state.name,
                    onValueChange = { handleEvent(ConfirmYourDetailsEvent.OnNameChange(it)) },
                    placeholder = "",
                    keyboardType = KeyboardType.Text
                )
            }

            item {
                TitledAppTextField(
                    title = "Date of Birth (as per PAN)/ " + stringResource(Res.string.dob_as_per_pan),
                    value = state.dob,
                    onValueChange = { handleEvent(ConfirmYourDetailsEvent.OnDobChange(it)) },
                    placeholder = "",
                    keyboardType = KeyboardType.Text
                )
            }

            item {
                SecureAndSafeCard(
                    isConsentChecked = state.isConsentChecked,
                    onConsentChange = { handleEvent(ConfirmYourDetailsEvent.OnConsentChange(it)) },
                    onTermsClick = { handleEvent(ConfirmYourDetailsEvent.OnTermsClick) },
                    onPrivacyClick = { handleEvent(ConfirmYourDetailsEvent.OnPrivacyClick) },
                    onReadMoreClick = { handleEvent(ConfirmYourDetailsEvent.OnReadMoreClick) }
                )
            }

            item {
                JantaNiveshAndVelvetLogo()
            }
        }

        AppButton(
            text = "Proceed",
            onClick = { handleEvent(ConfirmYourDetailsEvent.OnProceedClick) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.dp24)
                .genericDropShadow()
        )
    }
}

@Composable
private fun SecureAndSafeCard(
    isConsentChecked: Boolean,
    onConsentChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onReadMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .border(Spacing.dp1, FilterChipUnselected, RoundedCornerShape(Spacing.dp16))
            .background(GoalIconBg)
            .padding(Spacing.dp20),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = isConsentChecked,
            onCheckedChange = onConsentChange,
            colors = CheckboxDefaults.colors(checkedColor = Primary, checkmarkColor = White),
            modifier = Modifier
                .size(Spacing.dp24)
                .padding(top = Spacing.dp2)
        )

        val annotatedString = buildAnnotatedString {
            withStyle(SpanStyle(color = Primary, fontSize = 16.sp)) {
                append("By submitting this consent, I authorize Zeny to call/ SMS/ WhatsApp/ email me about its products & have accepted the ")
            }

            val termsLink = LinkAnnotation.Clickable("TERMS") {
                onTermsClick()
            }
            withLink(termsLink) {
                withStyle(SpanStyle(color = Black, fontSize = 16.sp)) {
                    append("Terms of Use")
                }
            }

            withStyle(SpanStyle(color = Primary, fontSize = 16.sp)) {
                append(" & the ")
            }

            val privacyLink = LinkAnnotation.Clickable("PRIVACY") {
                onPrivacyClick()
            }
            withLink(privacyLink) {
                withStyle(SpanStyle(color = Black, fontSize = 16.sp)) {
                    append("Privacy Policy.")
                }
            }

            withStyle(SpanStyle(color = Primary, fontSize = 16.sp)) {
                append(" I authorize  ")
            }

            val readMoreLink = LinkAnnotation.Clickable("READ_MORE") {
                onReadMoreClick()
            }
            withLink(readMoreLink) {
                withStyle(
                    SpanStyle(
                        color = Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Read More")
                }
            }
            withStyle(SpanStyle(color = Primary, fontSize = 16.sp)){
                append(stringResource(Res.string.agreement_text))
            }
        }
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(locale = "hi")
@Composable
fun ConfirmYourDetailsScreenPreview(){
    JantaNiveshTheme {
        ConfirmYourDetailsScreen(
            state = ConfirmYourDetailsUiState(),
            handleEvent = {}
        )
    }
}