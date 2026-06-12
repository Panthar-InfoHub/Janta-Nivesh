package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TwoWaySwitch
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.Metrics

@Composable
fun ReturnsSection(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    isShortTerm: Boolean,
    onShortTermClick: () -> Unit,
    onLongTermClick: () -> Unit,
    metrics: Metrics
) {

    ExpandableContent(
        modifier = modifier,
        expanded = expanded,
        heading = "Returns",
        onIconClick = onExpandToggle
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            TwoWaySwitch(
                isFirstSelected = isShortTerm,
                onFirstClick = onShortTermClick,
                onSecondClick = onLongTermClick,
                firstText = "Short Term",
                secondText = "Long Term"
            )

            Column(
                modifier=Modifier.fillMaxWidth().height(288.dp)
            ){
                Spacer(Modifier.height(16.dp))

                if (isShortTerm) {
                    ReturnsGrid(
                        items = listOf(
                            "1 Month" to metrics.return_30d,
                            "3 Months" to metrics.return_90d,
                            "6 Months" to metrics.return_6m,
                            "1 Year" to metrics.return_1y
                        )
                    )
                } else {
                    ReturnsGrid(
                        items = listOf(
                            "1 Year" to metrics.return_1y,
                            "3 Years" to metrics.return_3y,
                        )
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Returns calculated from historical NAV data",
                    fontSize = 12.sp,
                    fontFamily = InterFontFamily,
                    color = titleColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun ReturnsGrid(
    items: List<Pair<String, Double?>>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items.chunked(2).forEach { rowItems ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                rowItems.forEach { item ->
                    ReturnCard(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ReturnCard(
    item: Pair<String, Double?>,
    modifier: Modifier = Modifier
) {

    val (label, value) = item

    val isPositive = value != null && value >= 0

    val bgColor = when {
        value == null -> Color(0xFFF3F4F6)
        isPositive -> appGreen.copy(alpha = 0.1f)
        else -> appRed.copy(alpha = 0.1f)
    }

    val textColor = when {
        value == null -> titleColor
        isPositive -> appGreen
        else -> appRed
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF9FAFB))
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            color = titleColor
        )

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = value?.let {
                    val sign = if (it >= 0) "+" else ""
                    "$sign${it.trimTo(2)}%"
                } ?: "N/A",
                fontSize = 14.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}