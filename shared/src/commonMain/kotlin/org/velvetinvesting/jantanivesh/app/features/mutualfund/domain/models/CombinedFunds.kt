package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models


data class CombinedFundsDomain(
    val bundleFunds: List<BundledMutualFundDomain> = emptyList(),
    val categoryMutualFundDomain: List<CategoryMutualFundDomain> = emptyList()
)
