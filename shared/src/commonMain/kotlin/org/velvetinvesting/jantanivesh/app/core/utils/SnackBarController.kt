package org.velvetinvesting.jantanivesh.app.core.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackBarController {

    private val _snackBars = MutableSharedFlow<SnackBarType>(extraBufferCapacity = 1)
    val snackBars = _snackBars.asSharedFlow()

    suspend fun showSnackBar(snackBarType: SnackBarType) {
        _snackBars.emit(snackBarType)
    }

    suspend fun showSuccess(message: String) {
        _snackBars.emit(SnackBarType.Success(message))
    }

    suspend fun showError(message: String) {
        _snackBars.emit(SnackBarType.Error(message))
    }

    suspend fun showWarning(message: String) {
        _snackBars.emit(SnackBarType.Warning(message))
    }

    suspend fun showInfo(message: String) {
        _snackBars.emit(SnackBarType.Info(message))
    }

    suspend fun showNeutral(message: String) {
        _snackBars.emit(SnackBarType.Neutral(message))
    }
}

sealed class SnackBarType{
    data class Success(val message: String): SnackBarType()
    data class Error(val message: String): SnackBarType()
    data class Warning(val message: String): SnackBarType()
    data class Info(val message: String): SnackBarType()
    data class Neutral(val message: String): SnackBarType()
}
