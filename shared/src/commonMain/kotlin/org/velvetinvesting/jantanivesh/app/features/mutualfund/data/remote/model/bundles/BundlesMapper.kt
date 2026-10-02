package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundles

import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundleCategoryDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundleDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundleMetaDataDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundleSlotDomain

fun BundlesDto.toDomain(): List<BundleDomain> = data.bundles.map { it.toDomain() }

fun BundleDto.toDomain(): BundleDomain {
    return BundleDomain(
        id = id,
        name = bundle_name,
        description = bundle_description.orEmpty(),
        equityPercentage = equity_percentage,
        commodityPercentage = commodity_percentage,
        debtPercentage = debt_percentage,
        hybridPercentage = hybrid_percentage,
        imgUrl = img_url.orEmpty(),
        metaData = BundleMetaDataDomain(
            riskLevel = meta_data?.risk_level.orEmpty(),
            investmentTime = meta_data?.investment_time.orEmpty(),
            investmentGrowth = meta_data?.investment_growth.orEmpty()
        ),
        categories = categories.map { it.toDomain() }
    )
}

private fun BundleCategoryDto.toDomain(): BundleCategoryDomain {
    return BundleCategoryDomain(
        id = id,
        categoryName = category_name,
        displayName = display_name,
        totalPercentage = total_percentage,
        slots = slots
            .sortedBy { it.default_rank }
            .map { slot ->
                BundleSlotDomain(
                    id = slot.id,
                    allocationPercentage = slot.allocation_percentage,
                    defaultRank = slot.default_rank
                )
            }
    )
}
