package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.UploadKycFormSignatureUseCase

data class UploadSignatureUiState(
    val isLoading: Boolean = false,
    val signature: PhotoResult? = null,
    val showSignatureSelector: Boolean = false
)

sealed interface UploadSignatureEvent {
    data class OnSignatureSelected(val photo: PhotoResult) : UploadSignatureEvent
    data object ShowSignatureSelector : UploadSignatureEvent
    data object HideSignatureSelector : UploadSignatureEvent
    data object RemoveSignature : UploadSignatureEvent
    data object OnUploadClicked : UploadSignatureEvent
}

sealed interface UploadSignatureEffect {
    data object SignatureUploaded : UploadSignatureEffect
}

/**
 * Uploads the signature image and nothing more. The eSign it eventually produces is picked up
 * later in onboarding, from the investor profile screen.
 */
class UploadSignatureViewModel(
    private val uploadKycFormSignature: UploadKycFormSignatureUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UploadSignatureUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<UploadSignatureEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: UploadSignatureEvent) {
        when (event) {
            is UploadSignatureEvent.OnSignatureSelected -> _uiState.update {
                it.copy(signature = event.photo, showSignatureSelector = false)
            }

            UploadSignatureEvent.ShowSignatureSelector -> _uiState.update {
                it.copy(showSignatureSelector = true)
            }

            UploadSignatureEvent.HideSignatureSelector -> _uiState.update {
                it.copy(showSignatureSelector = false)
            }

            UploadSignatureEvent.RemoveSignature -> _uiState.update { it.copy(signature = null) }

            UploadSignatureEvent.OnUploadClicked -> uploadSignature()
        }
    }

    private fun uploadSignature() {
        val state = _uiState.value
        if (state.isLoading) return

        val signature = state.signature ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = uploadKycFormSignature(
                    imageBytes = signature.loadBytes(),
                    mimeType = signature.mimeType ?: "image/jpeg"
                )

                when (result) {
                    is NetworkResponse.Error -> SnackBarController.showError(result.error.message)
                    is NetworkResponse.Success ->
                        _effect.send(UploadSignatureEffect.SignatureUploaded)
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
