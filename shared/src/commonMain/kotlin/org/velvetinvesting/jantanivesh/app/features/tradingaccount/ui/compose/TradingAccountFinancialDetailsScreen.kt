package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.delete_icon
import jantanivesh.shared.generated.resources.full_name_placeholder
import jantanivesh.shared.generated.resources.kyc_form_aadhaar_number_label
import jantanivesh.shared.generated.resources.kyc_form_email_address_label
import jantanivesh.shared.generated.resources.location_icon
import jantanivesh.shared.generated.resources.tick_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.sharad.velvetinvestment.utils.tradingaccount.NomineeIdentityType
import org.sharad.velvetinvestment.utils.tradingaccount.NomineeRelationship
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.HolderNatureTextColor
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedTextColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppDatePicker
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BarHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.OnBoardingDateField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.dashedBorder
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.OccupationType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Country
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountUiState

@Preview(heightDp = 2000, showBackground = true, locale = "hi")
@Composable
fun TradingAccountFinancialDetailsPreview() {
    JantaNiveshTheme {
        TradingAccountFinancialDetailsScreen(
            pv = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            uiState = TradingAccountUiState(
                formState = UiState.Success(TradingAccountFormDomain(data = Data())),
                holderNature = Holding.SINGLE
            ),
            handleEvent = {},
            onClick = {},
            onBackClick = {}
        )
    }
}

