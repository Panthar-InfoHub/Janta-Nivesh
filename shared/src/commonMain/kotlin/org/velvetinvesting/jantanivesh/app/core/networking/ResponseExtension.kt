package org.velvetinvesting.jantanivesh.app.core.networking

inline fun <T, E> NetworkResponse<T, E>.onSuccess(
    action: (T) -> Unit
): NetworkResponse<T, E> {
    if (this is NetworkResponse.Success) action(data)
    return this
}

inline fun <T, E> NetworkResponse<T, E>.onError(
    action: (E) -> Unit
): NetworkResponse<T, E> {
    if (this is NetworkResponse.Error) action(error)
    return this
}
