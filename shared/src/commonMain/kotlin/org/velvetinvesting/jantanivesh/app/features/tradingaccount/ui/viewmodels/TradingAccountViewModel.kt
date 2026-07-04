package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.BrowserLauncher
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.ClientType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.DefaultDp
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.FatcaOccupationType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.KycType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.SourceOfWealth
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.StateCode
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.TaxStatus
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.OccupationSourceOfWealthMapper
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases.GetTradingAccountPrefilledDataUseCase
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases.SubmitTradingAccountFormUseCase
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases.TradingAccountConfirmationUseCase
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases.VerifyPANUseCase
import kotlin.time.Clock
import kotlin.time.Instant

data class TradingAccountUiState(
    val formState: UiState<TradingAccountFormDomain> = UiState.Loading,
    val launchedBrowser: Boolean = false,
    val isMinor: Boolean = false,
    val panVerified: Boolean = false,
    val verifiedPanNumber: String = "",
    val holderNature: Holding = Holding.SINGLE,
    val enableThirdHolder: Boolean = false,
    val nomineeChecked: Boolean = false,
    val showCalender: Boolean = false,
    val visibleBankAccounts: List<Int> = listOf(1),
    val reEnteredAccountNumbers: List<String> = List(5) { "" },
    val basicDetailsNextEnabled: Boolean = false,
    val bankScreenButtonEnabled: Boolean = false,
    val clientScreenButtonEnabled: Boolean = false,
    val financeScreenButtonEnabled: Boolean = false,
    val addressScreenButtonEnabled: Boolean = false,
    val guardianScreenButtonEnabled: Boolean = false,
    val totalSteps: Int = 0,
    val showStateDialog: Boolean = false,
    val showCountryDialog: Boolean = false,
    val showForeignCountryDialog: Boolean = false,
)

sealed interface TradingAccountEvent {
    object GetUserData : TradingAccountEvent
    data class SubmitForm(val onSuccessfulSubmit: () -> Unit) : TradingAccountEvent
    data class ConfirmAccount(val onSuccessfulSubmit: () -> Unit) : TradingAccountEvent
    data class VerifyPan(val pan: String) : TradingAccountEvent

    // Primary Holder
    data class OnFirstNameChange(val value: String) : TradingAccountEvent
    data class OnMiddleNameChange(val value: String) : TradingAccountEvent
    data class OnLastNameChange(val value: String) : TradingAccountEvent
    data class OnPanChange(val value: String) : TradingAccountEvent
    data class OnDobChange(val value: String) : TradingAccountEvent
    data class OnGenderChange(val value: String) : TradingAccountEvent
    data class OnEmailChange(val value: String) : TradingAccountEvent
    data class OnPhoneChange(val value: String) : TradingAccountEvent
    data class OnTaxStatusChange(val value: String) : TradingAccountEvent
    data class OnOccupationChange(val value: String) : TradingAccountEvent
    data class OnOccTypeChange(val value: String) : TradingAccountEvent
    data class OnPlaceOfBirthChange(val value: String) : TradingAccountEvent
    data class OnPrimaryCkycChange(val value: String) : TradingAccountEvent
    data class OnPrimaryKycTypeChange(val value: String) : TradingAccountEvent
    data class OnPrimaryPanExemptChange(val value: String) : TradingAccountEvent
    data class OnPrimaryKraExemptRefChange(val value: String) : TradingAccountEvent
    data class OnPrimaryExemptCategoryChange(val value: String) : TradingAccountEvent

    // Joint Holder
    data class OnHoldingNatureChange(val value: String) : TradingAccountEvent
    data class OnHolderNatureChangeUi(val value: Holding) : TradingAccountEvent
    object AddThirdHolder : TradingAccountEvent
    object RemoveThirdHolder : TradingAccountEvent
    data class OnSecondFirstNameChange(val value: String) : TradingAccountEvent
    data class OnSecondMiddleNameChange(val value: String) : TradingAccountEvent
    data class OnSecondLastNameChange(val value: String) : TradingAccountEvent
    data class OnSecondPanChange(val value: String) : TradingAccountEvent
    data class OnSecondDobChange(val value: String) : TradingAccountEvent
    data class OnSecondEmailChange(val value: String) : TradingAccountEvent
    data class OnSecondMobileChange(val value: String) : TradingAccountEvent
    data class OnSecondCkycChange(val value: String) : TradingAccountEvent
    data class OnSecondKycTypeChange(val value: String) : TradingAccountEvent
    data class OnSecondPanExemptChange(val value: String) : TradingAccountEvent
    data class OnSecondExemptCategoryChange(val value: String) : TradingAccountEvent
    data class OnSecondEmailDeclChange(val value: String) : TradingAccountEvent
    data class OnSecondMobileDeclChange(val value: String) : TradingAccountEvent

    data class OnThirdFirstNameChange(val value: String) : TradingAccountEvent
    data class OnThirdMiddleNameChange(val value: String) : TradingAccountEvent
    data class OnThirdLastNameChange(val value: String) : TradingAccountEvent
    data class OnThirdPanChange(val value: String) : TradingAccountEvent
    data class OnThirdDobChange(val value: String) : TradingAccountEvent
    data class OnThirdEmailChange(val value: String) : TradingAccountEvent
    data class OnThirdMobileChange(val value: String) : TradingAccountEvent
    data class OnThirdCkycChange(val value: String) : TradingAccountEvent
    data class OnThirdKycTypeChange(val value: String) : TradingAccountEvent
    data class OnThirdPanExemptChange(val value: String) : TradingAccountEvent
    data class OnThirdExemptCategoryChange(val value: String) : TradingAccountEvent
    data class OnThirdEmailDeclChange(val value: String) : TradingAccountEvent
    data class OnThirdMobileDeclChange(val value: String) : TradingAccountEvent

    // Guardian
    data class OnGuardianRelationChange(val value: String) : TradingAccountEvent
    data class OnGuardianFirstNameChange(val value: String) : TradingAccountEvent
    data class OnGuardianMiddleNameChange(val value: String) : TradingAccountEvent
    data class OnGuardianLastNameChange(val value: String) : TradingAccountEvent
    data class OnGuardianPanChange(val value: String) : TradingAccountEvent
    data class OnGuardianDobChange(val value: String) : TradingAccountEvent
    data class OnGuardianCkycChange(val value: String) : TradingAccountEvent
    data class OnGuardianKycTypeChange(val value: String) : TradingAccountEvent
    data class OnGuardianPanExemptChange(val value: String) : TradingAccountEvent
    data class OnGuardianExemptCategoryChange(val value: String) : TradingAccountEvent
    data class OnGuardianExemptRefNoChange(val value: String) : TradingAccountEvent