@Composable
fun TradingAccountFinancialDetailsScreen(
    pv: PaddingValues,
    uiState: TradingAccountUiState,
    handleEvent: (TradingAccountEvent) -> Unit,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {

        LocalTopAppBarWithBackButtonAndStepCount(
            title = "Trading",
            stepCount = 2,
            totalSteps = 5,
            onBack = onBackClick,
            modifier = Modifier.padding(pv)
        )

        UiStateContainer(
            uiState = uiState.formState,
            onRetry = { handleEvent(TradingAccountEvent.GetUserData) }
        ) { baseResponse ->
            val data = baseResponse.data
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                .clip(RoundedCornerShape(Spacing.dp24))
                                .background(color = Color.White, shape = RoundedCornerShape(Spacing.dp10))
                                .padding(Spacing.dp16),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Occupation/ ",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                                color = HolderNatureTextColor
                            )
                            DropDownSelector(
                                value = OccupationType.getDisplayNameFromCode(data.occupation_code)
                                    ?: "",
                                onValueChange = {
                                    handleEvent(TradingAccountEvent.OnOccupationChange(it.code))
                                },
                                placeholder = "Select Occupation",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                list = OccupationType.entries,
                                textConvertor = {
                                    it.displayName
                                }
                            )
                            HolderNature(
                                selected = uiState.holderNature,
                                onHolderNatureChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnHolderNatureChangeUi(
                                            it
                                        )
                                    )
                                }
                            )
                        }
                    }

                    if (uiState.holderNature == Holding.JOINT) {
                        item {
                            JointHolder(
                                jointHolder = "Second Holder",

                                secondHolderFirstName = data.second_holder_first_name,
                                onSecondHolderFirstNameChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnSecondFirstNameChange(
                                            it
                                        )
                                    )
                                },

                                secondHolderPan = data.second_holder_pan,
                                onSecondHolderPanChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnSecondPanChange(
                                            it
                                        )
                                    )
                                },

                                secondHolderEmail = data.second_holder_email,
                                onSecondHolderEmailChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnSecondEmailChange(
                                            it
                                        )
                                    )
                                },

                                secondHolderMobile = data.second_holder_mobile,
                                onSecondHolderMobileChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnSecondMobileChange(
                                            it
                                        )
                                    )
                                },

                                secondHolderDOB = data.second_holder_dob,
                                onSecondHolderDOBClick = {
                                    handleEvent(
                                        TradingAccountEvent.OnSecondDobChange(
                                            it
                                        )
                                    )
                                },
                            )
                        }

                        item {
                            CheckBoxComp(
                                heading = "+ Add Another Nominee",
                                checked = uiState.enableThirdHolder,
                                onCheckedChange = {
                                    if (it) handleEvent(TradingAccountEvent.AddThirdHolder)
                                    else handleEvent(TradingAccountEvent.RemoveThirdHolder)
                                }
                            )
                        }
                        if (uiState.enableThirdHolder) {
                            item {
                                JointHolder(
                                    jointHolder = "Third Holder",

                                    secondHolderFirstName = data.third_holder_first_name,
                                    onSecondHolderFirstNameChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnThirdFirstNameChange(it)
                                        )
                                    },

                                    secondHolderPan = data.third_holder_pan,
                                    onSecondHolderPanChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnThirdPanChange(
                                                it
                                            )
                                        )
                                    },

                                    secondHolderEmail = data.third_holder_email,
                                    onSecondHolderEmailChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnThirdEmailChange(
                                                it
                                            )
                                        )
                                    },

                                    secondHolderMobile = data.third_holder_mobile,
                                    onSecondHolderMobileChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnThirdMobileChange(
                                                it
                                            )
                                        )
                                    },

                                    secondHolderDOB = data.third_holder_dob,
                                    onSecondHolderDOBClick = {
                                        handleEvent(
                                            TradingAccountEvent.OnThirdDobChange(
                                                it
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    } // joint holder if block end

                    item {

                    }

                    item {
                        Column(
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                .clip(RoundedCornerShape(Spacing.dp24))
                                .background(color = Color.White, shape = RoundedCornerShape(Spacing.dp10))
                                .padding(horizontal = Spacing.dp16, vertical = Spacing.dp24),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                        ) {
                            CheckBoxComp(
                                heading = "I wish to nominate a person for my account in the event of my death",
                                subheading = "मैं अपनी मृत्यु की स्थिति में, अपने खाते के लिए किसी व्यक्ति को नॉमिनेट करना चाहता हूँ।", // TODO add stringResource,
                                onCheckedChange = {},
                                checked = false
                            )
                            HorizontalDivider(
                                thickness = Spacing.dp1,
                                color = Color(0xffDCE9FF),
                                modifier = Modifier.padding(vertical = Spacing.dp8)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Nominee 1/ ",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xff0B1C30)
                                )
                                IconButton(
                                    onClick = {}, // TODO add delete functionality
                                    modifier = Modifier.padding(end = Spacing.dp16)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.delete_icon),
                                        contentDescription = "Delete",
                                        modifier = Modifier.size(Spacing.dp15),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            TitledAppTextField(
                                title = "Full Name/ " + stringResource(Res.string.full_name_placeholder),
                                value = data.nominee_1_name,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnNominee1NameChange(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Nominee Name",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                            ) {
                                DropDownSelector(
                                    title = "Relation/ ",
                                    value = NomineeRelationship.getDisplayNameFromCode(data.nominee_1_relationship)
                                        ?: "",
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnNominee1RelationChange(
                                                it.code
                                            )
                                        )
                                    },
                                    placeholder = "Relation",
                                    mandatory = true,
                                    modifier = Modifier.weight(1f),
                                    list = NomineeRelationship.entries,
                                    textConvertor = {
                                        it.displayName
                                    }
                                )

                                OnBoardingDateField(
                                    value = data.nominee_1_dob,
                                    placeholder = "Select DOB",
                                    label = "Date of Birth/ ", //TODO add stringResource
                                    mandatory = true,
                                    onClick = {
                                        handleEvent(TradingAccountEvent.ShowCalender)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            DropDownSelector(
                                title = "Identity Type/ ", // TODO add stringResource
                                value = NomineeIdentityType.getDisplayNameFromCode(data.nominee_1_identity_type)
                                    ?: "",
                                onValueChange = { selection ->
                                    handleEvent(
                                        TradingAccountEvent.OnNominee1IdentityTypeChange(
                                            selection.code
                                        )
                                    )
                                },
                                placeholder = "Nominee identity",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                list = NomineeIdentityType.entries,
                                textConvertor = { it.displayName }
                            )
                            TitledAppTextField(
                                title = "Aadhaar Number/ " + "(" + stringResource(Res.string.kyc_form_aadhaar_number_label) + ")",
                                value = data.nominee_1_identity_number,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnNominee1IdentityNumberChange(
                                            it
                                        )
                                    )
                                },
                                placeholder = "xxxx xxxx 1234",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardType = if (data.nominee_1_identity_type == NomineeIdentityType.PAN.code) KeyboardType.Text else KeyboardType.Number,
                            )
                            TitledAppTextField(
                                title = "Email Address/ " + "(" + stringResource(Res.string.kyc_form_email_address_label) + ")",
                                value = data.nominee_1_email,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnNominee1EmailChange(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Nominee Email",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardType = KeyboardType.Email,
                            )
                            TitledAppTextField(
                                title = "Mobile Number",
                                value = data.nominee_1_mobile,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnNominee1MobileChange(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Nominee Mobile number",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardType = KeyboardType.Number,
                            )

                            ////////////////////////////////////////
                            /////////// ADDRESS SECTION ////////////
                            ////////////////////////////////////////
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.location_icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(Spacing.dp20),
                                        tint = GreyText
                                    )
                                    Text(
                                        "Nominee Address/ ",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                CheckBoxComp(
                                    heading = "Same as Applicant Address",
                                    onCheckedChange = {},
                                    checked = false
                                )
                                TitledAppTextField(
                                    value = data.nominee_1_address1,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnNominee1Address1Change(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "Address Line 1",
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardType = KeyboardType.Text,
                                )
                                TitledAppTextField(
                                    value = data.nominee_1_address2,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnNominee1Address2Change(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "Address Line 2 (Optional)",
                                    mandatory = false,
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardType = KeyboardType.Text,
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                                ) {
                                    TitledAppTextField(
                                        value = data.nominee_1_city,
                                        onValueChange = {
                                            handleEvent(
                                                TradingAccountEvent.OnNominee1CityChange(
                                                    it
                                                )
                                            )
                                        },
                                        placeholder = "City",
                                        mandatory = false,
                                        modifier = Modifier.weight(1f),
                                        keyboardType = KeyboardType.Text,
                                    )
                                    TitledAppTextField(
                                        value = data.nominee_1_pin,
                                        onValueChange = {
                                            handleEvent(
                                                TradingAccountEvent.OnNominee1PincodeChange(
                                                    it
                                                )
                                            )
                                        },
                                        placeholder = "Pincode",
                                        mandatory = true,
                                        modifier = Modifier.weight(1f),
                                        keyboardType = KeyboardType.Text,
                                    )
                                }
                                DropDownSelector(
                                    value = data.nominee_1_country,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnNominee1CountryChange(
                                                it.code
                                            )
                                        )
                                    },
                                    placeholder = "Country",
                                    list = Country.entries.toList(),
                                    textConvertor = {
                                        it.displayName
                                    }
                                )
                            }
                        }
                    }
                    item {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(Spacing.dp8),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Black),
                            modifier = Modifier.fillMaxWidth().dashedBorder(color = SelectedBoxBorder, dashLength = Spacing.dp2, gapLength = Spacing.dp2)
                        ) {
                            Text(
                                "+ Add Another Nominee",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                            )
                        }
                    }


                    if (data.nominee_1_minor_flag == "Y") {
                        item {
                            TitledAppTextField(
                                title = "Guardian Name",
                                value = data.nominee_1_guardian,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnNominee1GuardianChange(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Enter Guardian Name",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardType = KeyboardType.Text,
                            )
                        }

                        item {
                            TitledAppTextField(
                                title = "Guardian PAN",
                                value = data.nominee_1_guardian_pan,
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnNominee1GuardianPanChange(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Enter Guardian PAN",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                keyboardType = KeyboardType.Text,
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(pv.calculateBottomPadding() + 16.dp))
                    }
                }
                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Next",
                    enabled = uiState.financeScreenButtonEnabled
                )
            }
            if (uiState.showCalender) {
                AppDatePicker(
                    show = uiState.showCalender,
                    selectedDate = DateTimeUtils.slashDateToEpochMillis(data.nominee_1_dob),
                    onDismiss = { handleEvent(TradingAccountEvent.HideCalender) },
                    onDateSelected = { dob ->
                        dob?.let {
                            handleEvent(TradingAccountEvent.OnNomineeDobChange(it))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HolderNature(selected: Holding, onHolderNatureChange: (Holding) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row {
            Text(
                "Holder nature/ " + "Insert stringResource here",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                color = HolderNatureTextColor
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Holding.entries.forEach {
                HoldingCard(
                    cardHeading = it.heading.substringBefore(" ") + "/ ", //TODO Use stringResource
                    icon = it.icon,
                    isSelected = it == selected,
                    onHolderNatureChange = { onHolderNatureChange(it) },
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }
}

@Composable
fun HoldingCard(
    cardHeading: String,
    isSelected: Boolean,
    icon: DrawableResource,
    onHolderNatureChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .genericDropShadow(RoundedCornerShape(15.dp))
            .border(
                Spacing.dp1,
                color = if (isSelected) SelectedBoxBorder else BoxBorder,
                RoundedCornerShape(Spacing.dp8)
            )
            .clip(RoundedCornerShape(Spacing.dp8))
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .clickable { onHolderNatureChange() }.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = if (isSelected) Primary else GreyText
        )
        Text(
            text = cardHeading,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = if (isSelected) Primary else GreyText
        )
    }
}

@Composable
fun CheckBoxComp(
    heading: String = "Nomination",
    subheading: String = "",
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedBoxColor = SelectedTextColor,
                uncheckedBoxColor = Color.White,
                checkedBorderColor = SelectedTextColor, uncheckedBorderColor = SelectedTextColor
            ), modifier = Modifier.clip(RoundedCornerShape(4.dp))
        )

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
            Text(
                heading,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
            )
            if (subheading.isNotEmpty()) {
                Text(
                    subheading,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                )
            }
        }
    }
}

@Composable
fun JointHolder(
    jointHolder: String,

    secondHolderFirstName: String,
    onSecondHolderFirstNameChange: (String) -> Unit,

    secondHolderPan: String,
    onSecondHolderPanChange: (String) -> Unit,

    secondHolderEmail: String,
    onSecondHolderEmailChange: (String) -> Unit,

    secondHolderMobile: String,
    onSecondHolderMobileChange: (String) -> Unit,

    secondHolderDOB: String,
    onSecondHolderDOBClick: (String) -> Unit,
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        BarHeader(
            title = jointHolder
        )

        TitledAppTextField(
            title = "Full Name",
            value = secondHolderFirstName,
            onValueChange = onSecondHolderFirstNameChange,
            placeholder = "Enter full Name",
            mandatory = true,
        )

        TitledAppTextField(
            title = "PAN",
            value = secondHolderPan,
            onValueChange = onSecondHolderPanChange,
            placeholder = "Enter PAN number",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text,
        )

        TitledAppTextField(
            title = "Email Address",
            value = secondHolderEmail,
            onValueChange = onSecondHolderEmailChange,
            placeholder = "Enter email address",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email
        )

        TitledAppTextField(
            title = "Mobile Number",
            value = secondHolderMobile,
            onValueChange = onSecondHolderMobileChange,
            placeholder = "Enter Mobile number",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
        )

        OnBoardingDateField(
            modifier = Modifier.fillMaxWidth(),
            value = secondHolderDOB,
            placeholder = "Select DOB",
            label = "Holder DOB",
            mandatory = true,
            onClick = {
                showDatePicker = true
            }
        )
    }

    if (showDatePicker) {
        AppDatePicker(
            show = showDatePicker,
            selectedDate = DateTimeUtils.slashDateToEpochMillis(secondHolderDOB),
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = {
                it?.let {
                    onSecondHolderDOBClick(DateTimeUtils.epochMillisToSlashDate(it))
                }
            }
        )
    }
}

@Composable
fun CountrySelectorDialog(
    showDialog: Boolean,
    selectedCode: String?,
    onDismiss: () -> Unit,
    onCountrySelected: (Country) -> Unit
) {
    if (!showDialog) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = "Select Country",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Country.entries) { country ->

                        val isSelected = selectedCode == country.code

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onCountrySelected(country)
                                    onDismiss()
                                }
                                .background(
                                    if (isSelected) {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    } else {
                                        Color.Transparent
                                    }
                                )
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "${country.displayName} (${country.code})",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )

                            if (isSelected) {
                                Icon(
                                    painter = painterResource(Res.drawable.tick_icon),
                                    contentDescription = null,
                                    tint = appGreen
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocalTopAppBarWithBackButtonAndStepCount(
    stepCount: Int,
    totalSteps: Int,
    onBack: () -> Unit,
    title: String = "",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            AppBackButton(onClick = onBack)
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            for (i in 1..totalSteps) {

                val progress by animateFloatAsState(
                    targetValue = when {
                        i < stepCount -> 1f
                        i == stepCount -> 1f
                        else -> 0f
                    },
                    label = "step_progress",
                    animationSpec = tween(
                        easing = FastOutSlowInEasing
                    )
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(Spacing.dp4)
                        .clip(LocalShapes.current.roundedDp12)
                        .background(GreyBox)

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .clip(LocalShapes.current.roundedDp12)
                            .background(Primary)
                    )
                }
            }
        }
        Text(
            "Step $stepCount of $totalSteps",
            style = MaterialTheme.typography.bodySmall,
            color = GreyText,
            modifier = Modifier.align(Alignment.End).padding(top = Spacing.dp4)
        )
    }
}




