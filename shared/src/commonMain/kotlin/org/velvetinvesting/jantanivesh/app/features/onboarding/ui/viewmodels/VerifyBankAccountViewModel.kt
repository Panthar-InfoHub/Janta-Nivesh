package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.utils.toCapital
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.AccountType
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.BankAccount
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.GetPennyDropStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SubmitPennyDropUseCase

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
    val bankName: String = "",
    val accountType: AccountType? = null,
    val accountHolder: String = "",
    val accountNumber: String = "",
    val ifscCode: String = "",
    val isLoading: Boolean = false,
    val showConfirmBankAccountSheet: Boolean = false,
    val bankAccountDetails: BankAccountDetails = BankAccountDetails()
) {
    val canSubmit: Boolean
        get() = bankName.isNotBlank() &&
                accountType != null &&
                accountHolder.isNotBlank() &&
                accountNumber.isNotBlank() &&
                ifscCode.isNotBlank()
}

sealed interface VerifyBankAccountEvent {
    data class OnBankNameChange(val bankName: String) : VerifyBankAccountEvent
    data class OnAccountTypeChange(val accountType: AccountType) : VerifyBankAccountEvent
    data class OnAccountHolderChange(val accountHolder: String) : VerifyBankAccountEvent
    data class OnAccountNumberChange(val accountNumber: String) : VerifyBankAccountEvent
    data class OnIfscCodeChange(val ifscCode: String) : VerifyBankAccountEvent
    data object OnProceedClick : VerifyBankAccountEvent
    data object OnConfirmBankAccountClick : VerifyBankAccountEvent
    data object OnChangeBankAccountClick : VerifyBankAccountEvent
    data object OnDismissConfirmBankAccountSheet : VerifyBankAccountEvent
}

sealed interface VerifyBankAccountEffect {
    data object PennyDropCompleted : VerifyBankAccountEffect
    data object NavigateToChangeBankAccount : VerifyBankAccountEffect
}

class VerifyBankAccountViewModel(
    private val submitPennyDrop: SubmitPennyDropUseCase,
    private val getPennyDropStatus: GetPennyDropStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerifyBankAccountUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<VerifyBankAccountEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: VerifyBankAccountEvent) {
        when (event) {
            is VerifyBankAccountEvent.OnBankNameChange -> onBankNameChange(event.bankName)
            is VerifyBankAccountEvent.OnAccountTypeChange -> onAccountTypeChange(event.accountType)
            is VerifyBankAccountEvent.OnAccountHolderChange -> onAccountHolderChange(event.accountHolder)
            is VerifyBankAccountEvent.OnAccountNumberChange -> onAccountNumberChange(event.accountNumber)
            is VerifyBankAccountEvent.OnIfscCodeChange -> onIfscCodeChange(event.ifscCode)
            VerifyBankAccountEvent.OnProceedClick -> onProceedClick()
            VerifyBankAccountEvent.OnConfirmBankAccountClick -> onConfirmBankAccountClick()
            VerifyBankAccountEvent.OnChangeBankAccountClick -> onChangeBankAccountClick()
            VerifyBankAccountEvent.OnDismissConfirmBankAccountSheet -> setSheetVisible(false)
        }
    }

    private fun onBankNameChange(bankName: String) {
        _uiState.update { it.copy(bankName = bankName.toCapital()) }
    }

    private fun onAccountTypeChange(accountType: AccountType) {
        _uiState.update { it.copy(accountType = accountType) }
    }

    private fun onAccountHolderChange(accountHolder: String) {
        _uiState.update { it.copy(accountHolder = accountHolder) }
    }

    private fun onAccountNumberChange(accountNumber: String) {
        if (!accountNumber.all { it.isDigit() }) return
        _uiState.update { it.copy(accountNumber = accountNumber) }
    }

    private fun onIfscCodeChange(ifscCode: String) {
        _uiState.update { it.copy(ifscCode = ifscCode.toCapital()) }
    }

    private fun onProceedClick() {
        val state = _uiState.value
        if (!state.canSubmit || state.isLoading) return

        _uiState.update {
            it.copy(
                bankAccountDetails = BankAccountDetails(
                    bankName = state.bankName,
                    accountHolder = state.accountHolder,
                    accountNumber = state.accountNumber,
                    ifscCode = state.ifscCode
                )
            )
        }
        setSheetVisible(true)
    }

    private fun onConfirmBankAccountClick() {
        if (_uiState.value.isLoading) return

        setSheetVisible(false)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value
            val bankAccount = BankAccount(
                accountNumber = state.accountNumber,
                ifscCode = state.ifscCode,
                bankName = state.bankName,
                accountHolderName = state.accountHolder,
                accountType = state.accountType?.id ?: AccountType.SAVINGS.id
            )
            try {
                when (val result = submitPennyDrop(bankAccount)) {
                    is NetworkResponse.Error -> SnackBarController.showError(result.error.message)
                    is NetworkResponse.Success -> awaitPennyDropVerification(state.accountNumber)
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * The bank verifies the deposit out of band, so the submission succeeding only means the
     * request was accepted. This reads the status back until the account turns verified, and only
     * then lets the user move on — the screen stays in its loading state throughout.
     */
    private suspend fun awaitPennyDropVerification(accountNumber: String) {
        repeat(STATUS_POLL_ATTEMPTS) { attempt ->
            // The first read happens immediately; the bank has often answered by then.
            if (attempt > 0) delay(STATUS_POLL_INTERVAL_MS)

            when (val result = getPennyDropStatus(accountNumber)) {
                is NetworkResponse.Error -> {
                    SnackBarController.showError(result.error.message)
                    return
                }

                is NetworkResponse.Success -> {
                    val status = result.data
                    when {
                        status.isVerified -> {
                            sendEffect(VerifyBankAccountEffect.PennyDropCompleted)
                            return
                        }

                        status.isFailed -> {
                            SnackBarController.showError(
                                status.reason ?: VERIFICATION_FAILED_MESSAGE
                            )
                            return
                        }
                    }
                }
            }
        }

        SnackBarController.showError(VERIFICATION_PENDING_MESSAGE)
    }

    private fun onChangeBankAccountClick() {
        setSheetVisible(false)
    }

    private fun setSheetVisible(visible: Boolean) {
        _uiState.update { it.copy(showConfirmBankAccountSheet = visible) }
    }

    private fun sendEffect(effect: VerifyBankAccountEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    private companion object {
        /** Five reads with 5-second gaps — about 20 seconds for the bank to answer. */
        const val STATUS_POLL_ATTEMPTS = 5
        const val STATUS_POLL_INTERVAL_MS = 5_000L

        const val VERIFICATION_FAILED_MESSAGE =
            "We could not verify this bank account. Please check the details and try again."

        const val VERIFICATION_PENDING_MESSAGE =
            "Your bank account is still being verified. Please try again in a moment."
    }
}