    // Nominee
    data class OnNomineeCheckedChange(val value: Boolean) : TradingAccountEvent
    object ShowCalender : TradingAccountEvent
    object HideCalender : TradingAccountEvent
    data class OnNomineeDobChange(val dob: Long) : TradingAccountEvent
    data class OnNominee1NameChange(val value: String) : TradingAccountEvent
    data class OnNominee1RelationChange(val value: String) : TradingAccountEvent
    data class OnNominee1DobChange(val value: String) : TradingAccountEvent
    data class OnNominee1EmailChange(val value: String) : TradingAccountEvent
    data class OnNominee1MobileChange(val value: String) : TradingAccountEvent
    data class OnNominee1IdentityTypeChange(val value: String) : TradingAccountEvent
    data class OnNominee1IdentityNumberChange(val value: String) : TradingAccountEvent
    data class OnNominee1Address1Change(val value: String) : TradingAccountEvent
    data class OnNominee1Address2Change(val value: String) : TradingAccountEvent
    data class OnNominee1Address3Change(val value: String) : TradingAccountEvent
    data class OnNominee1CityChange(val value: String) : TradingAccountEvent
    data class OnNominee1PincodeChange(val value: String) : TradingAccountEvent
    data class OnNominee1CountryChange(val value: String) : TradingAccountEvent
    data class OnNominee1MinorFlagChange(val value: String) : TradingAccountEvent
    data class OnNominee1GuardianChange(val value: String) : TradingAccountEvent
    data class OnNominee1GuardianPanChange(val value: String) : TradingAccountEvent
    data class OnNominee1ApplicableChange(val value: String) : TradingAccountEvent
    data class OnNomineeSoaChange(val value: String) : TradingAccountEvent

    // Bank
    object AddBankAccount : TradingAccountEvent
    data class RemoveBankAccount(val index: Int) : TradingAccountEvent
    data class OnAccountTypeChange(val index: Int, val value: String) : TradingAccountEvent
    data class OnAccountNumberChange(val index: Int, val value: String) : TradingAccountEvent
    data class OnIfscChange(val index: Int, val value: String) : TradingAccountEvent
    data class OnMicrChange(val index: Int, val value: String) : TradingAccountEvent
    data class OnDefaultBankChange(val index: Int, val value: String) : TradingAccountEvent
    data class OnReEnteredAccountNumberChange(
        val index: Int,
        val value: String
    ) : TradingAccountEvent

    data class OnAccountType1Change(val value: String) : TradingAccountEvent
    data class OnAccountType2Change(val value: String) : TradingAccountEvent
    data class OnAccountType3Change(val value: String) : TradingAccountEvent
    data class OnAccountType4Change(val value: String) : TradingAccountEvent
    data class OnAccountType5Change(val value: String) : TradingAccountEvent
    data class OnAccountNumber1Change(val value: String) : TradingAccountEvent
    data class OnAccountNumber2Change(val value: String) : TradingAccountEvent
    data class OnAccountNumber3Change(val value: String) : TradingAccountEvent
    data class OnAccountNumber4Change(val value: String) : TradingAccountEvent
    data class OnAccountNumber5Change(val value: String) : TradingAccountEvent
    data class OnIfscCode1Change(val value: String) : TradingAccountEvent
    data class OnIfscCode2Change(val value: String) : TradingAccountEvent
    data class OnIfscCode3Change(val value: String) : TradingAccountEvent
    data class OnIfscCode4Change(val value: String) : TradingAccountEvent
    data class OnIfscCode5Change(val value: String) : TradingAccountEvent
    data class OnMicrNo1Change(val value: String) : TradingAccountEvent
    data class OnMicrNo2Change(val value: String) : TradingAccountEvent
    data class OnMicrNo3Change(val value: String) : TradingAccountEvent
    data class OnMicrNo4Change(val value: String) : TradingAccountEvent
    data class OnMicrNo5Change(val value: String) : TradingAccountEvent
    data class OnDefaultBankFlag1Change(val value: String) : TradingAccountEvent
    data class OnDefaultBankFlag2Change(val value: String) : TradingAccountEvent
    data class OnDefaultBankFlag3Change(val value: String) : TradingAccountEvent
    data class OnDefaultBankFlag4Change(val value: String) : TradingAccountEvent
    data class OnDefaultBankFlag5Change(val value: String) : TradingAccountEvent

    // Address
    data class OnAddress1Change(val value: String) : TradingAccountEvent
    data class OnAddress2Change(val value: String) : TradingAccountEvent
    data class OnAddress3Change(val value: String) : TradingAccountEvent
    data class OnCityChange(val value: String) : TradingAccountEvent
    data class OnStateChange(val value: String) : TradingAccountEvent
    data class OnPincodeChange(val value: String) : TradingAccountEvent
    data class OnCountryChange(val value: String) : TradingAccountEvent

    // Client/Demat
    data class OnClientTypeChangeUi(val clientType: ClientType) : TradingAccountEvent
    data class OnDefaultDpChangeUi(val defaultDp: DefaultDp) : TradingAccountEvent
    data class OnClientTypeChange(val value: String) : TradingAccountEvent
    data class OnPmsChange(val value: String) : TradingAccountEvent
    data class OnDefaultDpChange(val value: String) : TradingAccountEvent
    data class OnCdslDpidChange(val value: String) : TradingAccountEvent
    data class OnCdslCltidChange(val value: String) : TradingAccountEvent
    data class OnNsdlDpidChange(val value: String) : TradingAccountEvent
    data class OnNsdlCltidChange(val value: String) : TradingAccountEvent
    data class OnCmbpIdChange(val value: String) : TradingAccountEvent

    // Declarations
    data class OnNominationOptChange(val value: String) : TradingAccountEvent
    data class OnNominationAuthChange(val value: String) : TradingAccountEvent
    data class OnDivPayModeChange(val value: String) : TradingAccountEvent
    data class OnAadhaarUpdatedChange(val value: String) : TradingAccountEvent
    data class OnChequeNameChange(val value: String) : TradingAccountEvent
    data class OnCommunicationModeChange(val value: String) : TradingAccountEvent
    data class OnEmailDeclFlagChange(val value: String) : TradingAccountEvent
    data class OnMobileDeclFlagChange(val value: String) : TradingAccountEvent
    data class OnPaperlessFlagChange(val value: String) : TradingAccountEvent
    data class OnLeiNoChange(val value: String) : TradingAccountEvent
    data class OnLeiValidityChange(val value: String) : TradingAccountEvent
    data class OnMapinIdChange(val value: String) : TradingAccountEvent
    data class OnSourceWealthChange(val value: String) : TradingAccountEvent

