package org.velvetinvesting.jantanivesh.app.features.core.domain.usecase

import io.ktor.client.HttpClient
import org.velvetinvesting.jantanivesh.app.core.networking.clearAuthTokens
import org.velvetinvesting.jantanivesh.app.core.platform.SharedPreference
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.search.domain.repository.RecentSearchRepo

/**
 * Everything that has to go when the user signs out, in one place, so the two ways out of the app
 * — the log out button and an expired token — leave the device in the same state: nothing of the
 * last login on disk, in memory, or on the next request.
 */
class LogoutUseCase(
    private val preferences: SharedPreference,
    private val recentSearchRepo: RecentSearchRepo,
    private val httpClient: HttpClient
) {
    suspend operator fun invoke() {
        // The in-memory copy first: the provider keeps whatever `loadTokens` handed it, so wiping
        // storage alone would still leave the old bearer token on the next request.
        httpClient.clearAuthTokens()

        // Every key the app stores lives in this one store, so this covers the tokens, the
        // onboarding stage, the profile fields and the PIN/biometric flags in a single call.
        preferences.clear()

        // Search terms are the one thing cached locally that names what the last user looked at.
        recentSearchRepo.clearRecentSearches()

        // A queued log out event would otherwise be replayed to the next session's collector.
        AppEventsController.clear()
    }
}
