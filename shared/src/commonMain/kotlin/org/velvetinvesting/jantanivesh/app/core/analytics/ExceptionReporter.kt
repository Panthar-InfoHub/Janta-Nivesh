package org.velvetinvesting.jantanivesh.app.core.analytics

/**
 * Sends handled exceptions to error tracking. Implemented per platform and bound through Koin:
 * PostHog on Android (from `MyApplication`'s module), a no-op on iOS for now.
 */
interface ExceptionReporter {
    fun captureException(
        throwable: Throwable,
        properties: Map<String, Any> = emptyMap()
    )
}
