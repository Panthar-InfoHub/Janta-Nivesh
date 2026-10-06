package org.velvetinvesting.jantanivesh.analytics

import android.util.Log
import com.posthog.PostHog
import org.velvetinvesting.jantanivesh.app.core.analytics.ExceptionReporter

class PostHogExceptionReporter : ExceptionReporter {
    override fun captureException(throwable: Throwable, properties: Map<String, Any>) {
        PostHog.captureException(throwable, properties = properties)
        Log.d("PostHogExceptionReporter", "Captured exception: $throwable")
    }
}
