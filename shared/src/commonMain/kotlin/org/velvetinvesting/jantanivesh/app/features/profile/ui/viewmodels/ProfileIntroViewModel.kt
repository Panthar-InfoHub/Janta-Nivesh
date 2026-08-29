package org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.onError
import org.velvetinvesting.jantanivesh.app.core.networking.onSuccess
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.GetUserDataUseCase

data class ProfileUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String ="",
    val userName: String = "",
    val email: String = "",
    val kycCompleted: Boolean = false,
    val tradingAccountCompleted: Boolean = false
    )

sealed interface ProfileEvent {

    data object OnSecondaryLanguageClicked : ProfileEvent
    data object OnSettingsClicked : ProfileEvent

    data object OnBankAccountsClicked : ProfileEvent
//    data object OnTransactionHistoryClicked : ProfileEvent
    data object OnKycStatusClicked : ProfileEvent

    data object OnHelpFaqClicked : ProfileEvent
    data object OnContactUsClicked : ProfileEvent

    data object OnLogoutClicked : ProfileEvent
   data object OnTradingAccountStatusClicked : ProfileEvent
}

sealed interface ProfileEffect {

    data object NavigateToSecondaryLanguage : ProfileEffect
    data object NavigateToSettings : ProfileEffect

    data object NavigateToBankAccounts : ProfileEffect
//    data object NavigateToTransactionHistory : ProfileEffect
    data object NavigateToKycStatus : ProfileEffect
    data object NavigateToTradingAccountStatus: ProfileEffect

    data object NavigateToHelpFaq : ProfileEffect
    data object NavigateToContactUs : ProfileEffect

    data object ShowLogoutDialog : ProfileEffect
}
class ProfileViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ProfileEffect>()
    val effect = _effect.receiveAsFlow()


    init {
        loadData()
    }

    fun handleEvent(event: ProfileEvent) {
        when (event) {

            ProfileEvent.OnSecondaryLanguageClicked ->
                sendEffect(ProfileEffect.NavigateToSecondaryLanguage)

            ProfileEvent.OnSettingsClicked ->
                sendEffect(ProfileEffect.NavigateToSettings)
//
            ProfileEvent.OnBankAccountsClicked ->
                sendEffect(ProfileEffect.NavigateToBankAccounts)
//
//            ProfileEvent.OnTransactionHistoryClicked ->
//                sendEffect(ProfileEffect.NavigateToTransactionHistory)

            ProfileEvent.OnKycStatusClicked ->
                sendEffect(ProfileEffect.NavigateToKycStatus)

            ProfileEvent.OnTradingAccountStatusClicked->
                sendEffect(ProfileEffect.NavigateToTradingAccountStatus)

            ProfileEvent.OnHelpFaqClicked ->
                sendEffect(ProfileEffect.NavigateToHelpFaq)

            ProfileEvent.OnContactUsClicked ->
                sendEffect(ProfileEffect.NavigateToContactUs)

            ProfileEvent.OnLogoutClicked ->
                sendEffect(ProfileEffect.ShowLogoutDialog)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isError = false,
                    errorMessage = ""
                )
            }
            getUserDataUseCase()
                .onSuccess { userDataDomain ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userName = userDataDomain.name,
                            email = userDataDomain.email,
                            kycCompleted = userDataDomain.kycVerified,
                        )
                    }
                }
                .onError {error->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            errorMessage = error.message
                        )
                    }
                    SnackBarController.showError(error.message)
                }
        }
    }

    private fun sendEffect(effect: ProfileEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}