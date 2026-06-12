package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.delete_box
import jantanivesh.shared.generated.resources.empty_funds_ic
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CartType
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.LumpSumItemDomain

@Composable
fun LumpSumScreenContent(
    items: List<LumpSumItemDomain>,
    onRefresh: () -> Unit,
    onRemoveClick: (String) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ){
        if (items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Image(
                        painter = painterResource(Res.drawable.empty_funds_ic),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp)
                    )
                    Text(
                        text = "No funds added yet. Add funds to get started.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = titleColor.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(
                    items = items,
                    key = { _, it -> it.id }
                ) { _, it ->
                    LumpSumCartItem(
                        item = it,
                        onRemoveClick = onRemoveClick
                    )

                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = titleColor.copy(0.5f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun LumpSumCartItem(
    item: LumpSumItemDomain,
    onRemoveClick: (String) -> Unit
) {

    val initials = remember(item.productName) {
        item.productName
            .split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .joinToString("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                SubcomposeAsyncImage(
                    modifier = Modifier.size(44.dp)
                        .shadow(
                            elevation = 16.dp
                        ),
                    model = item.imageUrl,
                    contentDescription = null,

                    loading = {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = InterFontFamily
                            )
                        }
                    },

                    error = {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = InterFontFamily
                            )
                        }
                    },

                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )


                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = item.productName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.amcName,
                        style = titlesStyle,
                        color = titleColor
                    )
                }
            }

            Icon(
                painter = painterResource(Res.drawable.delete_box),
                contentDescription = null,
                tint = appRed,
                modifier = Modifier.padding(top=8.dp, start = 8.dp).size(18.dp)
                    .clickable { onRemoveClick(item.id) }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = "₹${item.amount}".withInterRupee(),
                color = appGreen,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (item.type == CartType.SIP) "Monthly" else "One-time",
                style = titlesStyle,
                color = Color(0xff4A5565)
            )
        }
    }
}

@Preview
@Composable
fun LumpSumScreenContentPreview() {
    JantaNiveshTheme {
        Box(modifier = Modifier.background(Color.White).fillMaxSize()) {
            LumpSumScreenContent(
                items = listOf(
                    LumpSumItemDomain(
                        id = "1",
                        amcName = "SBI Mutual Fund",
                        productName = "SBI Bluechip Fund Direct Growth",
                        amount = 5000,
                        type = CartType.LUMPSUM,
                        date = "2023-10-27",
                        imageUrl = "",
                        inv_id = "",
                        prodCode = "",
                        folio = "",
                        amcCode = "",
                    )
                ),
                onRefresh = {},
                onRemoveClick = {}
            )
        }
    }
}
