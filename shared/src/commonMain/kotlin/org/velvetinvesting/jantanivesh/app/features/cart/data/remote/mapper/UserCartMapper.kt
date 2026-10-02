package org.velvetinvesting.jantanivesh.app.features.cart.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.usercart.CartItem
import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.usercart.UserCartDto
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.CartType
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.LumpSumItemDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.SipDetails
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.UserCartDomain

fun UserCartDto.toDomain(): UserCartDomain {
    return UserCartDomain(
        sipItems = data.sip.map { it.toSipDomain() },
        lumpSumItems = data.lumpsum.map { it.toLumpSumDomain() }
    )
}

/** Amounts come back as decimal strings (`"1000"`, `"99999999999.99"`). */
private fun String.toAmount(): Long = toDoubleOrNull()?.toLong() ?: 0

fun CartItem.toSipDomain(): SipItemDomain {
    val sipAmount = amount.toAmount()
    return SipItemDomain(
        id = id,
        mfProductId = mf_product_id,
        productName = mf_product.name,
        amount = sipAmount,
        type = CartType.SIP,
        date = createdAt,
        sipDetails = SipDetails(
            startDate = "",
            endDate = "",
            frequency = frequency.orEmpty(),
            day = installment_day ?: 0,
            sipAmount = sipAmount
        ),
        imageUrl = mf_product.img_url.orEmpty()
    )
}

fun CartItem.toLumpSumDomain(): LumpSumItemDomain {
    return LumpSumItemDomain(
        id = id,
        mfProductId = mf_product_id,
        productName = mf_product.name,
        amount = amount.toAmount(),
        type = CartType.LUMPSUM,
        date = createdAt,
        imageUrl = mf_product.img_url.orEmpty()
    )
}
