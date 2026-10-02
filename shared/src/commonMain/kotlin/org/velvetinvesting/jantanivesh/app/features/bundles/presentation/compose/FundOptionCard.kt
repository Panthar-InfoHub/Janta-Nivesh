package org.velvetinvesting.jantanivesh.app.features.bundles.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardSubtitle
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardTitle
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBoxDivider
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.subHeading
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.FundIcon
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ShadowCard

/**
 * The compact row card for a fund that can be picked: name, a one-line subtitle, the 1Y / 3Y / 5Y
 * returns and a radio, outlined when [isSelected]. Shared by the select-fund and explore-funds screens, which hold
 * funds as different models, so it takes plain values.
 */
@Composable
internal fun FundOptionCard(
    name: String,
    subtitle: String,
    iconUrl: String?,
    return1Y: Double?,
    return3Y: Double?,
    return5Y: Double?,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    enabled: Boolean = true,
    /** Red when the subtitle explains why the fund can't be picked. */
    subtitleColor: Color = BundleCardSubtitle
) {
    val shape = RoundedCornerShape(12.dp)
    ShadowCard(
        modifier = modifier.fillMaxWidth().alpha(if (enabled) 1f else 0.5f)
            .then(if (isSelected) Modifier.border(1.dp, SelectedFundBorder, shape) else Modifier),
        shape = shape,
        clickable = enabled,
        onClick = onSelect,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FundIcon(iconUrl = iconUrl, name = name, size = 40.dp, cornerRadius = 8.dp)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = subHeading.copy(fontSize = 13.sp),
                    color = BundleCardTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = subtitleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CompactMetric("1Y", return1Y)
                CompactMetric("3Y", return3Y, highlight = true)
                CompactMetric("5Y", return5Y)
            }

            VerticalDivider(
                modifier = Modifier.height(32.dp),
                thickness = 0.7.dp,
                color = GreyBoxDivider
            )

            SelectionIndicator(isSelected = isSelected)
        }
    }
}

/** A section title with its one-line explanation, as used down the fund-picking screens. */
@Composable
internal fun FundSectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = BundleCardSubtitle
        )
    }
}

@Composable
internal fun SelectionIndicator(isSelected: Boolean) {
    if (isSelected) {
        Box(
            modifier = Modifier.size(22.dp).background(SelectedFundAccent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✓", color = Color.White, fontSize = 12.sp
            )
        }
    } else {
        Box(
            modifier = Modifier.size(22.dp).border(1.dp, titleColor.copy(alpha = 0.3f), CircleShape)
        )
    }
}

@Composable
private fun CompactMetric(label: String, value: Double?, highlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = titlesStyle.copy(fontSize = 8.sp),
            color = titleColor.copy(alpha = 0.6f)
        )
        Text(
            text = value?.let { "${it.trimTo(1)}%" } ?: "N/A",
            style = titlesStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
            color = value?.let { returnColor(it, highlight) } ?: titleColor
        )
    }
}

/** Losses are red; of the gains, only the highlighted (3Y) one is green, as in the design. */
internal fun returnColor(value: Double, highlight: Boolean): Color = when {
    value < 0 -> appRed
    highlight -> appGreen
    else -> BundleCardTitle
}

/** A scheme name without its plan and option suffixes, which [fundPlanLabel] shows instead. */
internal fun fundDisplayName(name: String): String =
    if (fundPlanLabel(name) != null) name.substringBefore(" - ").trim().ifBlank { name } else name

/** "Direct • Growth", read off the scheme name; null when the name states neither. */
internal fun fundPlanLabel(name: String): String? {
    val lower = name.lowercase()
    val plan = when {
        "direct" in lower -> "Direct"
        "regular" in lower -> "Regular"
        else -> null
    }
    val option = when {
        "growth" in lower -> "Growth"
        "idcw" in lower || "dividend" in lower -> "IDCW"
        else -> null
    }
    return listOfNotNull(plan, option).joinToString(" • ").ifBlank { null }
}

internal val SelectedFundBorder = Color(0xFF8CCBF2)
internal val SelectedFundPillBackground = Color(0xFFEAF3FE)
internal val SelectedFundAccent = Color(0xFF2F6BD8)
