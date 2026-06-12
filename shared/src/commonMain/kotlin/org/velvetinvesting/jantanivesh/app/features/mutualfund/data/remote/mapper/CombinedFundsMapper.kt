package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.mapper

import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mutualfundcombined.BundleProduct
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mutualfundcombined.CombinedFundsDto
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mutualfundcombined.ItemXX
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mutualfundcombined.Metrics
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CombinedFundsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.ReturnYearsRateDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CategoryMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.utils.toTitleCase
import org.velvetinvesting.jantanivesh.app.core.utils.trimDoubleTo

fun CombinedFundsDto.toDomain(): CombinedFundsDomain {
    return CombinedFundsDomain(
        bundleFunds = data.bundle_funds.items.map { item ->
            BundledMutualFundDomain(
                categoryName = item.bundle_name,
                key = item.id,
                mutualFunds = item.bundle_products.map { it.toDomain() },
                minAmount = item.accumulated_min_amount,
                img_url = item.img_url?:""
            )
        },
        categoryMutualFundDomain = data.normal_funds.items.map { itemX ->
            CategoryMutualFundDomain(
                categorySearchReference = itemX.key,
                categoryName = itemX.title,
                mutualFunds = itemX.items.map { it.toDomain() },
            )
        }
    )
}

fun BundleProduct.toDomain(): BundledMutualFundItemDomain {
        val product = this.mf_product

       return BundledMutualFundItemDomain(
           id = product.id,
           scheme_id = product.scheme_id,
           isin = product.isin,
           mapping_code = product.mapping_code,
           nse_scheme_code = product.nse_scheme_code,
           platform_code = product.platform_code,
           scheme_name = product.scheme_name.toTitleCase(),
           amc_id = product.amc_id,
           amc_code = product.amc_code,
           amc_name = product.amc_name,
           asset_type = product.asset_type,
           scheme_type = product.scheme_type,
           structure = product.structure,
           risk_name = product.risk_name,
           risk_level = product.risk_level,
           latest_nav = product.latest_nav,
           latest_nav_date = product.latest_nav_date,
           purchase_allowed = product.purchase_allowed,
           sip_allowed = product.sip_allowed,
           redemption_allowed = product.redemption_allowed,
           switch_allowed = product.switch_allowed,
           maturity_date = product.maturity_date,
           nfo_end_date = product.nfo_end_date,
           createdAt = product.createdAt,
           updatedAt = product.updatedAt,
           allocation_percentage = allocation_percentage,
           minAmount = min_amount,
           icon = product.img_url?:""
       )
}


fun ItemXX.toDomain(): MutualFundDomain {
    return MutualFundDomain(
        id = id,
        name = scheme_name.toTitleCase(),
        icon = img_url?:"",
        category = asset_type,
        remark = null,
        riskText = risk_name,
        type = scheme_type,
        returnYearsRate = metrics.toDomain(),
        latestNav=latest_nav
    )
}

fun Metrics.toDomain(): ReturnYearsRateDomain {
    return ReturnYearsRateDomain(
        month3 = return_90d?.trimDoubleTo(2),
        month6 = return_6m?.trimDoubleTo(2),
        year1 = return_1y?.trimDoubleTo(2),
        year3 = return_3y?.trimDoubleTo(2),
    )
}
