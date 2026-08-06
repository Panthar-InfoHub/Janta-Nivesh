package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.MutualFundIcon
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundPortfolioDomain

@Composable
fun FolioFundCard(fundItem: MutualFundPortfolioDomain, onClick: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick=onClick),
        contentAlignment = Alignment.Center
    ){

        Column(
            modifier = Modifier.padding(
                vertical = 16.dp
            )
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                SubcomposeAsyncImage(
                    modifier = Modifier.size(44.dp),
                    model = fundItem.icon,
                    contentDescription = null,

                    loading = {
                        MutualFundIcon(
                            schemeName = fundItem.title, size = 44.dp
                        )
                    },

                    error = {
                        MutualFundIcon(
                            schemeName = fundItem.title, size = 44.dp,
                            backgroundColor = Color(0xffEFEDF3),
                            textColor = Primary
                        )
                    },

                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )

                Column(
                    modifier = Modifier.weight(1f).padding(end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fundItem.title,
                            color = Color.Black,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                            lineHeight = 20.sp,
                            modifier = Modifier.weight(1f),
                            maxLines = 2
                        )

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Folio no. ",
                            color = titleColor,
                            style = tinyLabel
                        )
                        Text(
                            text = fundItem.folio,
                            color = Color(0xff919191),
                            style = tinyLabel
                        )
                    }
                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 20.dp),
                thickness = 0.5.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = "Invested",
                        style = MaterialTheme.typography.titleSmall,
                        color = titleColor
                    )
                    Text(
                        text = ("₹" + formatMoneyAfterL(fundItem.amount.toLong())).withInterRupee(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Current Value",
                        style = MaterialTheme.typography.titleSmall,
                        color = titleColor
                    )
                    Text(
                        text = ("₹" + formatMoneyAfterL(fundItem.currentValue.toLong())).withInterRupee(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FolioFundCardPreview() {
    val sampleFund = MutualFundPortfolioDomain(
        id = "f49b4800-6016-4123-bd17-7303bc2b18c3",
        title = "SBI Bluechip Fund Direct Growth Fund Direct Growth",
        category = "Equity: Large Cap",
        amount = 125000.0,
        currentValue = 143000.0,
        returnAmount = 18000.0,
        returnPercentage = "14.5%",
        folio = "12345678/90",
        icon = "",
        minSipAmount = 100,
        minLumpSumAmount = 1000,
        schemeId = 1,
        balanceUnits = 33.44,
        actualFolio = "cwcwecwc"
    )
    JantaNiveshTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            FolioFundCard(
                fundItem = sampleFund,
                onClick = {}
            )
        }
    }
}