package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.dob_dropdown_icon
import jantanivesh.shared.generated.resources.dob_identity_verification
import jantanivesh.shared.generated.resources.enter_dob
import jantanivesh.shared.generated.resources.select_date_desc
import jantanivesh.shared.generated.resources.select_dob_placeholder
import jantanivesh.shared.generated.resources.verify
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBUiState

@Composable
fun EnterYourDOBScreen(
    state: EnterYourDOBUiState,
    onEvent: (EnterYourDOBEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    if (state.showDatePicker) {
        // TODO: Implement your Material3 DatePickerDialog here.
    }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.dp24)
        ) {
            Text(
                text = "Enter your date of birth/" + stringResource(Res.string.enter_dob),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = Spacing.dp16)
            )

            Column(
                modifier = Modifier.padding(bottom = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                Text(
                    text = "Please provide your date of birth for identity verification./" + stringResource(
                        Res.string.dob_identity_verification
                    ),
                    color = GreyText,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            AppTextField(
                value = state.dob,
                onValueChange = { /* Read only, handled by DatePicker */ },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onEvent(EnterYourDOBEvent.OnDobFieldClicked)
                    },
                placeholder = {
                    Text(
                        text = stringResource(Res.string.select_dob_placeholder),
                        style = MaterialTheme.typography.labelMedium,
                        color = GreyText
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.dob_dropdown_icon),
                        contentDescription = stringResource(Res.string.select_date_desc),
                        modifier = Modifier.size(Spacing.dp24)
                    )
                },
                interactionSource = interactionSource
            )
            AppButton(
                text = stringResource(Res.string.verify),
                onClick = { onEvent(EnterYourDOBEvent.OnVerifyClicked) },
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp40)
            )
        }
}

@Preview(showBackground = true, locale = "te")
@Composable
fun EnterYourDOBScreenPreview() {
    JantaNiveshTheme {
        EnterYourDOBScreen(
            state = EnterYourDOBUiState(dob = "15/08/1947"),
            onEvent = {}
        )
    }
}