    // Foreign Address
    data class OnForeignAddress1Change(val value: String) : TradingAccountEvent
    data class OnForeignAddress2Change(val value: String) : TradingAccountEvent
    data class OnForeignAddress3Change(val value: String) : TradingAccountEvent
    data class OnForeignCityChange(val value: String) : TradingAccountEvent
    data class OnForeignStateChange(val value: String) : TradingAccountEvent
    data class OnForeignPincodeChange(val value: String) : TradingAccountEvent
    data class OnForeignCountryChange(val value: String) : TradingAccountEvent
    data class OnForeignPhoneChange(val value: String) : TradingAccountEvent
    data class OnForeignFaxChange(val value: String) : TradingAccountEvent
    data class OnForeignOfficePhoneChange(val value: String) : TradingAccountEvent
    data class OnForeignOfficeFaxChange(val value: String) : TradingAccountEvent
    data class OnResiPhoneChange(val value: String) : TradingAccountEvent
    data class OnResiFaxChange(val value: String) : TradingAccountEvent
    data class OnOfficePhoneChange(val value: String) : TradingAccountEvent
    data class OnOfficeFaxChange(val value: String) : TradingAccountEvent

    object ShowStateDialog : TradingAccountEvent
    object HideStateDialog : TradingAccountEvent

    object ShowCountryDialog : TradingAccountEvent
    object HideCountryDialog : TradingAccountEvent

    object ShowForeignCountryDialog : TradingAccountEvent
    object HideForeignCountryDialog : TradingAccountEvent
}

sealed interface TradingAccountEffect {
    // Add effects as needed, e.g., navigation or snackbars if moved from direct controller usage
}

