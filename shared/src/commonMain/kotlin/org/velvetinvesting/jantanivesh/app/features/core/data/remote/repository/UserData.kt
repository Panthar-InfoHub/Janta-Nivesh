package org.velvetinvesting.jantanivesh.app.features.core.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.features.core.data.local.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserDataDto
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.UserDataDomain
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.UserDataRepo

class UserDataRepoImpl(
    private val client: HttpClient
): UserDataRepo {
    override suspend fun getUserData(): NetworkResponse<UserDataDomain, ErrorDomain> {
        val response= safeRequest<UserDataDto> {
            client.get(
                getUrl("/user")
            )
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }
        }
    }
}