package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models

data class FDDetailsDomain(
    val bankInfo: FDBankInfoDomain,
    val investmentDetails: FDInvestmentDetailsDomain,
    val nomineeDetails: List<FDNomineeDomain>,
    val timelineDetails: FDTimelineDomain
)
