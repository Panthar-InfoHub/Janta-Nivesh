package org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.LinkKycDocumentsUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.UploadKycImageUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.UploadKycSignatureUseCase

data class KYCImageUploaderUiState(
    val isLoading: Boolean = false,
    val userPhotoBytes: ByteArray? = null,
    val signatureBytes: ByteArray? = null,
    val showSignatureSelector: Boolean = false,
    val showPhotoSelector: Boolean = false
)

sealed interface KYCImageUploaderEvent {
    data class OnUserPhotoSelected(val bytes: ByteArray) : KYCImageUploaderEvent
    data class OnSignatureSelected(val bytes: ByteArray) : KYCImageUploaderEvent
    data object OnUploadClicked : KYCImageUploaderEvent
    data object showSignatureSelector : KYCImageUploaderEvent
    data object showPhotoSelector : KYCImageUploaderEvent
    data object hideSignatureSelector : KYCImageUploaderEvent
    data object hidePhotoSelector : KYCImageUploaderEvent
}

sealed interface KYCImageUploaderEffect {
    data object NavigateToContract : KYCImageUploaderEffect
    data class ShowError(val message: String) : KYCImageUploaderEffect
}

class KYCImageUploaderScreenViewModel(
    private val uploadKycImageUseCase: UploadKycImageUseCase,
    private val uploadKycSignatureUseCase: UploadKycSignatureUseCase,
    private val linkKycDocumentsUseCase: LinkKycDocumentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KYCImageUploaderUiState())
    val uiState: StateFlow<KYCImageUploaderUiState> = _uiState.asStateFlow()

    private val _effect = Channel<KYCImageUploaderEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleEvent(event: KYCImageUploaderEvent) {
        when (event) {
            is KYCImageUploaderEvent.OnUserPhotoSelected -> _uiState.update {
                it.copy(
                    userPhotoBytes = event.bytes,
                    showPhotoSelector = false
                )
            }

            is KYCImageUploaderEvent.OnSignatureSelected -> _uiState.update {
                it.copy(
                    signatureBytes = event.bytes,
                    showSignatureSelector = false
                )
            }

            KYCImageUploaderEvent.OnUploadClicked -> uploadImages()
            KYCImageUploaderEvent.showSignatureSelector -> _uiState.update {
                it.copy(
                    showSignatureSelector = true
                )
            }

            KYCImageUploaderEvent.showPhotoSelector -> _uiState.update { it.copy(showPhotoSelector = true) }
            KYCImageUploaderEvent.hideSignatureSelector -> _uiState.update {
                it.copy(
                    showSignatureSelector = false
                )
            }

            KYCImageUploaderEvent.hidePhotoSelector -> _uiState.update { it.copy(showPhotoSelector = false) }
        }
    }

    private fun uploadImages() {
        viewModelScope.launch {
            val state = _uiState.value
            val userPhotoBytes = state.userPhotoBytes
            val signatureBytes = state.signatureBytes

            if (userPhotoBytes == null || signatureBytes == null) {
                sendEffect(KYCImageUploaderEffect.ShowError("Please select both photo and signature"))
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }
            
            // Upload User Photo
            val photoResponse = uploadKycImageUseCase(userPhotoBytes, "image/jpeg")
            if (photoResponse is NetworkResponse.Error) {
                sendEffect(KYCImageUploaderEffect.ShowError("Photo upload failed: ${photoResponse.error.message}"))
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }
            val photoUrl = (photoResponse as NetworkResponse.Success).data

            // Upload Signature
            val sigResponse = uploadKycSignatureUseCase(signatureBytes, "image/jpeg")
            if (sigResponse is NetworkResponse.Error) {
                sendEffect(KYCImageUploaderEffect.ShowError("Signature upload failed: ${sigResponse.error.message}"))
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }
            val sigUrl = (sigResponse as NetworkResponse.Success).data

            // Link documents
            linkKycDocumentsUseCase("identityVideo", photoUrl)
            linkKycDocumentsUseCase("signature", sigUrl)

            _uiState.update { it.copy(isLoading = false) }
            sendEffect(KYCImageUploaderEffect.NavigateToContract)
        }
    }

    private fun sendEffect(effect: KYCImageUploaderEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
