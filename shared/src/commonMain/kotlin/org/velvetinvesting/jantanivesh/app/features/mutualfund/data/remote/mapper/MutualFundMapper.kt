package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfundbyid.BundledFundByIdDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.initiatemfpurchase.Data
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.FundMetricsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.InvestmentFrequency
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MandateStatus
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundPurchaseInitiateDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.utils.toTitleCase

fun BundledFundByIdDto.toDomain(): BundledMutualFundDomain {
    return BundledMutualFundDomain(
        categoryName = data.bundle_name,
        key = data.id,
        img_url = data.img_url?:"",
        allowedFrequencies = data.allowed_frequencies.mapNotNull {
            InvestmentFrequency.fromCode(it)
        },
        minAmount = data.accumulated_min_amount,
        sipDates = data.allowed_dates,
        mutualFunds = data.bundle_products.map { bundleProduct ->
            val mf = bundleProduct.mf_product

            BundledMutualFundItemDomain(
                id = mf.id,
                scheme_id = mf.scheme_id,
                isin = mf.isin,
                mapping_code = mf.mapping_code,
                nse_scheme_code = mf.nse_scheme_code,
                platform_code = mf.platform_code,
                scheme_name = mf.scheme_name.toTitleCase(),
                amc_id = mf.amc_id,
                amc_code = mf.amc_code,
                amc_name = mf.amc_name,
                asset_type = mf.asset_type,
                scheme_type = mf.scheme_type,
                structure = mf.structure,
                risk_name = mf.risk_name,
                risk_level = mf.risk_level,
                latest_nav = mf.latest_nav,
                latest_nav_date = mf.latest_nav_date,
                purchase_allowed = mf.purchase_allowed,
                sip_allowed = mf.sip_allowed,
                redemption_allowed = mf.redemption_allowed,
                switch_allowed = mf.switch_allowed,
                maturity_date = mf.maturity_date,
                nfo_end_date = mf.nfo_end_date,
                createdAt = mf.createdAt,
                updatedAt = mf.updatedAt,
                allocation_percentage = bundleProduct.allocation_percentage,
                minAmount = bundleProduct.min_amount,
                metrics = FundMetricsDomain(
                    return1Y = mf.metrics.return_1y,
                    return3Y = mf.metrics.return_3y,
                    return6M = mf.metrics.return_6m,
                    return90D = mf.metrics.return_90d
                ),
                icon = mf.img_url?:""
            )
        }
    )
}

fun Data.toDomain(): MutualFundPurchaseInitiateDomain {
    return MutualFundPurchaseInitiateDomain(
        mandateId = mandate_id,
        url = mandate_short_url,
        status = if (status=="MANDATE_APPROVED") MandateStatus.APPROVED else MandateStatus.PENDING,
    )
}