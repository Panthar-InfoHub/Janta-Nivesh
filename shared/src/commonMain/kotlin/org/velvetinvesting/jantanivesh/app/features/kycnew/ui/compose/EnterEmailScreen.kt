package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.email_declaration
import jantanivesh.shared.generated.resources.email_id_label
import jantanivesh.shared.generated.resources.email_subtitle
import jantanivesh.shared.generated.resources.what_is_your_email_id
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AgreementCheckBoxCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.JantaNiveshAndVelvetLogo
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.EmailIdEvent
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.EmailIdUiState

@Composable
fun EmailIdScreen(
    state: EmailIdUiState,
    handleEvent: (EmailIdEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = White)
            .padding(Spacing.dp24)
            .clearFocusOnTap()

    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "What is your Email ID?/ " + stringResource(Res.string.what_is_your_email_id),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = "We'll use this to keep your account secure and send important updates./ " + stringResource(
                            Res.string.email_subtitle
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray444
                    )
                }
            }

            item {
                TitledAppTextField(
                    title = "Email ID/ " + stringResource(Res.string.email_id_label),
                    value = state.email,
                    onValueChange = { handleEvent(EmailIdEvent.OnEmailChange(it)) },
                    placeholder = "you@example.com",
                    trailingIcon = {
                        Box(
                            modifier = Modifier
                                .padding(end = Spacing.dp8)
                                .clip(CircleShape)
                                .background(Secondary)
                                .clickable { handleEvent(EmailIdEvent.OnGmailSuffixClick) }
                        ) {
                            Text(
                                "@gmail.com",
                                style = MaterialTheme.typography.bodySmall,
                                color = White,
                                modifier = Modifier.padding(
                                    horizontal = Spacing.dp12,
                                    vertical = Spacing.dp8
                                )
                            )
                        }
                    }
                )
            }

            item {
                AgreementCheckBoxCard(
                    isConsentChecked = state.isConsentChecked,
                    onConsentChange = { handleEvent(EmailIdEvent.OnConsentChange(it)) },
                    text = "I Declare that this email ID belongs to me and is operated by me. I will receive account statements, transection confirmations and regulatory communication on this mail /" + stringResource(Res.string.email_declaration),
                )
            }

            item {
                AppButton(
                    text = "Submit",
                    onClick = { handleEvent(EmailIdEvent.OnSubmitClick) },
                    modifier = Modifier.fillMaxWidth().genericDropShadow()
                )
            }
        }

        JantaNiveshAndVelvetLogo()
    }
}

@Preview(locale = "hi")
@Composable
fun EmailIdScreenPreview() {
    JantaNiveshTheme {
        EmailIdScreen(
            state = EmailIdUiState(),
            handleEvent = {}
        )
    }
}