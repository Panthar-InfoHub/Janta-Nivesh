package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import jantanivesh.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextFieldDefaults
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.TextFieldBorder

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

    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.dp24, vertical = Spacing.dp16)
        ) {

            Column(modifier = Modifier.weight(1f)) {

                TopAppBarWithBackButtonAndStepCount(
                    stepCount = 4,
                    totalSteps = 5,
                    onBack = { onEvent(EnterYourDOBEvent.OnBackClicked) }
                )

                Text(
                    text = stringResource(Res.string.enter_dob) + stringResource(Res.string.enter_dob_translated),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Spacing.dp16)
                )

                Column(modifier = Modifier.padding(bottom = Spacing.dp24), verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
                    Text(
                        text = stringResource(Res.string.dob_identity_verification),
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(Res.string.dob_identity_verification_translated),
                        color = GreyText,
                        style = MaterialTheme.typography.titleMedium
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

@Preview(showBackground = true)
@Composable
fun EnterYourDOBScreenPreview() {
    JantaNiveshTheme {
        EnterYourDOBScreen(
            state = EnterYourDOBUiState(dob = "15/08/1947"),
            onEvent = {}
        )
    }
}