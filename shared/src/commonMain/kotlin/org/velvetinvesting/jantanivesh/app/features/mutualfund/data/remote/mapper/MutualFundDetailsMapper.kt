package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfdetails.MutualFundsDetailDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.InvestmentFrequency
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.Metrics as DomainMetrics

fun MutualFundsDetailDto.toDomain(): MutualFundDetailsDomain {
    val d = data

    return MutualFundDetailsDomain(
        amc_code = d.amc_code?:"",
        amc_id = d.amc_id?:"",
        amc_name = d.amc_name?:"",
        asset_type = d.asset_type?:"",
        createdAt = d.createdAt?:"",
        id = d.id,
        isin = d.isin,
        latest_nav = d.latest_nav?:"n/a",
        latest_nav_date = d.latest_nav_date?:"",
        mapping_code = d.mapping_code?:"",
        maturity_date = d.maturity_date,
        metrics = d.metrics?.let {
             DomainMetrics(
                 nav_change_pct = it.nav_change_pct,
                 return_1y = it.return_1y,
                 return_30d = it.return_30d,
                 return_3y = it.return_3y,
                 return_6m = it.return_6m,
                 return_90d = it.return_90d,
                 return_5y = it.return_5y
             )
        }?: DomainMetrics(0.0, null, null, null, null,null, null),
        nfo_end_date = d.nfo_end_date,
        nse_scheme_code = d.nse_scheme_code?:"",
        platform_code = d.platform_code?:"",
        purchase_allowed = d.purchase_allowed?: true,
        redemption_allowed = d.redemption_allowed?: true,
        risk_level = d.risk_level?:-1,
        risk_name = d.risk_name?:"n/a",
        scheme_id = d.scheme_id?:"",
        scheme_name = d.scheme_name?:"",
        scheme_type = d.scheme_type?:"",
        sip_allowed = d.sip_allowed?:false,
        structure = d.structure?:"",
        switch_allowed = d.switch_allowed?: false,
        sipAllowedDated = d.transaction_rules?.sip_allowed_dates?:emptyList(),
        investmentFrequency = d.transaction_rules?.sip_frequencies?.mapNotNull { InvestmentFrequency.fromCode(it) }?: emptyList(),
        updatedAt = d.updatedAt?:"",
        icon = d.img_url?:"",
        minAmount = d.transaction_rules?.min_investment_amount?.toLong()?:0,
        minSipAmount = d.transaction_rules?.min_sip_amount?.toLong()?:0,
        minLumpSumAmount = d.transaction_rules?.min_lump_sum_amount?.toLong()?:0,
    )
}