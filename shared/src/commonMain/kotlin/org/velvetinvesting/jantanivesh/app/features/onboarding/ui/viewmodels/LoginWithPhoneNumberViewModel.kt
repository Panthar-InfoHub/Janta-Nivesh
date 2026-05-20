package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.models.LoginWithPhoneNumberState


class LoginWithPhoneNumberViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LoginWithPhoneNumberState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LoginWithPhoneNumber) {
        when (event) {
            is LoginWithPhoneNumber.UpdatePhoneNumber -> updatePhoneNumber(event.phone)
        }
    }

    private fun updatePhoneNumber(number: String){
        _uiState.update{it.copy(phoneNumber = number)}
    }
}

sealed interface LoginWithPhoneNumber{
    data class UpdatePhoneNumber(val phone: String): LoginWithPhoneNumber
}