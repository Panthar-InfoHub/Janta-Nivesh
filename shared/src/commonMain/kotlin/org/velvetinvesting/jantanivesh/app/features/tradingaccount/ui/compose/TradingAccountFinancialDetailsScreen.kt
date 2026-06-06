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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.add_more_joint_holder
import jantanivesh.shared.generated.resources.address_line_1_details
import jantanivesh.shared.generated.resources.address_line_2_details
import jantanivesh.shared.generated.resources.address_line_3_details
import jantanivesh.shared.generated.resources.city
import jantanivesh.shared.generated.resources.country
import jantanivesh.shared.generated.resources.date_of_birth
import jantanivesh.shared.generated.resources.delete_icon
import jantanivesh.shared.generated.resources.email_address
import jantanivesh.shared.generated.resources.full_name_as_per_pan
import jantanivesh.shared.generated.resources.full_name_placeholder
import jantanivesh.shared.generated.resources.holder_nature
import jantanivesh.shared.generated.resources.identity_type
import jantanivesh.shared.generated.resources.joint_holder2_icon
import jantanivesh.shared.generated.resources.kyc_form_aadhaar_number_label
import jantanivesh.shared.generated.resources.kyc_form_email_address_label
import jantanivesh.shared.generated.resources.location_icon
import jantanivesh.shared.generated.resources.mobile_number
import jantanivesh.shared.generated.resources.nomination_subheading
import jantanivesh.shared.generated.resources.nominee_1
import jantanivesh.shared.generated.resources.nominee_address
import jantanivesh.shared.generated.resources.occupation
import jantanivesh.shared.generated.resources.pan_number
import jantanivesh.shared.generated.resources.percentage_of_holding
import jantanivesh.shared.generated.resources.pincode
import jantanivesh.shared.generated.resources.plus_inside_circle_icon
import jantanivesh.shared.generated.resources.relation
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

