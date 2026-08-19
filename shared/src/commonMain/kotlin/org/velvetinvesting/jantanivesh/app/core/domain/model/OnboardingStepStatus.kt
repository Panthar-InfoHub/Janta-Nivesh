package org.velvetinvesting.jantanivesh.app.core.domain.model

/**
 * Per-step state the server reports alongside the current onboarding stage.
 *
 * [SKIPPED] is not a failure: the user was allowed to defer that step, and the app is expected to
 * carry on without it and pick the step back up from inside the main flow.
 */
enum class OnboardingStepStatus {
    PENDING,
    VERIFIED,
    SKIPPED,

    /** The server sent something this build does not know about — treated as not done. */
    UNKNOWN;

    companion object {
        fun fromValue(raw: String?): OnboardingStepStatus =
            entries.firstOrNull { it.name.equals(raw?.trim(), ignoreCase = true) } ?: UNKNOWN
    }
}
