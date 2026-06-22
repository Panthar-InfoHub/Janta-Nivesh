package org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.headphone
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.FeatureCardText
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.UploadBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.placeholderColor
import org.velvetinvesting.jantanivesh.app.core.utils.isValidEmail
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.RequestCallbackEvent
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.RequestCallbackUiState

@Composable
fun RequestCallbackScreen(
    state: RequestCallbackUiState,
    onEvent: (RequestCallbackEvent) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    ) {
    Scaffold(modifier = modifier.clearFocusOnTap()){pv->
        Column(
            modifier = modifier.fillMaxWidth()
                .background(White)
                .padding(pv),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)

        ) {
            BackHeader(
                title = "",
                onBack=onBack,
                modifier= Modifier.padding(horizontal = Spacing.dp16)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
                    .padding(horizontal = Spacing.dp16),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    Spacing.dp24
                ),
                contentPadding = PaddingValues(bottom = Spacing.dp16)
            ) {
                item {
                    Box(
                        modifier = Modifier.padding(Spacing.dp12).size(Spacing.dp64)
                            .clip(CircleShape)
                            .background(color = UploadBoxBorder, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.headphone),
                            contentDescription = "headphone",
                            tint = Primary
                        )
                    }
                    Text(
                        "Leave your details below and one of our expert\n" +
                                "advisors will call you back within 24 hours.",
                        color = Gray444,
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    Box(
                        Modifier.genericDropShadow(shape = RoundedCornerShape(Spacing.dp24))
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(Spacing.dp24)).background(
                                color = White, shape = RoundedCornerShape(
                                    Spacing.dp24
                                )
                            ).padding(Spacing.dp20)
                    ) {
                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                "Full Name",
                                style = MaterialTheme.typography.titleSmall,
                                color = Black
                            )
                            Spacer(modifier = Modifier.height(Spacing.dp6))

                            AppTextField(
                                value = state.fullName,
                                onValueChange = { onEvent(RequestCallbackEvent.OnNameChanged(it)) },
                                placeholder = {
                                    Text(
                                        "Jane Doe",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = placeholderColor
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(Spacing.dp18))
                            Text(
                                "Mobile Number",
                                style = MaterialTheme.typography.titleSmall,
                                color = Black
                            )
                            Spacer(modifier = Modifier.height(Spacing.dp6))

                            AppTextField(
                                value = state.mobileNumber,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                onValueChange = { onEvent(RequestCallbackEvent.OnMobileChanged(it)) },
                                placeholder = {
                                    Text(
                                        "+1 (555) 000-000",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = placeholderColor
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(Spacing.dp18))

                            Text(
                                "Email (Optional)",
                                style = MaterialTheme.typography.titleSmall,
                                color = Black
                            )
                            Spacer(modifier = Modifier.height(Spacing.dp6))


                            AppTextField(
                                value = state.email,
                                onValueChange = { onEvent(RequestCallbackEvent.OnEmailChanged(it)) },
                                placeholder = {
                                    Text(
                                        "jane@example.com",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = placeholderColor
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                "What type of insurance do you need help with?",
                                style = MaterialTheme.typography.titleSmall,
                                color = FeatureCardText
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
                                verticalArrangement = Arrangement.spacedBy(
                                    Spacing.dp12
                                )
                            ) {
                                state.insuranceTypeList.forEach { type ->
                                    HelpTypeBox(
                                        text = type,
                                        selected = state.selectedInsuranceType == type,
                                        onClick = {
                                            onEvent(
                                                RequestCallbackEvent.OnInsuranceTypeSelected(type)
                                            )
                                        })

                                }

                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                "Preferred Call Time",
                                style = MaterialTheme.typography.titleSmall,
                                color = FeatureCardText
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
                                verticalArrangement = Arrangement.spacedBy(
                                    Spacing.dp12
                                )
                            ) {
                                state.callTimeList.forEach { time ->
                                    HelpTypeBox(
                                        text = time,
                                        selected = state.selectedCallTime == time,
                                        onClick = {
                                            onEvent(
                                                RequestCallbackEvent.OnCallTimeSelected(time)
                                            )
                                        })

                                }

                            }
                        }


                    }
                }
            }

            NextButtonFooter(
                value="Submit",
                enabled = (state.fullName != "" && state.mobileNumber != "" && validityCheck(email = state.email)),
                onClick = { onEvent(RequestCallbackEvent.OnSubmitClicked) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun HelpTypeBox(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.border(
            width = Spacing.dp1,
            color = if (selected) Primary else UploadBoxBorder,
            shape = RoundedCornerShape(
                50.dp
            )
        ).clip(RoundedCornerShape(50.dp))
            .clickable { onClick()}.background(color = if (selected) Primary else White, shape = RoundedCornerShape(50.dp))
            .padding(horizontal = Spacing.dp20, vertical = Spacing.dp10)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleSmall,
            color = if (selected) White else Black
        )
    }
}


@Preview
@Composable
fun InsuranceScreenPreview2() {
    JantaNiveshTheme {
        RequestCallbackScreen(RequestCallbackUiState(),{}, onBack = {})
    }
}

fun validityCheck(email:String, optional: Boolean = true) : Boolean{
    return if(optional){
        if (email.isBlank()) {
            true
        } else {
            isValidEmail(email)
        }
    }else{
        if (email.isBlank()) {
            false
        } else {
            isValidEmail(email)
        }
    }
}