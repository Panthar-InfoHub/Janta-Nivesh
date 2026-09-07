package org.velvetinvesting.jantanivesh.app.features.orders.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.orders.data.remote.mapper.toPaginatedDomain
import org.velvetinvesting.jantanivesh.app.features.orders.data.remote.model.UserOrdersDto
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderDomain
import org.velvetinvesting.jantanivesh.app.features.orders.domain.repository.OrdersRepository

class OrdersRepo(
    private val client: HttpClient
) : OrdersRepository {

    override suspend fun getOrders(
        planType: String?,
        systematic: Boolean?,
        state: String?,
        page: Int?,
        limit: Int?
    ): NetworkResponse<PaginatedData<OrderDomain>, ErrorDomain> {

        val response = safeRequest<UserOrdersDto> {
            client.get(getUrl("/user/orders")) {
                // A blank filter is not a filter: sending one would narrow the list server-side
                // to nothing, so only the ones actually set are put on the query.
                planType?.takeIf { it.isNotBlank() }?.let { parameter("plan_type", it) }
                systematic?.let { parameter("systematic", it) }
                state?.takeIf { it.isNotBlank() }?.let { parameter("state", it) }
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
            }
        }

        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toPaginatedDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }
}
