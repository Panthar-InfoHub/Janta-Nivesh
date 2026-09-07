package org.velvetinvesting.jantanivesh.app.features.orders.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderDomain

interface OrdersRepository {

    /**
     * `GET /user/orders`.
     *
     * Every filter is optional and a null one is simply not sent, so a bare call returns the
     * whole order book — which is how the listing screen calls it. [planType] is
     * PURCHASE/REDEMPTION/SWITCH and [state] is SUCCESSFUL/ACTIVE, the only two the endpoint
     * narrows on.
     */
    suspend fun getOrders(
        planType: String? = null,
        systematic: Boolean? = null,
        state: String? = null,
        page: Int? = null,
        limit: Int? = null
    ): NetworkResponse<PaginatedData<OrderDomain>, ErrorDomain>
}
