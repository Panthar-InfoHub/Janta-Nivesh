package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.allbundles

import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.InvestmentFrequency
import org.velvetinvesting.jantanivesh.app.features.mutualfund.utils.toTitleCase

fun AllBundlesDto.toDomain(): List<BundledMutualFundDomain> {
    return data.bundles.map { bundleData ->
        bundleData.toDomain()
    }
}

fun Data.toDomain(): BundledMutualFundDomain {
    return BundledMutualFundDomain(
        categoryName = bundle_name,
        key = id,
        mutualFunds = bundle_products.map { product ->
            product.toDomain()
        },
        minAmount = accumulated_min_amount.toDouble(), // Not provided in DTO
        allowedFrequencies = listOf(InvestmentFrequency.MONTHLY),
        img_url = img_url?:""
    )
}

fun BundleProduct.toDomain(): BundledMutualFundItemDomain {
    return BundledMutualFundItemDomain(
        id = id,
        scheme_id = mf_product.scheme_id,
        isin = mf_product.isin,
        mapping_code = mf_product.mapping_code,
        nse_scheme_code = mf_product.nse_scheme_code,
        platform_code = mf_product.platform_code,
        scheme_name = mf_product.scheme_name.toTitleCase(),
        amc_id = mf_product.amc_id,
        amc_code = mf_product.amc_code,
        amc_name = mf_product.amc_name,
        asset_type = mf_product.asset_type,
        scheme_type = mf_product.scheme_type,
        structure = mf_product.structure,
        risk_name = mf_product.risk_name,
        risk_level = mf_product.risk_level,
        latest_nav = mf_product.latest_nav,
        latest_nav_date = mf_product.latest_nav_date,
        purchase_allowed = mf_product.purchase_allowed,
        sip_allowed = mf_product.sip_allowed,
        redemption_allowed = mf_product.redemption_allowed,
        switch_allowed = mf_product.switch_allowed,
        maturity_date = mf_product.maturity_date,
        nfo_end_date = mf_product.nfo_end_date,
        createdAt = mf_product.createdAt,
        updatedAt = mf_product.updatedAt,
        allocation_percentage = allocation_percentage,
        minAmount = min_amount,
        icon = mf_product.img_url?:""
    )
}