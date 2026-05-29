package org.velvetinvesting.jantanivesh.app.core.networking

sealed class NetworkResponse<out T, out E> {
    data class Success<out T>(val data: T) : NetworkResponse<T, Nothing>()
    data class Error<out E>(val error: E) : NetworkResponse<Nothing, E>()
}


data class ErrorDomain(
    val code: Int,
    val message: String,
    val type: ErrorType
)

enum class ErrorType {
    MF_KYC_REQUIRED,
    NO_INTERNET,
    SERIALIZATION,
    UNKNOWN,
    SERVER
}