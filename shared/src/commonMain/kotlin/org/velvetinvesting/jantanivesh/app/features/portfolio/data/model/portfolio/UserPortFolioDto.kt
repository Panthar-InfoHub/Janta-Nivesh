package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio

import kotlinx.serialization.Serializable

/**
 * The **previous** `GET /user/portfolio` shape. Nothing calls it any more — the app reads
 * [org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.userportfolio.UserPortfolioResponseDto]
 * instead. Kept only so the remaining references can be retired in one pass.
 */
@Serializable
data class UserPortFolioDto(
    val code: Int,
    val `data`: Data,
    val message: String
)