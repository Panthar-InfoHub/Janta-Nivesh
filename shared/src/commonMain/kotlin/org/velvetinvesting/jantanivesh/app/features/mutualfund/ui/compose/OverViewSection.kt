package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.toReadableDate
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDetailsDomain

@Composable
fun OverviewSection(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    data: MutualFundDetailsDomain?
) {

    val expenseRatio = data?.metrics?.nav_change_pct?.let {
        "${it}%"
    } ?: "N/A"

    val amc = data?.amc_name ?: "N/A"

    val inceptionDate = data?.createdAt?.toReadableDate() ?: "N/A"

    val schemeType = data?.scheme_type ?: "N/A"

    ExpandableContent(
        modifier = modifier,
        expanded = expanded,
        heading = "Overview",
        onIconClick = onExpandToggle
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                OverviewItem("Expense Ratio", expenseRatio, Modifier.weight(1f))
                OverviewItem("AMC", amc, Modifier.weight(1f))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                OverviewItem("Inception Date", inceptionDate, Modifier.weight(1f))
                OverviewItem("Scheme Type", schemeType, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun OverviewItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {

        Text(
            text = title,
            style = titlesStyle.copy(fontSize = 12.sp),
            color = titleColor
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold
        )
    }
}