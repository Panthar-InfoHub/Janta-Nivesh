package org.velvetinvesting.jantanivesh.app.features.cart.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.UserCartDomain

/**
 * The number of funds in the user's cart, SIP and one-time together, for any screen that shows it.
 *
 * Every successful [GetUserCartUseCase][org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases.GetUserCartUseCase]
 * call updates it, so a change made on the cart screen reaches a screen further back in the stack
 * without that screen reloading the cart itself.
 */
object CartCountController {

    private val _cartFundCount = MutableStateFlow(0)
    val cartFundCount: StateFlow<Int> = _cartFundCount.asStateFlow()

    fun update(cart: UserCartDomain) {
        _cartFundCount.value = cart.sipItems.size + cart.lumpSumItems.size
    }

    /** Back to an empty cart, so the next user doesn't see the last one's count. */
    fun clear() {
        _cartFundCount.value = 0
    }
}
