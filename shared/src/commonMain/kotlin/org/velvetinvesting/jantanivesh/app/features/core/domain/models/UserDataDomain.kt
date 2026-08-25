package org.velvetinvesting.jantanivesh.app.features.core.domain.models

import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserFinance

/**
 * Everything `GET /user/` tells the app about the signed-in user: who they are, how far through
 * onboarding they got, and what their portfolio is worth.
 */
data class UserDataDomain(
    val id: String,
    val name: String,
    val email: String,
    val mobile: String,
    val dob: String,
    val city: String?,
    val goals: List<GoalsSummaryDomain>,
    val onboarding: OnboardingStatusDomain,
    /** The user chose to skip onboarding rather than finishing it. */
    val isSkipped: Boolean,
    val dashboard: DashboardSummaryDomain,
    /** Gateway ids, present only once KYC has cleared. */
    val investorProfile: String?,
    val investmentAccount: String?,
    val userFinance: UserFinance?,
) {
    val kycVerified: Boolean
        get() = onboarding.isKycVerified

    /**
     * True when onboarding was skipped or never finished — which is what the home screen's
     * "complete your KYC" prompt keys on. A user can skip into the app and invest nothing until
     * this clears, so the prompt has to stay up for both cases, not just an unverified KYC.
     */
    val needsOnboarding: Boolean
        get() = isSkipped

    /**
     * Not reported by `GET /user/` any more — the trading account was removed from the onboarding
     * flow. Kept so the profile screen still compiles; it always reads as unverified.
     */
}

/**
 * Per-step onboarding progress. Statuses are compared case-insensitively because the server has
 * used both casings across endpoints.
 */
data class OnboardingStatusDomain(
    val currentStage: String,
    val basicDetailsStatus: String,
    val readinessStatus: String,
    val kycStatus: String,
    val pennyDropStatus: String,
    val emailStatus: String,
    val profileStatus: String,
    val nomineeStatus: String,
    val isCompleted: Boolean,
    val completedAt: String?
) {
    val isKycVerified: Boolean
        get() = kycStatus.isVerified

    val isBankVerified: Boolean
        get() = pennyDropStatus.isVerified

    val isEmailVerified: Boolean
        get() = emailStatus.isVerified

    val isProfileComplete: Boolean
        get() = profileStatus.isVerified

    /** Skipping a nominee is a valid outcome, so it counts as settled rather than as pending. */
    val isNomineeSettled: Boolean
        get() = nomineeStatus.isVerified || nomineeStatus.equals(SKIPPED, ignoreCase = true)

    private val String.isVerified: Boolean
        get() = equals(VERIFIED, ignoreCase = true)

    companion object {
        const val VERIFIED = "VERIFIED"
        const val SKIPPED = "SKIPPED"
        const val PENDING = "PENDING"
        const val COMPLETED = "COMPLETED"

        val EMPTY = OnboardingStatusDomain(
            currentStage = "",
            basicDetailsStatus = "",
            readinessStatus = "",
            kycStatus = "",
            pennyDropStatus = "",
            emailStatus = "",
            profileStatus = "",
            nomineeStatus = "",
            isCompleted = false,
            completedAt = null
        )
    }
}

/** The portfolio figures the home screen shows, in rupees. */
data class DashboardSummaryDomain(
    val portfolioValue: Double = 0.0,
    val mutualFunds: Double = 0.0,
    val fixedDeposits: Double = 0.0,
    val totalReturns: Double = 0.0,
    val returnPercent: Double = 0.0,
    /** Null until there is a previous month to compare against. */
    val monthChangePercent: Double? = null
)
