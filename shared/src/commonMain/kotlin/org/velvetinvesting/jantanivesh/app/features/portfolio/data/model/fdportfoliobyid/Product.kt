package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.fdportfoliobyid

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val issuer: Issuer,
    val issuer_id: String
)