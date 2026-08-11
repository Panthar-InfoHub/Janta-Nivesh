package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model.BankAccount
import org.velvetinvesting.jantanivesh.app.features.kycnew.domain.usecases.SubmitPennyDropUseCase
import kotlin.random.Random

data class BankAccountDetails(
    val bankName: String = "",
    val accountHolder: String = "",
    val accountNumber: String = "",
    val ifscCode: String = ""
) {
    /** Shows only the last four digits, e.g. `••••••••4856`. */
    val maskedAccountNumber: String
        get() = if (accountNumber.length <= VISIBLE_ACCOUNT_DIGITS) {
            accountNumber
        } else {
            "•".repeat(accountNumber.length - VISIBLE_ACCOUNT_DIGITS) +
                    accountNumber.takeLast(VISIBLE_ACCOUNT_DIGITS)
        }

    private companion object {
        const val VISIBLE_ACCOUNT_DIGITS = 4
    }
}

data class VerifyBankAccountUiState(
    val isLoading: Boolean = false,
    val showConfirmBankAccountSheet: Boolean = false,
    // TODO: Replace with the details fetched from the penny drop response
    val bankAccountDetails: BankAccountDetails = BankAccountDetails(
        bankName = VerifyBankAccountViewModel.DUMMY_BANK_ACCOUNT.bankName,
        accountHolder = VerifyBankAccountViewModel.DUMMY_BANK_ACCOUNT.accountHolderName,
        accountNumber = VerifyBankAccountViewModel.DUMMY_BANK_ACCOUNT.accountNumber,
        ifscCode = VerifyBankAccountViewModel.DUMMY_BANK_ACCOUNT.ifscCode
    )
)

sealed interface VerifyBankAccountEvent {
    data object OnVerifyUpiClick : VerifyBankAccountEvent
    data object OnConfirmBankAccountClick : VerifyBankAccountEvent
    data object OnChangeBankAccountClick : VerifyBankAccountEvent
    data object OnDismissConfirmBankAccountSheet : VerifyBankAccountEvent
}

sealed interface VerifyBankAccountEffect {
    data object PennyDropCompleted : VerifyBankAccountEffect
    data object NavigateToChangeBankAccount : VerifyBankAccountEffect
}

class VerifyBankAccountViewModel(
    private val submitPennyDrop: SubmitPennyDropUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyBankAccountUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<VerifyBankAccountEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: VerifyBankAccountEvent) {
        when (event) {
            VerifyBankAccountEvent.OnVerifyUpiClick -> onVerifyUpiClick()
            VerifyBankAccountEvent.OnConfirmBankAccountClick -> onConfirmBankAccountClick()
            VerifyBankAccountEvent.OnChangeBankAccountClick -> onChangeBankAccountClick()
            VerifyBankAccountEvent.OnDismissConfirmBankAccountSheet -> setSheetVisible(false)
        }
    }

    private fun onVerifyUpiClick() {
        // TODO: Fetch the bank details for the ₹1 penny drop before showing the sheet
        setSheetVisible(true)
    }

    private fun onConfirmBankAccountClick() {
        if (_uiState.value.isLoading) return

        setSheetVisible(false)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                when (val result = submitPennyDrop(DUMMY_BANK_ACCOUNT)) {
                    is NetworkResponse.Error -> SnackBarController.showError(result.error.message)
                    is NetworkResponse.Success ->
                        _effect.send(VerifyBankAccountEffect.PennyDropCompleted)
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onChangeBankAccountClick() {
        setSheetVisible(false)
        sendEffect(VerifyBankAccountEffect.NavigateToChangeBankAccount)
    }

    private fun setSheetVisible(visible: Boolean) {
        _uiState.update { it.copy(showConfirmBankAccountSheet = visible) }
    }

    private fun sendEffect(effect: VerifyBankAccountEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    companion object {
        private fun generateDummyAccountNumber(): String {
            val randomPart = buildString {
                repeat(8) {
                    append(Random.nextInt(4, 10))
                }
            }
            return "${randomPart}1193"
        }
        val DUMMY_BANK_ACCOUNT = BankAccount(
            accountNumber = generateDummyAccountNumber(),
            ifscCode = "HDFC0000453",
            bankName = "HDFC BANK",
            accountHolderName = "User Test",
            accountType = "savings"
        )
    }
}
