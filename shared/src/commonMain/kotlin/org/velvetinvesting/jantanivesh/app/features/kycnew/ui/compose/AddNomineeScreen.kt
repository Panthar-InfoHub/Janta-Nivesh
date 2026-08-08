package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.CheckBoxCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.AddNomineeEvent
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.AddNomineeUiState
import org.velvetinvesting.jantanivesh.app.utils.tradingaccount.NomineeIdentityType
import org.velvetinvesting.jantanivesh.app.utils.tradingaccount.NomineeRelationship

@Composable
fun AddNomineeScreen(
    state: AddNomineeUiState,
    handleEvent: (AddNomineeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = White)
            .padding(Spacing.dp20)
            .clearFocusOnTap()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Add your Nominee/ " + stringResource(Res.string.add_your_nominee_label),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = "Who should get your money if something happens? Make sure it is a trusted person or family member./ " + stringResource(
                            Res.string.nominee_subtitle
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray444
                    )
                }
            }

            item {
                CheckBoxCard(
                    text = "I will add Nominees later",
                    isChecked = state.addLater,
                    onCheckedChange = { handleEvent(AddNomineeEvent.OnAddLaterChanged(it)) }
                )
            }

            itemsIndexed(state.nominees) { index, nominee ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
                    modifier = Modifier
                        .genericDropShadow()
                        .clip(RoundedCornerShape(Spacing.dp24))
                        .background(White)
                        .padding(Spacing.dp24)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Nominee ${index + 1}",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            if (state.nominees.size > 1) {
                                Icon(
                                    painter = painterResource(Res.drawable.delete_icon),
                                    contentDescription = "Discard Nominee",
                                    tint = Black,
                                    modifier = Modifier
                                        .size(Spacing.dp18)
                                        .clickable { handleEvent(AddNomineeEvent.OnDeleteNomineeClick(index)) }
                                )
                            }
                        }
                        HorizontalDivider(
                            thickness = Spacing.dp1,
                            color = Primary,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    TitledAppTextField(
                        title = "Nominee Name/" + stringResource(Res.string.nominee_name),
                        value = nominee.name,
                        onValueChange = { handleEvent(AddNomineeEvent.OnNameChanged(index, it)) },
                        placeholder = "",
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier.padding(top = Spacing.dp16)
                    )
                    DropDownSelector(
                        title = "Relationship/ " + stringResource(Res.string.relation),
                        value = nominee.relationship?.displayName ?: "",
                        onValueChange = {
                                handleEvent(AddNomineeEvent.OnRelationshipChanged(index, it))
                        },
                        list = NomineeRelationship.entries,
                        placeholder = "",
                        textConvertor = { it.displayName }
                    )
                    TitledAppTextField(
                        title = "Percentage Allocation/" + stringResource(Res.string.percentage_allocation),
                        value = nominee.percentageAllocation,
                        onValueChange = { handleEvent(AddNomineeEvent.OnPercentageAllocationChanged(index, it)) },
                        placeholder = "",
                        keyboardType = KeyboardType.Number,
                        trailingIcon = {
                            Text("%", style = MaterialTheme.typography.bodyLarge, color = Gray444)
                        }
                    )
                    TitledAppTextField(
                        title = "Date of Birth/ " + stringResource(Res.string.date_of_birth),
                        value = nominee.dateOfBirth,
                        onValueChange = { handleEvent(AddNomineeEvent.OnDateOfBirthChanged(index, it)) },
                        placeholder = "mm/dd/yyyy",
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    DropDownSelector(
                        title = "Identity Type/ " + stringResource(Res.string.identity_type),
                        value = nominee.identityType?.displayName ?: "",
                        onValueChange = {
                                handleEvent(AddNomineeEvent.OnIdentityTypeChanged(index, it))
                        },
                        list = NomineeIdentityType.entries,
                        placeholder = "",
                        textConvertor = { it.displayName }
                    )
                    TitledAppTextField(
                        title = "PAN Card/ " + stringResource(Res.string.pan_card_label),
                        value = nominee.panCard,
                        onValueChange = { handleEvent(AddNomineeEvent.OnPanCardChanged(index, it)) },
                        placeholder = "",
                    )
                    TitledAppTextField(
                        title = "Email/ " + stringResource(Res.string.email),
                        value = nominee.email,
                        onValueChange = { handleEvent(AddNomineeEvent.OnEmailChanged(index, it)) },
                        keyboardType = KeyboardType.Email,
                        placeholder = "",
                    )
                    TitledAppTextField(
                        title = "Phone/ " + stringResource(Res.string.phone_label),
                        value = nominee.phone,
                        onValueChange = { handleEvent(AddNomineeEvent.OnPhoneChanged(index, it)) },
                        keyboardType = KeyboardType.Number,
                        placeholder = "",
                    )
                    TitledAppTextField(
                        title = "Address Line 1/ " + stringResource(Res.string.address_line_1_nominee),
                        value = nominee.addressLine1,
                        onValueChange = { handleEvent(AddNomineeEvent.OnAddressLine1Changed(index, it)) },
                        placeholder = "\n\n\n",
                    )
                    TitledAppTextField(
                        title = "Address Line 2/ " + stringResource(Res.string.address_line_2_nominee),
                        value = nominee.addressLine2,
                        onValueChange = { handleEvent(AddNomineeEvent.OnAddressLine2Changed(index, it)) },
                        placeholder = "\n\n\n",
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TitledAppTextField(
                            title = "City/ " + stringResource(Res.string.city),
                            value = nominee.city,
                            onValueChange = { handleEvent(AddNomineeEvent.OnCityChanged(index, it)) },
                            placeholder = "",
                            modifier = Modifier.weight(1f)
                        )
                        TitledAppTextField(
                            title = "State/ " + stringResource(Res.string.state),
                            value = nominee.state,
                            onValueChange = { handleEvent(AddNomineeEvent.OnStateChanged(index, it)) },
                            placeholder = "",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (index == state.nominees.lastIndex) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { handleEvent(AddNomineeEvent.OnAddAnotherNomineeClick) }
                                .padding(top = Spacing.dp24)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.plus_inside_circle_icon),
                                contentDescription = "Add another nominee",
                                tint = Gray444,
                                modifier = Modifier.size(Spacing.dp20)
                            )
                            Text(
                                "Add another nominee/ " + stringResource(Res.string.add_another_nominee),
                                style = MaterialTheme.typography.labelSmall,
                                color = Gray444
                            )
                        }
                    }
                }
            }
        }

        AppButton(
            text = "Confirm and proceed",
            onClick = { handleEvent(AddNomineeEvent.OnConfirmAndProceedClick) },
            modifier = Modifier
                .fillMaxWidth()
                .genericDropShadow()
        )
    }
}

@Preview(locale = "hi", heightDp = 1800, showBackground = true)
@Composable
private fun AddNomineeScreenPreview() {
    JantaNiveshTheme {
        AddNomineeScreen(
            state = AddNomineeUiState(),
            handleEvent = {}
        )
    }
}