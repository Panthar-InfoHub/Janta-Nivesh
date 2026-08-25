package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.core.utils.trimDoubleTo
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.frontendmfdata.FrontendMfDataDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.frontendmfdata.FundSectionDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.frontendmfdata.SectionFundDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.frontendmfdata.SectionFundMetricsDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CategoryMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.ReturnYearsRateDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.utils.toTitleCase

fun FrontendMfDataDto.toDomain(): List<CategoryMutualFundDomain> {
    return data.map { (key, section) -> section.toDomain(key) }
        .filter { it.mutualFunds.isNotEmpty() }
}

fun FundSectionDto.toDomain(fallbackKey: String): CategoryMutualFundDomain {
    return CategoryMutualFundDomain(
        categoryName = title,
        categorySearchReference = tag.ifBlank { fallbackKey },
        mutualFunds = funds.map { it.toDomain() }
    )
}

fun SectionFundDto.toDomain(): MutualFundDomain {
    return MutualFundDomain(
        id = id,
        name = name.toTitleCase(),
        icon = img_url ?: "",
        returnYearsRate = metrics.toReturnDomain(),
        latestNav = latest_nav ?: "",
        isin = isin,
        latestNavDate = latest_nav_date,
    )
}

fun SectionFundMetricsDto?.toReturnDomain(): ReturnYearsRateDomain {
    return ReturnYearsRateDomain(
        month1 = this?.return_30d?.trimDoubleTo(2),
        month3 = this?.return_90d?.trimDoubleTo(2),
        month6 = this?.return_6m?.trimDoubleTo(2),
        year1 = this?.return_1y?.trimDoubleTo(2),
        year3 = this?.return_3y?.trimDoubleTo(2),
        year5 = this?.return_5y?.trimDoubleTo(2),
        navChangePct = this?.nav_change_pct?.trimDoubleTo(2),
    )
}
