package org.velvetinvesting.jantanivesh.app.features.bundles.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.view_bundle
import jantanivesh.shared.generated.resources.ic_bundle_card_fund
import jantanivesh.shared.generated.resources.ic_pointer_right
import jantanivesh.shared.generated.resources.rupee_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardFundsIcon
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardFundsIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardPriceIcon
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardPriceIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardSubtitle
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardTitle
import org.velvetinvesting.jantanivesh.app.core.theme.IconSize
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.AssetAllocationDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleCategorySummaryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleMetaDataDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleRisk
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleRiskStyle
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleSummaryDomain

/** A bundle card's palette in Compose colours, read from its risk's domain style. */
data class BundleCardStyle(
    val outline: Color,
    val background: Color,
    /** Text on the card's tint, like the risk pill's label. */
    val accent: Color
) {
    /** The risk pill: a slightly deeper shade of the card, so it reads as part of it. */
    val pillBackground: Color
        get() = lerp(background, outline, PILL_TINT)

    private companion object {
        const val PILL_TINT = 0.06f
    }
}

fun BundleRiskStyle.toCardStyle(): BundleCardStyle = BundleCardStyle(
    outline = Color(outline),
    background = Color(background),
    accent = Color(accent)
)

/** The card palette for this bundle's risk, or a neutral one when the risk isn't recognised. */
val BundleSummaryDomain.cardStyle: BundleCardStyle
    get() = (metaData.risk.style).toCardStyle()

/** The bundle's risk, as a pill at the top of its card; nothing when the risk isn't recognised. */
@Composable
fun BundleRiskPill(
    risk: String,
    style: BundleCardStyle,
    modifier: Modifier = Modifier
) {
    Text(
        text = risk,
        style = tinyLabel.copy(fontWeight = FontWeight.SemiBold),
        color = style.accent,
        modifier = modifier
            .clip(LocalShapes.current.circle)
            .background(style.pillBackground)
            .border(0.6.dp, style.outline, LocalShapes.current.circle)
            .padding(horizontal = Spacing.dp12, vertical = Spacing.dp6)
    )
}

@Composable
fun BundleCard(
    bundle: BundleSummaryDomain,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shapes = LocalShapes.current
    val style = bundle.cardStyle

    Column(
        modifier = modifier
            .clip(shapes.roundedDp20)
            .background(style.background)
            .border(1.dp, style.outline, shapes.roundedDp20)
            .clickable(onClick = onClick)
            .padding(Spacing.dp20),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp4),
            modifier = Modifier.padding(bottom = Spacing.dp16)
        ) {
            BundleRiskPill(
                risk = bundle.metaData.riskLevel,
                style = style,
                modifier = Modifier.padding(bottom = Spacing.dp8)
            )
            Text(
                text = bundle.name,
                style= MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = BundleCardTitle
            )
            if (bundle.description.isNotBlank()) {
                Text(
                    text = bundle.description,
                    style= tinyLabel,
                    color = BundleCardSubtitle
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
            modifier = Modifier.fillMaxWidth()
        ){
            BundleInfoRow(
                icon = painterResource(Res.drawable.ic_bundle_card_fund),
                iconTint = BundleCardFundsIcon,
                iconBackground = BundleCardFundsIconBg
            ) {
                Text(
                    text = "${bundle.fundCount} Mutual Funds",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = BundleCardTitle
                )
                if (bundle.assetClassLabel.isNotBlank()) {
                    Text(
                        text = bundle.assetClassLabel,
                        style = tinyLabel,
                        color = BundleCardSubtitle
                    )
                }
            }

            // NEW: this card always shows the monthly SIP start amount (was start_amount).
            val monthlyStartAmount = bundle.metaData.monthlyStartAmount
            if (monthlyStartAmount != null) {
                BundleInfoRow(
                    icon = painterResource(Res.drawable.rupee_icon),
                    iconTint = BundleCardPriceIcon,
                    iconBackground = BundleCardPriceIconBg
                ) {
                    Text(
                        text = "Start From",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = BundleCardSubtitle
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Black
                                )
                            ) {
                                append("₹")
                                append(monthlyStartAmount.toString())
                            }

                            withStyle(
                                SpanStyle(
                                    fontSize = 11.sp
                                )
                            ) {
                                append("/month")
                            }
                        },
                        style = tinyLabel,
                        color = BundleCardSubtitle
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.dp16, bottom = Spacing.dp12)
                .height(1.dp)
                .background(style.outline.copy(alpha = 0.4f))
        )

        Button(
            onClick = onClick,
            shape = shapes.roundedDp12,
            colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = White),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "View Bundle/" + stringResource(Res.string.view_bundle),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Icon(
                painter = painterResource(Res.drawable.ic_pointer_right),
                contentDescription = null,
                tint = White,
                modifier = Modifier.padding(start = 10.dp).size(16.dp)
            )
        }
    }
}

@Composable
private fun BundleInfoRow(
    icon: Painter,
    iconTint: Color,
    iconBackground: Color,
    modifier: Modifier= Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier=modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(LocalShapes.current.circle)
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(IconSize.dp12)
            )
        }
        Column { content() }
    }
}

internal val previewBundleSummary = BundleSummaryDomain(
    id = "1",
    name = "Equity + (Gold & Silver)",
    description = "Janta Nivesh Equity + (Gold & Silver)",
    imageUrl = "",
    assetAllocation = AssetAllocationDomain(equity = 70.0, debt = 0.0, hybrid = 5.0, commodity = 25.0),
    metaData = BundleMetaDataDomain(
        riskLevel = "HIGH RISK",
        investmentTime = "",
        investmentGrowth = "",
        risk = BundleRisk.HIGH,
        startAmount = 1000L,
        // NEW
        dailyStartAmount = 50L,
        monthlyStartAmount = 500L
    ),
    categories = listOf(
        BundleCategorySummaryDomain("c1", "mid_cap", "Mid Cap", 25.0, slotCount = 2),
        BundleCategorySummaryDomain("c2", "multi_cap", "Multi Cap", 10.0, slotCount = 1),
        BundleCategorySummaryDomain("c3", "global_others", "Commodity", 25.0, slotCount = 2)
    )
)

@Preview(showBackground = true)
@Composable
private fun BundleCardPreview() {
    val bundle = previewBundleSummary

    JantaNiveshTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            (BundleRisk.entries).forEach { risk ->
                BundleCard(
                    bundle = bundle.copy(metaData = bundle.metaData.copy(risk = risk, monthlyStartAmount = 500)),
                    onClick = {}
                )
            }
        }
    }
}
