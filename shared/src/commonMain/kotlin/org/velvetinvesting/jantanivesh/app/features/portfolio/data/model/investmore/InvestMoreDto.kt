package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.investmore

import kotlinx.serialization.Serializable

@Serializable
data class InvestMoreDto(
    val type: String,
    val items: List<InvestMoreItemDto>
)

@Serializable
data class InvestMoreItemDto(
    val scheme_id: String,
    val amount: Long,
    val folio: String
)
