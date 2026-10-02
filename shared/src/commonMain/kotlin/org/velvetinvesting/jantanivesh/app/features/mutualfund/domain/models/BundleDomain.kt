package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models

data class BundleDomain(
    val id: String,
    val name: String,
    val description: String,
    val equityPercentage: Int,
    val commodityPercentage: Int,
    val debtPercentage: Int,
    val hybridPercentage: Int,
    val imgUrl: String,
    val metaData: BundleMetaDataDomain,
    val categories: List<BundleCategoryDomain>
) {
    /** Each slot is one fund in the bundle, so the fund count is the slot count. */
    val fundCount: Int
        get() = categories.sumOf { it.slots.size }

    /** The asset classes the bundle actually holds, e.g. "Equity + Commodity + Hybrid". */
    val assetClassLabel: String
        get() = listOf(
            "Equity" to equityPercentage,
            "Commodity" to commodityPercentage,
            "Debt" to debtPercentage,
            "Hybrid" to hybridPercentage
        )
            .filter { (_, percentage) -> percentage > 0 }
            .joinToString(" + ") { (name, _) -> name }
}

data class BundleMetaDataDomain(
    val riskLevel: String,
    val investmentTime: String,
    val investmentGrowth: String
)

data class BundleCategoryDomain(
    val id: String,
    val categoryName: String,
    val displayName: String,
    val totalPercentage: Int,
    val slots: List<BundleSlotDomain>
)

data class BundleSlotDomain(
    val id: String,
    val allocationPercentage: Int,
    val defaultRank: Int
)
