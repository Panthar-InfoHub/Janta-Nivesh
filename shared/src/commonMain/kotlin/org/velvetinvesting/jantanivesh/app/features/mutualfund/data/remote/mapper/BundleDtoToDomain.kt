package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfunds.BundledFundsDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.utils.toTitleCase

fun BundledFundsDto.toDomain(): List<BundledMutualFundDomain> {
    val bundles = this.data.bundles
    return bundles.map { bundle ->
        BundledMutualFundDomain(
            categoryName = bundle.bundle_name,
            key = bundle.id,
            img_url = bundle.bundle_products.firstOrNull()?.mf_product?.img_url?:"",
            mutualFunds = bundle.bundle_products.map { product ->
                val mf = product.mf_product
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
                    allocation_percentage = product.allocation_percentage,
                    minAmount=product.min_amount,
                    icon = mf.img_url?:""
                )
            }
        )
    }
}
