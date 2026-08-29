package org.velvetinvesting.jantanivesh.app.features.auth.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeUnitRequest
import org.velvetinvesting.jantanivesh.app.features.auth.data.remote.model.UpdateMpinBodyDto
import org.velvetinvesting.jantanivesh.app.features.auth.data.remote.model.VerifyMpinBodyDto
import org.velvetinvesting.jantanivesh.app.features.auth.domain.repository.MpinRepo

class MpinRepoImpl(
    private val client: HttpClient
) : MpinRepo {

    override suspend fun updateMpin(mpin: String): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.patch(getUrl("/user/")) {
                setBody(UpdateMpinBodyDto(mpin = mpin))
            }
        }
    }

    override suspend fun verifyMpin(mpin: String): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/user/verify-mpin")) {
                setBody(VerifyMpinBodyDto(mpin = mpin))
            }
        }
    }
}
