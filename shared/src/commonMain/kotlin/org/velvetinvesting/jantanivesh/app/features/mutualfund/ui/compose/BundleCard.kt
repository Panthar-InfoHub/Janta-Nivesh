package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.arrow_forward_short_icon
import jantanivesh.shared.generated.resources.ic_bundle_card_fund
import jantanivesh.shared.generated.resources.ic_graph
import jantanivesh.shared.generated.resources.ic_pointer_right
import jantanivesh.shared.generated.resources.rupee_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardAmberBackground
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardAmberOutline
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardAmberTagText
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardFundsIcon
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardFundsIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardPriceIcon
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardPriceIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardPurpleBackground
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardPurpleOutline
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardRoseBackground
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardRoseOutline
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardSkyBackground
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardSkyOutline
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardSubtitle
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardTitle
import org.velvetinvesting.jantanivesh.app.core.theme.IconSize
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SlateGray
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundleCategoryDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundleDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundleMetaDataDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundleSlotDomain

/** The palette a bundle card is drawn in; cards cycle through [bundleCardStyles] by position. */
data class BundleCardStyle(
    val outline: Color,
    val background: Color,
    val tagText: Color
)

val bundleCardStyles = listOf(
    BundleCardStyle(BundleCardAmberOutline, BundleCardAmberBackground, BundleCardAmberTagText),
    BundleCardStyle(BundleCardSkyOutline, BundleCardSkyBackground, BundleCardSkyOutline),
    BundleCardStyle(BundleCardPurpleOutline, BundleCardPurpleBackground, BundleCardPurpleOutline),
    BundleCardStyle(BundleCardRoseOutline, BundleCardRoseBackground, BundleCardRoseOutline)
)

fun bundleCardStyleFor(index: Int): BundleCardStyle = bundleCardStyles[index % bundleCardStyles.size]

@Composable
fun BundleCard(
    bundle: BundleDomain,
    style: BundleCardStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shapes = LocalShapes.current

    Column(
        modifier = modifier
            .clip(shapes.roundedDp20)
            .background(style.background)
            .border(1.dp, style.outline, shapes.roundedDp20)
//            .clickable(onClick = onClick)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
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

        BundleInfoRow(
            icon = painterResource(Res.drawable.ic_bundle_card_fund),
            iconTint = BundleCardFundsIcon,
            iconBackground = BundleCardFundsIconBg
        ) {
            Text(
                text = "${bundle.fundCount} Mutual Funds",
                style= MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = BundleCardTitle
            )
            if (bundle.assetClassLabel.isNotBlank()) {
                Text(
                    text = bundle.assetClassLabel,
                    style=tinyLabel,
                    color = BundleCardSubtitle
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(style.outline.copy(alpha = 0.4f))
        )

        Button(
            onClick = onClick,
            shape = shapes.roundedDp12,
            colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = White),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "View Bundle",
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
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
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

@Preview(showBackground = true)
@Composable
private fun BundleCardPreview() {
    val bundle = BundleDomain(
        id = "1",
        name = "Janta Balance",
        description = "Balanced growth for your long-term goals",
        equityPercentage = 70,
        commodityPercentage = 25,
        debtPercentage = 0,
        hybridPercentage = 5,
        imgUrl = "",
        metaData = BundleMetaDataDomain("", "", ""),
        categories = listOf(
            BundleCategoryDomain(
                id = "c1",
                categoryName = "mid_cap",
                displayName = "Mid Cap",
                totalPercentage = 25,
                slots = listOf(BundleSlotDomain("s1", 15, 1), BundleSlotDomain("s2", 10, 2))
            ),
            BundleCategoryDomain(
                id = "c2",
                categoryName = "global_others",
                displayName = "Commodity",
                totalPercentage = 25,
                slots = listOf(BundleSlotDomain("s3", 15, 1), BundleSlotDomain("s4", 10, 2))
            )
        )
    )

    JantaNiveshTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            bundleCardStyles.indices.forEach { index ->
                BundleCard(
                    bundle = bundle,
                    style = bundleCardStyleFor(index),
                    onClick = {}
                )
            }
        }
    }
}
