package org.velvetinvesting.jantanivesh.app.features.onboarding.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeUnitRequest
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.remote.mapper.toDto
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.OnboardingDomain
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo

class OnboardingRepoImpl(
    private val client: HttpClient,
    private val authPrefs: AuthPrefs
): OnboardingRepo {
    override suspend fun onBoardUser(onboardingDomain: OnboardingDomain): NetworkResponse<Unit, ErrorDomain> {
        val response= safeUnitRequest {
            client.post(
                getUrl("/onboarding/complete-onboarding")
            ) {
                setBody(onboardingDomain.toDto())
            }
        }

        when(response){
            is NetworkResponse.Error -> {
                return NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success ->{
                authPrefs.setOnboardingCompleted(true)
                authPrefs.setLoggedIn(true)
                return response
            }
        }
    }
}