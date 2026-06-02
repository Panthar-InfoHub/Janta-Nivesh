package org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
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
    val photoMimeType: String?= null,
    val signatureMimeType: String?= null,
    val showSignatureSelector: Boolean = false,
    val showPhotoSelector: Boolean = false
)

sealed interface KYCImageUploaderEvent {
    data class OnUserPhotoSelected(val bytes: ByteArray, val mimeType: String) : KYCImageUploaderEvent
    data class OnSignatureSelected(val bytes: ByteArray, val mimeType: String) : KYCImageUploaderEvent
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
                    photoMimeType = event.mimeType,
                    showPhotoSelector = false
                )
            }

            is KYCImageUploaderEvent.OnSignatureSelected -> _uiState.update {
                it.copy(
                    signatureBytes = event.bytes,
                    signatureMimeType = event.mimeType,
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

            val photoBytes = state.userPhotoBytes ?: run {
                sendEffect(
                    KYCImageUploaderEffect.ShowError(
                        "Please select both photo and signature"
                    )
                )
                return@launch
            }

            val signatureBytes = state.signatureBytes ?: run {
                sendEffect(
                    KYCImageUploaderEffect.ShowError(
                        "Please select both photo and signature"
                    )
                )
                return@launch
            }

            val photoMimeType = state.photoMimeType ?: "image/jpeg"
            val signatureMimeType = state.signatureMimeType ?: "image/jpeg"

            _uiState.update { it.copy(isLoading = true) }

            try {
                val photoDeferred = async {
                    uploadKycImageUseCase(
                        photoBytes,
                        photoMimeType
                    )
                }

                val signatureDeferred = async {
                    uploadKycSignatureUseCase(
                        signatureBytes,
                        signatureMimeType
                    )
                }

                val photoUploadResult = photoDeferred.await()
                val signatureUploadResult = signatureDeferred.await()

                if (photoUploadResult is NetworkResponse.Error) {
                    sendEffect(
                        KYCImageUploaderEffect.ShowError(
                            photoUploadResult.error.message
                        )
                    )
                    return@launch
                }

                if (signatureUploadResult is NetworkResponse.Error) {
                    sendEffect(
                        KYCImageUploaderEffect.ShowError(
                            signatureUploadResult.error.message
                        )
                    )
                    return@launch
                }

                val photoUrl =
                    (photoUploadResult as NetworkResponse.Success).data

                val signatureUrl =
                    (signatureUploadResult as NetworkResponse.Success).data

                val photoLinkResult =
                    linkKycDocumentsUseCase(
                        type = "photo",
                        imgUrl = photoUrl
                    )

                if (photoLinkResult is NetworkResponse.Error) {
                    sendEffect(
                        KYCImageUploaderEffect.ShowError(
                            photoLinkResult.error.message
                        )
                    )
                    return@launch
                }

                val signatureLinkResult =
                    linkKycDocumentsUseCase(
                        type = "signature",
                        imgUrl = signatureUrl
                    )

                if (signatureLinkResult is NetworkResponse.Error) {
                    sendEffect(
                        KYCImageUploaderEffect.ShowError(
                            signatureLinkResult.error.message
                        )
                    )
                    return@launch
                }

                sendEffect(
                    KYCImageUploaderEffect.NavigateToContract
                )
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    private fun sendEffect(effect: KYCImageUploaderEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
