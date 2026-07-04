package org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
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

enum class PickerTarget {
    PHOTO,
    SIGNATURE
}
data class KYCImageUploaderUiState(
    val isLoading: Boolean = false,
    val userPhoto: PhotoResult? = null,
    val signature: PhotoResult? = null,
    val showSignatureSelector: Boolean = false,
    val showPhotoSelector: Boolean = false

)

sealed interface KYCImageUploaderEvent {


    data class OnUserPhotoSelected(
        val photo: PhotoResult
    ) : KYCImageUploaderEvent

    data class OnSignatureSelected(
        val photo: PhotoResult
    ) : KYCImageUploaderEvent
    data object OnUploadClicked : KYCImageUploaderEvent
    data object showSignatureSelector : KYCImageUploaderEvent
    data object showPhotoSelector : KYCImageUploaderEvent
    data object hideSignatureSelector : KYCImageUploaderEvent
    data object hidePhotoSelector : KYCImageUploaderEvent
    data object removeSignature : KYCImageUploaderEvent
    data object removePhoto : KYCImageUploaderEvent
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
                    userPhoto = event.photo,
                    showPhotoSelector = false
                )
            }

            is KYCImageUploaderEvent.OnSignatureSelected -> _uiState.update {
                it.copy(
                    signature = event.photo,
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
            KYCImageUploaderEvent.removePhoto -> _uiState.update {
                it.copy(
                    userPhoto = null
                )
            }
            KYCImageUploaderEvent.removeSignature -> _uiState.update {
                it.copy(
                    signature = null
                )
            }
        }
    }

    private fun uploadImages() {
        viewModelScope.launch {
            val state = _uiState.value

            val photo = state.userPhoto ?: run {
                sendEffect(
                    KYCImageUploaderEffect.ShowError(
                        "Please select both photo and signature"
                    )
                )
                return@launch
            }

            val signature = state.signature ?: run {
                sendEffect(
                    KYCImageUploaderEffect.ShowError(
                        "Please select both photo and signature"
                    )
                )
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            try {
                val photoDeferred = async {
                    uploadKycImageUseCase(
                        photo.loadBytes(),
                        photo.mimeType ?: "image/jpeg"
                    )
                }

                val signatureDeferred = async {
                    uploadKycSignatureUseCase(
                        signature.loadBytes(),
                        signature.mimeType ?: "image/jpeg"
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
