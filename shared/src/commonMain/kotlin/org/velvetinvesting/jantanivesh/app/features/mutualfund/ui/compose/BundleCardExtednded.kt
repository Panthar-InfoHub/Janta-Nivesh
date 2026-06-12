package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.MutualFundIcon
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundItemDomain

@Composable
fun BundleCardExtended(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    bundleData: BundledMutualFundDomain
) {
    val categoryColors = listOf(Color(0xFF0A9B2A), Color(0xFFF59E00), Color(0xFF7C3AED), Color(0xFF4F7CF3), Color(0xFFE11D48), Color(0xFF14B8A6), Color(0xFFEC4899), Color(0xFF8B5CF6), Color(0xFFF97316), Color(0xFF06B6D4))
    val allocations = bundleData.getGroupedAssetAllocations()
    val shapes = LocalShapes.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(shapes.roundedDp24)
            .clip(shapes.roundedDp24)
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFE5E7EB),
                shape = shapes.roundedDp24
            )
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SubcomposeAsyncImage(
                        modifier = Modifier.size(38.dp),
                        model = bundleData.img_url,
                        contentDescription = null,
                        loading = {
                            MutualFundIcon(
                                schemeName = bundleData.categoryName,
                                size = 38.dp
                            )
                        },
                        error = {
                            MutualFundIcon(
                                schemeName = bundleData.categoryName,
                                size = 38.dp
                            )
                        },
                        success = {
                            SubcomposeAsyncImageContent()
                        }
                    )
                    Column(
                    ) {
                        Text(
                            text = bundleData.categoryName,
                            style = MaterialTheme.typography.labelSmall,
                            color = titleColor,
                            maxLines = 1
                        )
                        Text(
                            text = "Curated Fund By Velvet",
                            style = MaterialTheme.typography.bodySmall,
                            color = titleColor,
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = "Min Investment",
                        style = MaterialTheme.typography.bodySmall,
                        color = titleColor,
                    )
                    Text(
                        text = "₹ ${formatMoneyAfterL(bundleData.minAmount.toLong())}".withInterRupee(),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            ) {
                drawLine(
                    color = titleColor,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(12f, 8f),
                        phase = 0f
                    )
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Distribution by category",
                    style = MaterialTheme.typography.bodySmall,
                    color = titleColor,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(shapes.circle)
                ) {
                    allocations.forEachIndexed { index, allocation ->
                        Box(
                            modifier = Modifier
                                .weight(allocation.percentage.toFloat())
                                .fillMaxHeight()
                                .background(
                                    categoryColors[index % categoryColors.size]
                                )
                        )
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    allocations.forEachIndexed { index, allocation ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(shapes.circle)
                                    .background(
                                        categoryColors[index % categoryColors.size]
                                    )
                            )
                            Text(
                                text = "${allocation.assetType} ${allocation.percentage}%",
                                fontFamily = InterFontFamily,
                                fontSize = 10.sp,
                                color = titleColor
                            )
                        }
                    }
                }
            }
        }
    }
}

data class AssetAllocationUi(
    val assetType: String,
    val percentage: Int
)

fun BundledMutualFundDomain.getGroupedAssetAllocations(): List<AssetAllocationUi> {
    return mutualFunds
        .groupBy { fund ->
            val schemeType = fund.scheme_type
                .trim()
                .replace("fund", "", ignoreCase = true)
                .trim()

            if (schemeType.contains("other", ignoreCase = true)) {
                "Other"
            } else {
                schemeType
            }
        }
        .map { (assetType, funds) ->
            AssetAllocationUi(
                assetType = assetType,
                percentage = funds.sumOf { it.allocation_percentage }
            )
        }
        .sortedByDescending { it.percentage }
}

@Preview(showBackground = true)
@Composable
private fun BundleCardPreview() {
    val mockBundle = BundledMutualFundDomain(
        categoryName = "Velvet Preserve",
        key = "velvet_preserve",
        mutualFunds = listOf(
            BundledMutualFundItemDomain(
                id = "1",
                scheme_id = "",
                isin = "",
                mapping_code = "",
                nse_scheme_code = "",
                platform_code = "",
                scheme_name = "Debt Fund",
                amc_id = "",
                amc_code = "",
                amc_name = "",
                asset_type = "MF",
                scheme_type = "Debt",
                structure = "",
                risk_name = "",
                risk_level = 1,
                latest_nav = "",
                latest_nav_date = "",
                purchase_allowed = true,
                sip_allowed = true,
                redemption_allowed = true,
                switch_allowed = true,
                maturity_date = null,
                nfo_end_date = null,
                createdAt = "",
                updatedAt = "",
                allocation_percentage = 30,
                minAmount = "500",
                metrics = null,
                icon = ""
            ),
            BundledMutualFundItemDomain(
                id = "2",
                scheme_id = "",
                isin = "",
                mapping_code = "",
                nse_scheme_code = "",
                platform_code = "",
                scheme_name = "Hybrid Fund",
                amc_id = "",
                amc_code = "",
                amc_name = "",
                asset_type = "Hybrid",
                scheme_type = "Hybrid",
                structure = "",
                risk_name = "",
                risk_level = 2,
                latest_nav = "",
                latest_nav_date = "",
                purchase_allowed = true,
                sip_allowed = true,
                redemption_allowed = true,
                switch_allowed = true,
                maturity_date = null,
                nfo_end_date = null,
                createdAt = "",
                updatedAt = "",
                allocation_percentage = 30,
                minAmount = "500",
                metrics = null,
                icon = ""
            ),
            BundledMutualFundItemDomain(
                id = "3",
                scheme_id = "",
                isin = "",
                mapping_code = "",
                nse_scheme_code = "",
                platform_code = "",
                scheme_name = "Equity Fund",
                amc_id = "",
                amc_code = "",
                amc_name = "",
                asset_type = "Equity",
                scheme_type = "Equity",
                structure = "",
                risk_name = "",
                risk_level = 5,
                latest_nav = "",
                latest_nav_date = "",
                purchase_allowed = true,
                sip_allowed = true,
                redemption_allowed = true,
                switch_allowed = true,
                maturity_date = null,
                nfo_end_date = null,
                createdAt = "",
                updatedAt = "",
                allocation_percentage = 20,
                minAmount = "500",
                metrics = null,
                icon = ""
            ),
            BundledMutualFundItemDomain(
                id = "4",
                scheme_id = "",
                isin = "",
                mapping_code = "",
                nse_scheme_code = "",
                platform_code = "",
                scheme_name = "Gold Fund",
                amc_id = "",
                amc_code = "",
                amc_name = "",
                asset_type = "Gold",
                scheme_type = "Gold",
                structure = "",
                risk_name = "",
                risk_level = 3,
                latest_nav = "",
                latest_nav_date = "",
                purchase_allowed = true,
                sip_allowed = true,
                redemption_allowed = true,
                switch_allowed = true,
                maturity_date = null,
                nfo_end_date = null,
                createdAt = "",
                updatedAt = "",
                allocation_percentage = 20,
                minAmount = "500",
                metrics = null,
                icon = ""
            )
        ),
        minAmount = 10000.0,
        img_url = ""
    )

    JantaNiveshTheme {
        BundleCardExtended(
            bundleData = mockBundle,
            onClick = {}
        )
    }
}
