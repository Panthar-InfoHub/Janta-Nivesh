package org.velvetinvesting.jantanivesh.app.features.goals.domain.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.features.goals.data.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.portfolio.UserPortFolioDto
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.PortfolioDomain

interface UserFinance {
    suspend fun getPortfolio(): NetworkResponse<PortfolioDomain, ErrorDomain>
}

class UserFinanceRepo(
    private val client: HttpClient
) : UserFinance {
    override suspend fun getPortfolio(): NetworkResponse<PortfolioDomain, ErrorDomain> {
        val response = safeRequest<UserPortFolioDto> {
            client.get(getUrl("/user/portfolio"))
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
        }
    }
}
