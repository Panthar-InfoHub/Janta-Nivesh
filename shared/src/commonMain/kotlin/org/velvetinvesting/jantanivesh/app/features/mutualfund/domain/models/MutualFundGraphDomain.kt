package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models

data class MutualFundGraphDomain(
    val graphPoints: List<MutualFundGraphPointsDomain>
)


data class MutualFundGraphPointsDomain(
    val navValue: Double,
    val date: String,
    val label:String
)
