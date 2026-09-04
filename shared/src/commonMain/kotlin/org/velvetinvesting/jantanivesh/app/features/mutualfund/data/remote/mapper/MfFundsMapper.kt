package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.core.utils.trimDoubleTo
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mffunds.MfFundDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mffunds.MfFundMetricsDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mffunds.MfFundsDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.ReturnYearsRateDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.utils.toTitleCase

/**
 * `GET /mf/funds` -> the paged list the search screen renders.
 *
 * A response missing `data` or `pagination` maps to an empty first page with no next page,
 * rather than throwing: an empty result and a malformed one both mean "nothing to show".
 */
fun MfFundsDto.toPaginatedDomain(): PaginatedData<MutualFundDomain> {
    val pagination = data?.pagination

    val page = pagination?.page ?: 1
    val totalPages = pagination?.total_pages ?: 0

    return PaginatedData(
        items = data?.funds.orEmpty().map { it.toDomain() },
        page = page,
        pageSize = pagination?.limit ?: 0,
        totalItems = pagination?.total ?: 0,
        totalPages = totalPages,
        hasNextPage = page < totalPages
    )
}

/**
 * This endpoint carries no category, risk or scheme type, so those stay at their defaults —
 * the list card falls back to the NAV line when it has no subtitle to build.
 */
fun MfFundDto.toDomain(): MutualFundDomain {
    return MutualFundDomain(
        id = id,
        name = name?.toTitleCase().orEmpty(),
        icon = img_url.orEmpty(),
        returnYearsRate = metrics.toReturnDomain(),
        latestNav = latest_nav.orEmpty(),
        isin = isin,
        latestNavDate = latest_nav_date
    )
}

fun MfFundMetricsDto?.toReturnDomain(): ReturnYearsRateDomain {
    return ReturnYearsRateDomain(
        month1 = this?.return_30d?.trimDoubleTo(2),
        month3 = this?.return_90d?.trimDoubleTo(2),
        month6 = this?.return_6m?.trimDoubleTo(2),
        year1 = this?.return_1y?.trimDoubleTo(2),
        year3 = this?.return_3y?.trimDoubleTo(2),
        year5 = this?.return_5y?.trimDoubleTo(2),
        navChangePct = this?.nav_change_pct?.trimDoubleTo(2)
    )
}
