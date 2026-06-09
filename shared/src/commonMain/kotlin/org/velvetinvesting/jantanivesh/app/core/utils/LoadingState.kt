package org.velvetinvesting.jantanivesh.app.core.utils

sealed interface LoadingState {
    data object Loading: LoadingState
    data object Success: LoadingState
    data class Error(val error:String): LoadingState
}

sealed interface UiState<out T> {

    data object Loading : UiState<Nothing>

    data class Success<T>(
        val data: T
    ) : UiState<T>

    data class Error(
        val message: String
    ) : UiState<Nothing>
}