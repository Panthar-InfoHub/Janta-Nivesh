package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.confirm_your_details
import jantanivesh.shared.generated.resources.dob_as_per_pan
import jantanivesh.shared.generated.resources.name_as_per_pan
import jantanivesh.shared.generated.resources.verify_your_info
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.formatMillisToIsoDate
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppDatePicker
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.JantaNiveshAndVelvetLogo
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.BasicDetailsEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.BasicDetailsUiState

@Composable
fun BasicDetailsScreen(
    state: BasicDetailsUiState,
    handleEvent: (BasicDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    AppDatePicker(
        show = showDatePicker,
        selectedDate = null,
        onDismiss = { showDatePicker = false },
        onDateSelected = { millis ->
            handleEvent(BasicDetailsEvent.OnDobChange(formatMillisToIsoDate(millis)))
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.dp24)
            .clearFocusOnTap()
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp18),
            contentPadding = PaddingValues(top = Spacing.dp24)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Confirm your details/ " + stringResource(Res.string.confirm_your_details),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "We need to verify your information to keep your financial journey secure and restorative./ " + stringResource(
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
                    title = "Name (as per PAN)/ " + stringResource(Res.string.name_as_per_pan),
                    value = state.name,
                    onValueChange = { handleEvent(BasicDetailsEvent.OnNameChange(it)) },
                    placeholder = "",
                    mandatory = true,
                    keyboardType = KeyboardType.Text,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
            }

            item {
                TitledDateField(
                    title = "Date of Birth (as per PAN)/ " + stringResource(Res.string.dob_as_per_pan),
                    value = state.dob,
                    onClick = { showDatePicker = true }
                )
            }

            item {
                AppButton(
                    text = "Proceed",
                    onClick = { handleEvent(BasicDetailsEvent.OnProceedClick) },
                    loading = state.isLoading,
                    enabled = state.canSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.dp24)
                        .genericDropShadow()
                )
            }
        }

        JantaNiveshAndVelvetLogo()
    }
}


@Preview(locale = "hi", showBackground = true)
@Composable
fun BasicDetailsScreenPreview(){
    JantaNiveshTheme {
        BasicDetailsScreen(
            state = BasicDetailsUiState(),
            handleEvent = {}
        )
    }
}