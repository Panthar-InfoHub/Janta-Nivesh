package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.TextFieldBorder
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.isoUtcToDisplayDate
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppDatePicker
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.OnboardingEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.OnboardingUiState

@Composable
fun EnterYourDOBScreen(
    state: OnboardingUiState,
    onEvent: (OnboardingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    if (state.showDatePicker) {
        AppDatePicker(
            show = state.showDatePicker,
            selectedDate = state.dob.toLongOrNull(),
            onDismiss = { onEvent(OnboardingEvent.OnDatePickerDismissed) },
            onDateSelected = { onEvent(OnboardingEvent.OnDobSelected(DateTimeUtils.epochMillisToIsoUtc(it)?:"")) },
        )
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

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onEvent(OnboardingEvent.OnDobFieldClicked)
                    },
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (state.dob.isEmpty()) MaterialTheme.colorScheme.outline else TextFieldBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = DateTimeUtils.isoUtcToSlashDate(state.dob).ifBlank {
                            stringResource(Res.string.select_dob_placeholder)
                        },
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (state.dob.isBlank()) {
                            GreyText
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )

                    Icon(
                        painter = painterResource(Res.drawable.dob_dropdown_icon),
                        contentDescription = stringResource(Res.string.select_date_desc),
                        modifier = Modifier.size(Spacing.dp24)
                    )
                }
            }

            AppButton(
                text = stringResource(Res.string.verify),
                onClick = { onEvent(OnboardingEvent.OnDobVerifyClicked) },
                enabled = state.isDobNextEnabled,
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp40),
                loading = state.isLoading
            )
        }
}

@Preview(showBackground = true, locale = "te")
@Composable
fun EnterYourDOBScreenPreview() {
    JantaNiveshTheme {
        EnterYourDOBScreen(
            state = OnboardingUiState(dob = "15/08/1947"),
            onEvent = {}
        )
    }
}
