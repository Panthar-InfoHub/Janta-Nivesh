package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models


data class MutualFundDomain(
    val id: String,
    val name: String,
    val icon: String,
    val category: String,
    val remark: String?,
    val riskText: String?,
    val type: String,
    val returnYearsRate: ReturnYearsRateDomain,
    val latestNav: String,
)

data class ReturnYearsRateDomain(
    val month3:Double?,
    val month6:Double?,
    val year1:Double?,
    val year3:Double?,
)