@Preview(heightDp = 3000, showBackground = true, locale = "hi")
@Composable
fun TradingAccountFinancialDetailsPreview() {
    JantaNiveshTheme {
        TradingAccountFinancialDetailsScreen(
            pv = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            uiState = TradingAccountUiState(
                formState = UiState.Success(TradingAccountFormDomain(data = Data())),
                holderNature = Holding.JOINT
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
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(Spacing.dp10)
                                )
                                .padding(Spacing.dp16),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Occupation/ " + "(" + stringResource(Res.string.occupation) + ")",
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
                                data = data,
                                handleEvent = handleEvent,
                                jointHolder = "Joint Holder 2",
                            )
                        }
                        if (uiState.enableThirdHolder) {
                            item {
                                JointHolder(
                                    jointHolder = "Joint Holder 3",
                                    data = data,
                                    handleEvent = handleEvent,
                                )
                            }
                        }
                    } // joint holder if block end

                    item {
                        Column(
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                .clip(RoundedCornerShape(Spacing.dp24))
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(Spacing.dp10)
                                )
                                .padding(horizontal = Spacing.dp16, vertical = Spacing.dp24),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                        ) {
                            CheckBoxComp(
                                heading = "I wish to nominate a person for my account in the event of my death",
                                subheading = stringResource(Res.string.nomination_subheading),
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
                                    "Nominee 1/ " + "(" + stringResource(Res.string.nominee_1) + ")",
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
                                title = "Full Name/ " + "(" + stringResource(Res.string.full_name_placeholder) + ")",
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
                                    title = "Relation/ " + "(" + stringResource(Res.string.relation) + ")",
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
                                    label = "Date of Birth/ " + "(" + stringResource(Res.string.date_of_birth) + ")",
                                    mandatory = true,
                                    onClick = {
                                        handleEvent(TradingAccountEvent.ShowCalender)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            DropDownSelector(
                                title = "Identity Type/ " + "(" + stringResource(Res.string.identity_type) + ")",
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
                                title = "Mobile Number/ " + "(" + stringResource(Res.string.mobile_number) + ")",
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
                                        "Nominee Address/ " + "(" + stringResource(Res.string.nominee_address) + ")",
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
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Black
                            ),
                            modifier = Modifier.fillMaxWidth().dashedBorder(
                                color = SelectedBoxBorder,
                                dashLength = Spacing.dp2,
                                gapLength = Spacing.dp2
                            )
                        ) {
                            Text(
                                "+ Add Another Nominee",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(pv.calculateBottomPadding() + 16.dp))
                    }
                }
                if (uiState.holderNature == Holding.SINGLE) {
                    NextButtonFooter(
                        onClick = onClick,
                        pv = pv,
                        value = "Next",
                        enabled = uiState.financeScreenButtonEnabled
                    )
                } else {
                    Column {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(Spacing.dp8),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Black
                            ),
                            modifier = Modifier.fillMaxWidth().padding(Spacing.dp24).border(
                                width = Spacing.dp1,
                                shape = RoundedCornerShape(Spacing.dp8),
                                color = BoxBorder
                            )
                        ) {
                            Text(
                                "Back",
                                style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp),
                                color = Primary,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                        NextButtonFooter(
                            onClick = onClick,
                            pv = pv,
                            value = "Save & Continue",
                            enabled = uiState.financeScreenButtonEnabled
                        )
                    }
                }
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
                "Holder nature/ " + "(" + stringResource(Res.string.holder_nature) + ")",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                color = HolderNatureTextColor
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Holding.entries.forEach {
                HoldingCard(
                    cardHeading = it.heading.substringBefore(" ") + "/ " + "(" + stringResource(Res.string.holder_nature) + ")",
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
    data: Data,
    handleEvent: (TradingAccountEvent) -> Unit,
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .genericDropShadow(RoundedCornerShape(Spacing.dp16))
            .clip(RoundedCornerShape(Spacing.dp24))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(Spacing.dp10)
            )
            .padding(Spacing.dp24),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.joint_holder2_icon),
                contentDescription = "Second Joint Holder",
                modifier = Modifier.size(width = Spacing.dp20, height = Spacing.dp16).weight(0.2f),
                tint = Primary
            )
            Text(
                jointHolder,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Primary,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {}, // TODO add delete functionality
                modifier = Modifier.padding(end = Spacing.dp16).weight(0.2f)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.delete_icon),
                    contentDescription = "Delete",
                    modifier = Modifier.size(Spacing.dp15),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        HorizontalDivider(
            thickness = Spacing.dp1,
            color = Color(0xffDCE9FF),
        )

        TitledAppTextField(
            title = "Full Name (As per PAN)/ " + "(" + stringResource(Res.string.full_name_as_per_pan) + ")",
            value = data.second_holder_first_name,
            onValueChange = {
                handleEvent(
                    TradingAccountEvent.OnSecondFirstNameChange(
                        it
                    )
                )
            },
            placeholder = "Enter full Name",
            mandatory = true,
        )
        DropDownSelector(
            title = "Relation/ " + "(" + stringResource(Res.string.relation) + ")",
            value = NomineeRelationship.getDisplayNameFromCode(data.nominee_1_relationship)
                ?: "",
            onValueChange = { handleEvent(TradingAccountEvent.OnNominee1RelationChange(it.code)) },
            placeholder = "Relation",
            mandatory = true,
            list = NomineeRelationship.entries,
            textConvertor = {
                it.displayName
            }
        )


        OnBoardingDateField(
            modifier = Modifier.fillMaxWidth(),
            value = data.second_holder_dob,
            placeholder = "Select DOB",
            label = "Date of Birth/ " + "(" + stringResource(Res.string.date_of_birth) + ")",
            mandatory = true,
            onClick = {
                showDatePicker = true
            }
        )
        TitledAppTextField(
            title = "PAN Number/ " + "(" + stringResource(Res.string.pan_number) + ")",
            value = data.second_holder_pan,
            onValueChange = {
                handleEvent(
                    TradingAccountEvent.OnSecondPanChange(
                        it
                    )
                )
            },
            placeholder = "Enter PAN number",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text,
        )
        TitledAppTextField(
            title = "Email Address/ " + "(" + stringResource(Res.string.email_address) + ")",
            value = data.second_holder_email,
            onValueChange = {
                handleEvent(
                    TradingAccountEvent.OnSecondEmailChange(
                        it
                    )
                )
            },
            placeholder = "Enter email address",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email
        )

        TitledAppTextField(
            title = "Mobile Number/ " + "(" + stringResource(Res.string.mobile_number) + ")",
            value = data.second_holder_mobile,
            onValueChange = {
                handleEvent(
                    TradingAccountEvent.OnSecondDobChange(
                        it
                    )
                )
            },
            placeholder = "Enter Mobile number",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
        )
        TitledAppTextField(
            title = "Percentage of Holding(%)/ " + "(" + stringResource(Res.string.percentage_of_holding) + ")",
            value = "secondHolderMobile",
            onValueChange = { TODO() }, //TODO Implement this feature
            placeholder = "e.g. 50",
            mandatory = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
        )
        Column(
            modifier = Modifier.padding(top = Spacing.dp40)
                .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                .clip(RoundedCornerShape(Spacing.dp12))
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(Spacing.dp12)
                )
                .padding(Spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
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
                    "Address Details",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            TitledAppTextField(
                title = "Address Line 1 (Flat/House No., Building)/ " + "(" + stringResource(Res.string.address_line_1_details) + ")",
                value = data.nominee_1_address1, //todo adjust data
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
                title = "Address Line 2 (Street, Sector, Area)/ " + "(" + stringResource(Res.string.address_line_2_details) + ")",
                value = data.nominee_1_address2,     //todo adjust data
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
            TitledAppTextField(
                title = "Address Line 3 (Landmark)/ " + "(" + stringResource(Res.string.address_line_3_details) + ")",
                value = data.nominee_1_address2,     //todo adjust data
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
            TitledAppTextField(
                title = "City/ " + "(" + stringResource(Res.string.city) + ")",
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
                keyboardType = KeyboardType.Text,
            )


            TitledAppTextField(
                title = "Pincode/ " + "(" + stringResource(Res.string.pincode) + ")",
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
                keyboardType = KeyboardType.Text,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
            ) {
            }
            DropDownSelector(
                title = "Country/ " + "(" + stringResource(Res.string.country) + ")",
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
        Button(
            onClick = {},
            shape = RoundedCornerShape(Spacing.dp8),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Black
            ),
            modifier = Modifier.fillMaxWidth().dashedBorder(
                strokeWidth = Spacing.dp2,
                color = SelectedBoxBorder,
                dashLength = Spacing.dp4,
                gapLength = Spacing.dp4
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(
                        Res.drawable.plus_inside_circle_icon
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(Spacing.dp20),
                    tint = Primary
                )

                Text(
                    "Add More Joint Holder/ " + "(" + stringResource(Res.string.add_more_joint_holder) + ")",
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp),
                    color = Primary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (showDatePicker) {
        AppDatePicker(
            show = showDatePicker,
            selectedDate = DateTimeUtils.slashDateToEpochMillis(data.second_holder_dob),
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = {
                it?.let {
                    handleEvent(
                        TradingAccountEvent.OnSecondDobChange(
                            DateTimeUtils.epochMillisToSlashDate(
                                it
                            )
                        )
                    )
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
