package org.velvetinvesting.jantanivesh.app.core.analytics

/** PostHog is not wired up on iOS yet, so exceptions are dropped. */
class IosExceptionReporter : ExceptionReporter {
    override fun captureException(throwable: Throwable, properties: Map<String, Any>) {
    }
}
