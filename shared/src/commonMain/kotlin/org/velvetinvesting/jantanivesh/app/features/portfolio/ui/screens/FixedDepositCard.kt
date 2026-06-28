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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.ic_callended_filled
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositPortfolioDomain

@Composable
fun FixedDepositCard(fdData: FixedDepositPortfolioDomain, onClick: () -> Unit){
    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable(onClick=onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(44.dp),
                    model = fdData.issuerLogoUrl,
                    contentDescription = null,

                    loading = {
                        MutualFundIcon(
                            schemeName = fdData.issuerDisplayName, size = 44.dp
                        )
                    },

                    error = {
                        MutualFundIcon(
                            schemeName = fdData.issuerDisplayName, size = 44.dp
                        )
                    },

                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = fdData.issuerDisplayName,
                        style = MaterialTheme.typography.labelLarge,
                        lineHeight = 22.sp,
                        color = Color.Black
                    )
                    Text(
                        text = fdData.roiAtBooking+"% p.a.",
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = InterFontFamily,
                        color = Color(0xff00658D)
                    )
                }

                Text(
                    text = "₹ ${ formatMoneyAfterL(fdData.amount.toDouble().toLong()) }".withInterRupee(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary
                )

            }
            HorizontalDivider(
                thickness = 0.5.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_callended_filled),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.DarkGray
                )
                Text(
                    text = "Matures "+ (fdData.maturityDate ?: ""),
                    color = Color.DarkGray,
                    style = tinyLabel.copy(fontWeight = FontWeight.Normal)
                )
            }
            if (fdData.maturityAmount==null){
                Text(
                    text = "Action Pending",
                    color = appRed,
                    style = tinyLabel
                )
            }

        }
    }
}

@Composable
fun MutualFundIcon(schemeName: String, size: Dp) {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
private fun FixedDepositCardPreview() {
    val sampleFd = FixedDepositPortfolioDomain(
        id = "1",
        amount = "100000",
        roiAtBooking = "7.5",
        tenureAtBooking = 12,
        fdIssuedAt = "2023-10-12",
        status = "Active",
        maturityAmount = null,
        userId = "user123",
        userFullName = "John Doe",
        userEmail = "john@example.com",
        issuerLogoUrl = "",
        issuerDisplayName = "HDFC Bank",
        maturityDate = "06 July,2025"
    )
    JantaNiveshTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            FixedDepositCard(
                fdData = sampleFd,
                onClick = {}
            )
        }
    }
}