class TradingAccountViewModel(
    private val getTradingAccountPrefilledDataUseCase: GetTradingAccountPrefilledDataUseCase,
    private val verifyPANUseCase: VerifyPANUseCase,
    private val submitTradingAccountFormUseCase: SubmitTradingAccountFormUseCase,
    private val tradingAccountConfirmationUseCase: TradingAccountConfirmationUseCase,
    private val openBrowserLauncher: BrowserLauncher
) : ViewModel() {

    private val _uiState = MutableStateFlow(TradingAccountUiState())
    val uiState: StateFlow<TradingAccountUiState> = _uiState.asStateFlow()

    init {
        handleEvent(TradingAccountEvent.GetUserData)
    }

    private fun updateData(update: (Data) -> Data) {
        _uiState.update { currentState ->
            val updatedFormState = when (val formState = currentState.formState) {
                is UiState.Success -> {
                    formState.copy(
                        data = formState.data.copy(
                            data = update(formState.data.data)
                        )
                    )
                }

                else -> formState
            }
            calculateDerivedState(currentState.copy(formState = updatedFormState))
        }
    }

    fun handleEvent(event: TradingAccountEvent) {
        when (event) {
            TradingAccountEvent.GetUserData -> getUserData()
            is TradingAccountEvent.SubmitForm -> submitForm(event.onSuccessfulSubmit)
            is TradingAccountEvent.ConfirmAccount -> confirmAccount(event.onSuccessfulSubmit)
            is TradingAccountEvent.VerifyPan -> verifyPan(event.pan)
            is TradingAccountEvent.OnFirstNameChange -> updateData {
                it.copy(
                    primary_holder_first_name = event.value.toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnMiddleNameChange -> updateData {
                it.copy(
                    primary_holder_middle_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnLastNameChange -> updateData {
                it.copy(
                    primary_holder_last_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnPanChange -> updateData {
                it.copy(
                    primary_holder_pan = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnDobChange -> {
                updateData {
                    it.copy(
                        primary_holder_dob_incorporation = event.value.trim()
                            .toUpperCase(Locale.current)
                    )
                }
                _uiState.update { it.copy(isMinor = isMinor(event.value)) }
            }

            is TradingAccountEvent.OnGenderChange -> updateData {
                it.copy(
                    gender = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnEmailChange -> updateData { it.copy(email = event.value.trim()) }
            is TradingAccountEvent.OnPhoneChange -> {
                if (event.value.length > 10) return

                updateData {
                    it.copy(
                        indian_mobile_no = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnTaxStatusChange -> updateData {
                it.copy(
                    tax_status = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnOccupationChange -> {

                val occupation = event.value.trim().toUpperCase(Locale.current)

                val sourceOfWealth =
                    OccupationSourceOfWealthMapper.getSourceOfWealthCode(occupation) ?: ""

                updateData {
                    it.copy(
                        occupation_code = occupation,
                        srce_wealt = sourceOfWealth,
                        occ_type = OccupationSourceOfWealthMapper
                            .getFatcaOccupationTypeCode(sourceOfWealth)
                    )
                }
            }

            is TradingAccountEvent.OnOccTypeChange -> updateData {
                it.copy(
                    occ_type = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnPlaceOfBirthChange -> updateData {
                it.copy(
                    po_bir_inc = event.value.toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnPrimaryCkycChange -> updateData {
                it.copy(
                    primary_holder_ckyc_number = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnPrimaryKycTypeChange -> updateData {
                it.copy(
                    primary_holder_kyc_type = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnPrimaryPanExemptChange -> updateData {
                it.copy(
                    primary_holder_pan_exempt = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnPrimaryKraExemptRefChange -> updateData {
                it.copy(
                    primary_holder_kra_exempt_ref_no = event.value.trim()
                        .toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnPrimaryExemptCategoryChange -> updateData {
                it.copy(
                    primary_holder_exempt_category = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnHoldingNatureChange -> updateData { it.copy(holding_nature = event.value) }
            is TradingAccountEvent.OnHolderNatureChangeUi -> {
                _uiState.update { it.copy(holderNature = event.value) }
                handleEvent(TradingAccountEvent.OnHoldingNatureChange(event.value.code))
                if (event.value == Holding.SINGLE) {
                    resetJointHolderData()
                }
                if (event.value == Holding.JOINT) {
                    updateData { it.copy(second_holder_pan_exempt = "N") }
                }
            }

            TradingAccountEvent.AddThirdHolder -> {
                _uiState.update { it.copy(enableThirdHolder = true) }
                updateData { it.copy(third_holder_pan_exempt = "N") }
            }

            TradingAccountEvent.RemoveThirdHolder -> {
                _uiState.update { it.copy(enableThirdHolder = false) }
                resetThirdHolder()
            }

            is TradingAccountEvent.OnSecondFirstNameChange -> updateData {
                it.copy(
                    second_holder_first_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondMiddleNameChange -> updateData {
                it.copy(
                    second_holder_middle_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondLastNameChange -> updateData {
                it.copy(
                    second_holder_last_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondPanChange -> updateData {
                it.copy(
                    second_holder_pan = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondDobChange -> updateData {
                it.copy(
                    second_holder_dob = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondEmailChange -> updateData {
                it.copy(
                    second_holder_email = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondMobileChange -> {
                if (event.value.length > 10) return

                updateData {
                    it.copy(
                        second_holder_mobile = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnSecondCkycChange -> updateData {
                it.copy(
                    second_holder_ckyc_number = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondKycTypeChange -> updateData {
                it.copy(
                    second_holder_kyc_type = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondPanExemptChange -> updateData {
                it.copy(
                    second_holder_pan_exempt = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondExemptCategoryChange -> updateData {
                it.copy(
                    second_holder_exempt_category = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondEmailDeclChange -> updateData {
                it.copy(
                    second_holder_email_declaration = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSecondMobileDeclChange -> updateData {
                it.copy(
                    second_holder_mobile_declaration = event.value.trim()
                        .toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdFirstNameChange -> updateData {
                it.copy(
                    third_holder_first_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdMiddleNameChange -> updateData {
                it.copy(
                    third_holder_middle_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdLastNameChange -> updateData {
                it.copy(
                    third_holder_last_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdPanChange -> updateData {
                it.copy(
                    third_holder_pan = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdDobChange -> updateData {
                it.copy(
                    third_holder_dob = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdEmailChange -> updateData {
                it.copy(
                    third_holder_email = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdMobileChange -> {
                if (event.value.length > 10) return

                updateData {
                    it.copy(
                        third_holder_mobile = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnThirdCkycChange -> updateData {
                it.copy(
                    third_holder_ckyc_number = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdKycTypeChange -> updateData {
                it.copy(
                    third_holder_kyc_type = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdPanExemptChange -> updateData {
                it.copy(
                    third_holder_pan_exempt = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdExemptCategoryChange -> updateData {
                it.copy(
                    third_holder_exempt_category = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdEmailDeclChange -> updateData {
                it.copy(
                    third_holder_email_declaration = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnThirdMobileDeclChange -> updateData {
                it.copy(
                    third_holder_mobile_declaration = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianRelationChange -> updateData {
                it.copy(
                    guardian_relation = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianFirstNameChange -> updateData {
                it.copy(
                    guardian_first_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianMiddleNameChange -> updateData {
                it.copy(
                    guardian_middle_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianLastNameChange -> updateData {
                it.copy(
                    guardian_last_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianPanChange -> updateData {
                it.copy(
                    guardian_pan = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianDobChange -> updateData {
                it.copy(
                    guardian_dob = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianCkycChange -> updateData {
                it.copy(
                    guardian_ckyc_number = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianKycTypeChange -> updateData {
                it.copy(
                    guardian_kyc_type = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianPanExemptChange -> updateData {
                it.copy(
                    guardian_pan_exempt = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianExemptCategoryChange -> updateData {
                it.copy(
                    guardian_exempt_category = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnGuardianExemptRefNoChange -> updateData {
                it.copy(
                    guardian_exempt_ref_no = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNomineeCheckedChange -> {
                _uiState.update { it.copy(nomineeChecked = event.value) }
                handleEvent(TradingAccountEvent.OnNominationOptChange(if (event.value) "Y" else "N"))
                handleEvent(TradingAccountEvent.OnNominationAuthChange(""))
            }

            TradingAccountEvent.ShowCalender -> _uiState.update { it.copy(showCalender = true) }
            TradingAccountEvent.HideCalender -> _uiState.update { it.copy(showCalender = false) }
            is TradingAccountEvent.OnNomineeDobChange -> {
                handleEvent(
                    TradingAccountEvent.OnNominee1DobChange(
                        DateTimeUtils.epochMillisToSlashDate(
                            event.dob
                        )
                    )
                )
                val isMinor = DateTimeUtils.epochMillisToIsoUtc(event.dob)?.let { isMinor(it) }
                handleEvent(TradingAccountEvent.OnNominee1MinorFlagChange(if (isMinor == true) "Y" else "N"))
            }

            is TradingAccountEvent.OnNominee1NameChange -> updateData {
                it.copy(
                    nominee_1_name = event.value.toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1RelationChange -> updateData {
                it.copy(
                    nominee_1_relationship = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1DobChange -> updateData {
                it.copy(
                    nominee_1_dob = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1EmailChange -> updateData {
                it.copy(
                    nominee_1_email = event.value.trim()
                )
            }

            is TradingAccountEvent.OnNominee1MobileChange -> {
                if (event.value.length > 10) return

                updateData {
                    it.copy(
                        nominee_1_mobile = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnNominee1IdentityTypeChange -> updateData {
                it.copy(
                    nominee_1_identity_type = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1IdentityNumberChange -> updateData {
                it.copy(
                    nominee_1_identity_number = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1Address1Change -> updateData {
                it.copy(
                    nominee_1_address1 = event.value.toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1Address2Change -> updateData {
                it.copy(
                    nominee_1_address2 = event.value.toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1Address3Change -> updateData {
                it.copy(
                    nominee_1_address3 = event.value.toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1CityChange -> updateData {
                it.copy(
                    nominee_1_city = event.value.toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1PincodeChange -> {

                if (event.value.length > 6) return

                updateData {
                    it.copy(
                        nominee_1_pin = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnNominee1CountryChange -> updateData {
                it.copy(
                    nominee_1_country = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1MinorFlagChange -> updateData {
                it.copy(
                    nominee_1_minor_flag = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1GuardianChange -> updateData {
                it.copy(
                    nominee_1_guardian = event.value.toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1GuardianPanChange -> updateData {
                it.copy(
                    nominee_1_guardian_pan = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominee1ApplicableChange -> updateData {
                it.copy(
                    nominee_1_applicable = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNomineeSoaChange -> updateData {
                it.copy(
                    nominee_soa = event.value.trim().toUpperCase(Locale.current)
                )
            }

            TradingAccountEvent.AddBankAccount -> {
            val current = _uiState.value.visibleBankAccounts

            if (current.size >= 5) return

            val next = (1..5).firstOrNull { it !in current } ?: return

            _uiState.update {
                calculateDerivedState(
                    it.copy(
                        visibleBankAccounts = current + next
                    )
                )
            }
        }

            is TradingAccountEvent.RemoveBankAccount -> {
                if (event.index == 1) return

                _uiState.update {
                    calculateDerivedState(
                        it.copy(
                            visibleBankAccounts = it.visibleBankAccounts - event.index,
                            reEnteredAccountNumbers =
                                it.reEnteredAccountNumbers.toMutableList().apply {
                                    this[event.index - 1] = ""
                                }
                        )
                    )
                }

                resetBankAccount(event.index)
            }

            is TradingAccountEvent.OnAccountTypeChange -> {
                when (event.index) {
                    1 -> handleEvent(TradingAccountEvent.OnAccountType1Change(event.value))
                    2 -> handleEvent(TradingAccountEvent.OnAccountType2Change(event.value))
                    3 -> handleEvent(TradingAccountEvent.OnAccountType3Change(event.value))
                    4 -> handleEvent(TradingAccountEvent.OnAccountType4Change(event.value))
                    5 -> handleEvent(TradingAccountEvent.OnAccountType5Change(event.value))
                }
            }

            is TradingAccountEvent.OnAccountNumberChange -> {
                when (event.index) {
                    1 -> handleEvent(TradingAccountEvent.OnAccountNumber1Change(event.value))
                    2 -> handleEvent(TradingAccountEvent.OnAccountNumber2Change(event.value))
                    3 -> handleEvent(TradingAccountEvent.OnAccountNumber3Change(event.value))
                    4 -> handleEvent(TradingAccountEvent.OnAccountNumber4Change(event.value))
                    5 -> handleEvent(TradingAccountEvent.OnAccountNumber5Change(event.value))
                }
            }

            is TradingAccountEvent.OnIfscChange -> {
                when (event.index) {
                    1 -> handleEvent(TradingAccountEvent.OnIfscCode1Change(event.value))
                    2 -> handleEvent(TradingAccountEvent.OnIfscCode2Change(event.value))
                    3 -> handleEvent(TradingAccountEvent.OnIfscCode3Change(event.value))
                    4 -> handleEvent(TradingAccountEvent.OnIfscCode4Change(event.value))
                    5 -> handleEvent(TradingAccountEvent.OnIfscCode5Change(event.value))
                }
            }

            is TradingAccountEvent.OnMicrChange -> {
                when (event.index) {
                    1 -> handleEvent(TradingAccountEvent.OnMicrNo1Change(event.value))
                    2 -> handleEvent(TradingAccountEvent.OnMicrNo2Change(event.value))
                    3 -> handleEvent(TradingAccountEvent.OnMicrNo3Change(event.value))
                    4 -> handleEvent(TradingAccountEvent.OnMicrNo4Change(event.value))
                    5 -> handleEvent(TradingAccountEvent.OnMicrNo5Change(event.value))
                }
            }

            is TradingAccountEvent.OnDefaultBankChange -> {
                when (event.index) {
                    1 -> handleEvent(TradingAccountEvent.OnDefaultBankFlag1Change(event.value))
                    2 -> handleEvent(TradingAccountEvent.OnDefaultBankFlag2Change(event.value))
                    3 -> handleEvent(TradingAccountEvent.OnDefaultBankFlag3Change(event.value))
                    4 -> handleEvent(TradingAccountEvent.OnDefaultBankFlag4Change(event.value))
                    5 -> handleEvent(TradingAccountEvent.OnDefaultBankFlag5Change(event.value))
                }
            }

            is TradingAccountEvent.OnReEnteredAccountNumberChange -> {
                _uiState.update {
                    calculateDerivedState(
                        it.copy(
                            reEnteredAccountNumbers =
                                it.reEnteredAccountNumbers.toMutableList().apply {
                                    this[event.index - 1] = event.value
                                }
                        )
                    )
                }
            }

            is TradingAccountEvent.OnAccountType1Change -> updateData {
                it.copy(
                    account_type_1 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAccountNumber1Change -> updateData {
                it.copy(
                    account_no_1 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnIfscCode1Change -> updateData {
                it.copy(
                    ifsc_code_1 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnMicrNo1Change -> updateData {
                it.copy(
                    micr_no_1 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnDefaultBankFlag1Change -> updateData {
                it.copy(
                    default_bank_flag_1 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAccountType2Change -> updateData {
                it.copy(
                    account_type_2 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAccountType3Change -> updateData {
                it.copy(
                    account_type_3 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAccountType4Change -> updateData {
                it.copy(
                    account_type_4 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAccountType5Change -> updateData {
                it.copy(
                    account_type_5 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAccountNumber2Change -> updateData {
                it.copy(
                    account_no_2 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAccountNumber3Change -> updateData {
                it.copy(
                    account_no_3 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAccountNumber4Change -> updateData {
                it.copy(
                    account_no_4 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAccountNumber5Change -> updateData {
                it.copy(
                    account_no_5 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnIfscCode2Change -> updateData {
                it.copy(
                    ifsc_code_2 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnIfscCode3Change -> updateData {
                it.copy(
                    ifsc_code_3 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnIfscCode4Change -> updateData {
                it.copy(
                    ifsc_code_4 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnIfscCode5Change -> updateData {
                it.copy(
                    ifsc_code_5 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnMicrNo2Change -> updateData {
                it.copy(
                    micr_no_2 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnMicrNo3Change -> updateData {
                it.copy(
                    micr_no_3 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnMicrNo4Change -> updateData {
                it.copy(
                    micr_no_4 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnMicrNo5Change -> updateData {
                it.copy(
                    micr_no_5 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnDefaultBankFlag2Change -> updateData {
                it.copy(
                    default_bank_flag_2 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnDefaultBankFlag3Change -> updateData {
                it.copy(
                    default_bank_flag_3 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnDefaultBankFlag4Change -> updateData {
                it.copy(
                    default_bank_flag_4 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnDefaultBankFlag5Change -> updateData {
                it.copy(
                    default_bank_flag_5 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAddress1Change -> updateData {
                it.copy(
                    address_1 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAddress2Change -> updateData {
                it.copy(
                    address_2 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAddress3Change -> updateData {
                it.copy(
                    address_3 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnCityChange -> updateData {
                it.copy(
                    city = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnStateChange -> updateData {
                it.copy(
                    state = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnPincodeChange -> {
                if (event.value.length > 6) return

                updateData {
                    it.copy(
                        pincode = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnCountryChange -> updateData {
                it.copy(
                    country = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnClientTypeChangeUi -> {
                handleEvent(TradingAccountEvent.OnClientTypeChange(event.clientType.code))
                resetClientTypeDependentFields(event.clientType)
            }

            is TradingAccountEvent.OnDefaultDpChangeUi -> {
                handleEvent(TradingAccountEvent.OnDefaultDpChange(event.defaultDp.code))
                resetDefaultDpDependentFields(event.defaultDp)
            }

            is TradingAccountEvent.OnClientTypeChange -> updateData {
                it.copy(
                    client_type = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnPmsChange -> updateData {
                it.copy(
                    pms = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnDefaultDpChange -> updateData {
                it.copy(
                    default_dp = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnCdslDpidChange -> updateData {
                it.copy(
                    cdsl_dpid = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnCdslCltidChange -> updateData {
                it.copy(
                    cdslcltid = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNsdlDpidChange -> updateData {
                it.copy(
                    nsdldpid = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNsdlCltidChange -> updateData {
                it.copy(
                    nsdlcltid = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnCmbpIdChange -> updateData {
                it.copy(
                    cmbp_id = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominationOptChange -> updateData {
                it.copy(
                    nomination_opt = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnNominationAuthChange -> updateData {
                it.copy(
                    nomination_authentication = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnDivPayModeChange -> updateData {
                it.copy(
                    div_pay_mode = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnAadhaarUpdatedChange -> updateData {
                it.copy(
                    aadhaar_updated = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnChequeNameChange -> updateData {
                it.copy(
                    cheque_name = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnCommunicationModeChange -> updateData {
                it.copy(
                    communication_mode = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnEmailDeclFlagChange -> updateData {
                it.copy(
                    email_declaration_flag = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnMobileDeclFlagChange -> updateData {
                it.copy(
                    mobile_declaration_flag = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnPaperlessFlagChange -> updateData {
                it.copy(
                    paperless_flag = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnLeiNoChange -> updateData {
                it.copy(
                    lei_no = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnLeiValidityChange -> updateData {
                it.copy(
                    lei_validity = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnMapinIdChange -> updateData {
                it.copy(
                    mapin_id = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnSourceWealthChange -> {
                val value = event.value.trim()
                val occType = when (value) {
                    SourceOfWealth.SALARY.code ->
                        FatcaOccupationType.SERVICE.code

                    SourceOfWealth.BUSINESS_INCOME.code ->
                        FatcaOccupationType.BUSINESS.code

                    SourceOfWealth.GIFT.code,
                    SourceOfWealth.ANCESTRAL_PROPERTY.code,
                    SourceOfWealth.RENTAL_INCOME.code,
                    SourceOfWealth.PRIZE_MONEY.code,
                    SourceOfWealth.ROYALTY.code,
                    SourceOfWealth.OTHER.code ->
                        FatcaOccupationType.OTHERS.code

                    else ->
                        FatcaOccupationType.NOT_CATEGORIZED.code
                }

                updateData {
                    it.copy(
                        srce_wealt = value,
                        occ_type = occType
                    )
                }
            }

            is TradingAccountEvent.OnForeignAddress1Change -> updateData {
                it.copy(
                    foreign_address_1 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnForeignAddress2Change -> updateData {
                it.copy(
                    foreign_address_2 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnForeignAddress3Change -> updateData {
                it.copy(
                    foreign_address_3 = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnForeignCityChange -> updateData {
                it.copy(
                    foreign_address_city = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnForeignStateChange -> updateData {
                it.copy(
                    foreign_address_state = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnForeignPincodeChange -> {
                if (event.value.length > 6) return

                updateData {
                    it.copy(
                        foreign_address_pincode = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnForeignCountryChange -> updateData {
                it.copy(
                    foreign_address_country = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnForeignPhoneChange -> {
                if (event.value.length > 10) return

                updateData {
                    it.copy(
                        foreign_address_resi_phone = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnForeignFaxChange -> updateData {
                it.copy(
                    foreign_address_fax = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnForeignOfficePhoneChange -> {
                if (event.value.length > 10) return

                updateData {
                    it.copy(
                        foreign_address_off_phone = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnForeignOfficeFaxChange -> updateData {
                it.copy(
                    foreign_address_off_fax = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnResiPhoneChange -> {
                if (event.value.length > 10) return

                updateData {
                    it.copy(
                        resi_phone = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnResiFaxChange -> updateData {
                it.copy(
                    resi_fax = event.value.trim().toUpperCase(Locale.current)
                )
            }

            is TradingAccountEvent.OnOfficePhoneChange -> {
                if (event.value.length > 10) return

                updateData {
                    it.copy(
                        office_phone = event.value.trim().toUpperCase(Locale.current)
                    )
                }
            }

            is TradingAccountEvent.OnOfficeFaxChange -> updateData {
                it.copy(
                    office_fax = event.value.trim().toUpperCase(Locale.current)
                )
            }
            TradingAccountEvent.ShowStateDialog ->
                _uiState.update { it.copy(showStateDialog = true) }

            TradingAccountEvent.HideStateDialog ->
                _uiState.update { it.copy(showStateDialog = false) }

            TradingAccountEvent.ShowCountryDialog ->
                _uiState.update { it.copy(showCountryDialog = true) }

            TradingAccountEvent.HideCountryDialog ->
                _uiState.update { it.copy(showCountryDialog = false) }

            TradingAccountEvent.ShowForeignCountryDialog ->
                _uiState.update { it.copy(showForeignCountryDialog = true) }

            TradingAccountEvent.HideForeignCountryDialog ->
                _uiState.update { it.copy(showForeignCountryDialog = false) }
        }
    }

    private fun calculateDerivedState(state: TradingAccountUiState): TradingAccountUiState {
        val data = (state.formState as? UiState.Success)?.data?.data ?: return state

        // basicDetailsNextEnabled
        val basicDetailsNextEnabled = data.primary_holder_first_name.isNotEmpty() &&
                data.primary_holder_dob_incorporation.isNotEmpty() &&
                data.indian_mobile_no.isNotEmpty() &&
                data.email.isNotEmpty() &&
                data.po_bir_inc.isNotEmpty() &&
                data.tax_status.isNotEmpty()

        // bankScreenButtonEnabled
        val bankScreenButtonEnabled =
            data.div_pay_mode.isNotBlank() &&
                    state.visibleBankAccounts.all { index ->
                        val accountValid = when (index) {
                            1 -> data.account_type_1.isNotBlank() &&
                                    data.account_no_1.isNotBlank() &&
                                    data.ifsc_code_1.isNotBlank() &&
                                    data.default_bank_flag_1.isNotBlank()

                            2 -> data.account_type_2.isNotBlank() &&
                                    data.account_no_2.isNotBlank() &&
                                    data.ifsc_code_2.isNotBlank() &&
                                    data.default_bank_flag_2.isNotBlank()

                            3 -> data.account_type_3.isNotBlank() &&
                                    data.account_no_3.isNotBlank() &&
                                    data.ifsc_code_3.isNotBlank() &&
                                    data.default_bank_flag_3.isNotBlank()

                            4 -> data.account_type_4.isNotBlank() &&
                                    data.account_no_4.isNotBlank() &&
                                    data.ifsc_code_4.isNotBlank() &&
                                    data.default_bank_flag_4.isNotBlank()

                            5 -> data.account_type_5.isNotBlank() &&
                                    data.account_no_5.isNotBlank() &&
                                    data.ifsc_code_5.isNotBlank() &&
                                    data.default_bank_flag_5.isNotBlank()

                            else -> false
                        }

                        accountValid &&
                                !isBankAccountMismatch(index, data, state)
                    }

        // clientScreenButtonEnabled
        val clientTypeValid = data.client_type.isNotBlank()
        val isDemat = data.client_type == ClientType.DEMAT.code
        val dematFieldsValid = if (isDemat) {
            val pmsValid = data.pms.isNotBlank()
            val defaultDpValid = data.default_dp.isNotBlank()
            val dpBranchValid = when (data.default_dp) {
                DefaultDp.CDSL.code -> data.cdsl_dpid.isNotBlank() && data.cdslcltid.isNotBlank()
                DefaultDp.NSDL.code -> data.cmbp_id.isNotBlank() && data.nsdldpid.isNotBlank() && data.nsdlcltid.isNotBlank()
                else -> false
            }
            pmsValid && defaultDpValid && dpBranchValid
        } else true
        val clientScreenButtonEnabled = clientTypeValid && dematFieldsValid

        // financeScreenButtonEnabled
        val holdingNature = data.holding_nature
        val secondHolderValid = when (holdingNature) {
            Holding.JOINT.code -> data.second_holder_first_name.isNotBlank() && data.second_holder_pan.isNotBlank() && data.second_holder_email.isNotBlank() && data.second_holder_mobile.isNotBlank() && data.second_holder_dob.isNotBlank()
            else -> true
        }
        val thirdHolderStarted =
            data.third_holder_first_name.isNotBlank() || data.third_holder_pan.isNotBlank() || data.third_holder_email.isNotBlank() || data.third_holder_mobile.isNotBlank() || data.third_holder_dob.isNotBlank()
        val thirdHolderValid = when (holdingNature) {
            Holding.JOINT.code -> if (!thirdHolderStarted) true else data.third_holder_first_name.isNotBlank() && data.third_holder_pan.isNotBlank() && data.third_holder_email.isNotBlank() && data.third_holder_mobile.isNotBlank() && data.third_holder_dob.isNotBlank()
            else -> true
        }
        val nominee1IsMinor =
            data.nominee_1_dob.isNotBlank() && isMinor(DateTimeUtils.slashDateToIsoUtc(data.nominee_1_dob))
        val nomineeGuardianValid =
            if (nominee1IsMinor) data.nominee_1_guardian.isNotBlank() && data.nominee_1_guardian_pan.isNotBlank() else true
        val nominationEnabled = data.nomination_opt == "Y"
        val nominationValid = if (nominationEnabled) {
            val nomineeFilled =
                data.nominee_1_name.isNotBlank() && data.nominee_1_relationship.isNotBlank() && data.nominee_1_identity_type.isNotBlank() && data.nominee_1_identity_number.isNotBlank() && data.nominee_1_dob.isNotBlank() && data.nominee_1_email.isNotBlank() && data.nominee_1_mobile.isNotBlank() && data.nominee_1_address1.isNotBlank() && data.nominee_1_city.isNotBlank() && data.nominee_1_pin.isNotBlank() && data.nominee_1_country.isNotBlank() && nomineeGuardianValid
            val authValid = data.nomination_authentication in listOf("W", "E", "O")
            nomineeFilled && authValid
        } else data.nomination_authentication in listOf("O", "V")
        val financeScreenButtonEnabled = secondHolderValid && thirdHolderValid && nominationValid

        // addressScreenButtonEnabled
        val isForeignAddress = TaxStatus.fromCode(data.tax_status)?.isResident?.not() ?: false
        val addressValid = if (isForeignAddress) {
            data.foreign_address_1.isNotBlank() && data.foreign_address_city.isNotBlank() && data.foreign_address_state.isNotBlank() && data.foreign_address_pincode.isNotBlank() && data.foreign_address_country.isNotBlank()
        } else {
            data.address_1.isNotBlank() && data.city.isNotBlank() && data.state.isNotBlank() && data.pincode.isNotBlank() && data.country.isNotBlank()
        }
        val kycValid = when (data.primary_holder_kyc_type) {
            KycType.CKYC_COMPLIANT.code -> data.primary_holder_ckyc_number.isNotBlank()
            else -> data.primary_holder_kyc_type.isNotBlank()
        }
        val onboardingValid = data.paperless_flag.isNotBlank()
        val addressScreenButtonEnabled = addressValid && kycValid && onboardingValid

        // guardianScreenButtonEnabled
        val guardianScreenButtonEnabled =
            data.guardian_first_name.isNotBlank() && data.guardian_relation.isNotBlank() && data.guardian_dob.isNotBlank() && data.guardian_pan.isNotBlank()

        return state.copy(
            basicDetailsNextEnabled = basicDetailsNextEnabled,
            bankScreenButtonEnabled = bankScreenButtonEnabled,
            clientScreenButtonEnabled = clientScreenButtonEnabled,
            financeScreenButtonEnabled = financeScreenButtonEnabled,
            addressScreenButtonEnabled = addressScreenButtonEnabled,
            guardianScreenButtonEnabled = guardianScreenButtonEnabled
        )
    }

    private fun getUserData() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(formState = UiState.Loading)
            }

            getTradingAccountPrefilledDataUseCase()
                .onSuccess { userData ->
                    _uiState.update {
                        it.copy(
                            formState = UiState.Success(
                                TradingAccountFormDomain(
                                    data = Data()
                                )
                            )
                        )
                    }
                    updateData {
                        it.copy(
                            primary_holder_first_name = userData.fullName,
                            email = userData.email,
                            primary_holder_dob_incorporation = DateTimeUtils.isoUtcToSlashDate(
                                userData.dob
                            ),
                            indian_mobile_no = userData.phoneNo,
                            gender = userData.gender
                                .toUpperCase(Locale.current)
                                .take(1),
                            primary_holder_pan = userData.panNo,
                            po_bir_inc = userData.placeOfBirth,
                            address_1 = userData.fullAddress,
                            pincode = userData.pinCode,
                            city = userData.city,
                            state = StateCode.fromDisplayName(
                                userData.state
                            )?.code.orEmpty(),
                            country = userData.country,
                        )
                    }

                    val minor = isMinor(userData.dob)

                    _uiState.update {
                        it.copy(
                            isMinor = minor,
                            totalSteps = if (minor) 6 else 5
                        )
                    }

                    if (minor) {
                        handleEvent(
                            TradingAccountEvent.OnGuardianPanExemptChange("N")
                        )

                        handleEvent(
                            TradingAccountEvent.OnPrimaryPanExemptChange("Y")
                        )
                    }
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            formState = UiState.Error(error.message)
                        )
                    }
                    SnackBarController.showError("Failed to load data.")
                }
        }
    }

    private fun verifyPan(pan: String) {
        viewModelScope.launch {
            val previousState = _uiState.value.formState
            if (previousState !is UiState.Success) {
                return@launch
            }

            _uiState.update {
                it.copy(
                    formState = UiState.Loading
                )
            }

            verifyPANUseCase(pan)
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            formState = previousState,
                            panVerified = response.status,
                            verifiedPanNumber = pan
                        )
                    }
                    SnackBarController.showInfo(response.message)
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            formState = previousState
                        )
                    }
                    SnackBarController.showError(error.message)
                }
        }
    }

    private fun submitForm(onSuccessfulSubmit: () -> Unit) {
        viewModelScope.launch {

            val previousState = _uiState.value.formState
            val successState = previousState as? UiState.Success ?: return@launch

            _uiState.update {
                it.copy(
                    formState = UiState.Loading
                )
            }

            submitTradingAccountFormUseCase(successState.data)
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            formState = previousState,
                            launchedBrowser = true
                        )
                    }

                    openBrowserLauncher.launchBrowser(response)
                }
                .onError { error ->

                    _uiState.update {
                        it.copy(
                            formState = previousState
                        )
                    }
                    SnackBarController.showError(
                        error.message.ifBlank {
                            "Failed to submit trading account form"
                        }
                    )
                }
        }
    }
    private fun confirmAccount(
        onSuccessfulSubmit: () -> Unit
    ) {
        viewModelScope.launch {

            val previousState = _uiState.value.formState

            _uiState.update {
                it.copy(
                    launchedBrowser = false
                )
            }

            val successState = previousState as? UiState.Success
                ?: return@launch

            _uiState.update {
                it.copy(
                    formState = UiState.Loading
                )
            }

            tradingAccountConfirmationUseCase(
                taxStatus = successState.data.data.tax_status,
                holdingNature = successState.data.data.holding_nature,
                jointHolderName1 =
                successState.data.data.second_holder_first_name +
                        " " +
                        successState.data.data.second_holder_last_name,
                jointHolderName2 =
                successState.data.data.third_holder_first_name +
                        " " +
                        successState.data.data.third_holder_last_name,
                guardianName =
                successState.data.data.guardian_first_name +
                        " " +
                        successState.data.data.guardian_last_name,
                isMinor = _uiState.value.isMinor
            )
                .onSuccess {

                    _uiState.update {
                        it.copy(
                            formState = previousState
                        )
                    }

                    SnackBarController.showSuccess("Account Created Successfully")
                    AppEventsController.sendHomeRefreshEvent()

                    onSuccessfulSubmit()
                }
                .onError { error ->

                    _uiState.update {
                        it.copy(
                            formState = previousState
                        )
                    }

                    SnackBarController.showError(
                        error.message.ifBlank {
                            "Failed to confirm trading account"
                        }
                    )
                }
        }
    }

    private fun resetJointHolderData() {
        resetSecondHolder()
        resetThirdHolder()
    }

    private fun resetSecondHolder() {
        updateData {
            it.copy(
                second_holder_first_name = "",
                second_holder_middle_name = "",
                second_holder_last_name = "",
                second_holder_pan = "",
                second_holder_dob = "",
                second_holder_email = "",
                second_holder_mobile = "",
                second_holder_ckyc_number = "",
                second_holder_kyc_type = "",
                second_holder_pan_exempt = "",
                second_holder_exempt_category = "",
                second_holder_email_declaration = "",
                second_holder_mobile_declaration = "",
                second_holder_kra_exempt_ref_no = ""
            )
        }
    }

    private fun resetThirdHolder() {
        updateData {
            it.copy(
                third_holder_first_name = "",
                third_holder_middle_name = "",
                third_holder_last_name = "",
                third_holder_pan = "",
                third_holder_dob = "",
                third_holder_email = "",
                third_holder_mobile = "",
                third_holder_ckyc_number = "",
                third_holder_kyc_type = "",
                third_holder_pan_exempt = "",
                third_holder_exempt_category = "",
                third_holder_email_declaration = "",
                third_holder_mobile_declaration = "",
                third_holder_kra_exempt_ref_no = ""
            )
        }
    }

    private fun resetBankAccount(index: Int) {
        updateData {
            when (index) {
                2 -> it.copy(
                    account_type_2 = "",
                    account_no_2 = "",
                    ifsc_code_2 = "",
                    micr_no_2 = "",
                    default_bank_flag_2 = ""
                )

                3 -> it.copy(
                    account_type_3 = "",
                    account_no_3 = "",
                    ifsc_code_3 = "",
                    micr_no_3 = "",
                    default_bank_flag_3 = ""
                )

                4 -> it.copy(
                    account_type_4 = "",
                    account_no_4 = "",
                    ifsc_code_4 = "",
                    micr_no_4 = "",
                    default_bank_flag_4 = ""
                )

                5 -> it.copy(
                    account_type_5 = "",
                    account_no_5 = "",
                    ifsc_code_5 = "",
                    micr_no_5 = "",
                    default_bank_flag_5 = ""
                )

                else -> it
            }
        }
    }

    private fun resetClientTypeDependentFields(clientType: ClientType) {
        updateData { data ->
            when (clientType) {
                ClientType.PHYSICAL -> data.copy(
                    pms = "",
                    default_dp = "",
                    cdsl_dpid = "",
                    cdslcltid = "",
                    cmbp_id = "",
                    nsdldpid = "",
                    nsdlcltid = ""
                )

                ClientType.DEMAT -> data
            }
        }
    }

    private fun resetDefaultDpDependentFields(defaultDp: DefaultDp) {
        updateData { data ->
            when (defaultDp) {
                DefaultDp.CDSL -> data.copy(cmbp_id = "", nsdldpid = "", nsdlcltid = "")
                DefaultDp.NSDL -> data.copy(cdsl_dpid = "", cdslcltid = "")
            }
        }
    }
}

fun isMinor(dobIsoUtc: String): Boolean {
    return try {
        val birthDate = Instant.parse(dobIsoUtc).toLocalDateTime(TimeZone.UTC).date
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val age = calculateAge(birthDate, today)
        age < 18
    } catch (_: Exception) {
        false
    }
}

private fun calculateAge(birthDate: LocalDate, today: LocalDate): Int {
    var age = today.year - birthDate.year
    if (today.month.number < birthDate.month.number || (today.month.number == birthDate.month.number && today.day < birthDate.day)) {
        age--
    }
    return age
}

// Helper functions for bank accounts (still used in bank screen probably)
fun getAccountType(index: Int, data: Data): String = when (index) {
    1 -> data.account_type_1
    2 -> data.account_type_2
    3 -> data.account_type_3
    4 -> data.account_type_4
    5 -> data.account_type_5
    else -> ""
}

fun getAccountNumber(index: Int, data: Data): String = when (index) {
    1 -> data.account_no_1
    2 -> data.account_no_2
    3 -> data.account_no_3
    4 -> data.account_no_4
    5 -> data.account_no_5
    else -> ""
}

fun getIfsc(index: Int, data: Data): String = when (index) {
    1 -> data.ifsc_code_1
    2 -> data.ifsc_code_2
    3 -> data.ifsc_code_3
    4 -> data.ifsc_code_4
    5 -> data.ifsc_code_5
    else -> ""
}

fun getMicr(index: Int, data: Data): String = when (index) {
    1 -> data.micr_no_1
    2 -> data.micr_no_2
    3 -> data.micr_no_3
    4 -> data.micr_no_4
    5 -> data.micr_no_5
    else -> ""
}

fun getDefaultBank(index: Int, data: Data): String = when (index) {
    1 -> data.default_bank_flag_1
    2 -> data.default_bank_flag_2
    3 -> data.default_bank_flag_3
    4 -> data.default_bank_flag_4
    5 -> data.default_bank_flag_5
    else -> ""
}

fun getReEnteredAccountNumber(
    index: Int,
    state: TradingAccountUiState
): String =
    state.reEnteredAccountNumbers[index - 1]

fun isBankAccountMismatch(
    index: Int,
    data: Data,
    state: TradingAccountUiState
): Boolean {

    val original = getAccountNumber(index, data)
    val reEntered = getReEnteredAccountNumber(index, state)

    if (reEntered.isBlank()) return false

    return original != reEntered
}