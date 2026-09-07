package org.velvetinvesting.jantanivesh.app.features.orders.domain.usecase

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderDomain
import org.velvetinvesting.jantanivesh.app.features.orders.domain.repository.OrdersRepository

/**
 * The order book behind My Orders.
 *
 * The screen asks for everything and does its own filtering, because the endpoint's `state`
 * filter only knows SUCCESSFUL and ACTIVE while the chips also offer pending and failed. The
 * parameters are still carried through so a caller that wants a narrower read — only SIPs, only
 * redemptions — can ask for one.
 */
class GetOrdersUseCase(
    private val repository: OrdersRepository
) {

    suspend operator fun invoke(
        planType: String? = null,
        systematic: Boolean? = null,
        state: String? = null,
        page: Int? = 1,
        limit: Int? = 20
    ): NetworkResponse<PaginatedData<OrderDomain>, ErrorDomain> = repository.getOrders(
        planType = planType,
        systematic = systematic,
        state = state,
        page = page,
        limit = limit
    )
}
