package org.velvetinvesting.jantanivesh.app.features.login.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.core.utils.deviceinfo.DeviceInfoRetriever
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.login.data.mapper.toLoginDomain
import org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.sendotp.SendOtpDto
import org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp.VerifyOtpBodyDto
import org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp.VerifyOtpDto
import org.velvetinvesting.jantanivesh.app.features.login.domain.model.LoginDomain
import org.velvetinvesting.jantanivesh.app.features.login.domain.repository.UserAuth

class UserAuthenticationRepo(
    private val client: HttpClient,
    private val authPrefs: AuthPrefs,
    private val deviceInfoRetriever: DeviceInfoRetriever
) : UserAuth {

    override suspend fun loginWithNumber(number: String): NetworkResponse<Unit, ErrorDomain> {
        val deviceInfo = deviceInfoRetriever.getDeviceInfo()
        val response = safeRequest<SendOtpDto> {
            client.post(getUrl("/auth/req-otp")) {
                parameter("dtyp", deviceInfo.deviceType)
                parameter("dver", deviceInfo.deviceVersion)
                parameter("dbn", deviceInfo.deviceBuildNumber.take(6))
                parameter("did", deviceInfo.deviceId)
                parameter("mob", number)
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun verifyOTP(
        number: String,
        otp: String
    ): NetworkResponse<LoginDomain, ErrorDomain> {
        val deviceInfo = deviceInfoRetriever.getDeviceInfo()

        val response = safeRequest<VerifyOtpDto> {
            client.post(getUrl("/auth/validate-otp")) {
                parameter("dtyp", deviceInfo.deviceType)
                parameter("dver", deviceInfo.deviceVersion)
                parameter("dbn", deviceInfo.deviceBuildNumber)
                parameter("did", deviceInfo.deviceId)

                setBody(
                    VerifyOtpBodyDto(
                        mob = number,
                        otp = otp
                    )
                )
            }
        }

        return when (response) {
            is NetworkResponse.Success -> {
                val dto = response.data.data

                authPrefs.setBearerToken(dto.token)
                authPrefs.setRefreshToken(dto.refresh_token)
                authPrefs.setUserId(dto.user.user_id)
                authPrefs.setOnboardingCompleted(dto.user.metadata.is_onboarding_completed)
                authPrefs.setLoggedIn(true)
                authPrefs.setPhoneNumber(dto.user.phone_no)

                NetworkResponse.Success(
                    response.data.toLoginDomain()
                )
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }
}
