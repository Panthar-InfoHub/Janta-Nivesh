package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import jantanivesh.shared.generated.resources.ic_jagged_arrow
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.subHeading
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ToggleSwitch
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CartType
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SipDetails
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SipItemDomain
import kotlin.math.roundToInt


enum class StepUpType {
    PERCENTAGE,
    AMOUNT
}

@Composable
fun SIPScreenContent(
    items: List<SipItemDomain>,
    onRefresh: () -> Unit,
    onRemoveClick: (String) -> Unit,
    onStepUpEnabled: (SipItemDomain) -> Unit,
    onStepUpDisabled: (SipItemDomain) -> Unit,
    onStepUpAmountChange: (SipItemDomain, String) -> Unit
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
                    SIPCartItem(
                        item = it,
                        onRemoveClick = onRemoveClick,
                        onStepUpEnabled = { onStepUpEnabled(it) },
                        onStepUpDisabled = { onStepUpDisabled(it) },
                        onStepUpAmountChange = { amount ->
                            onStepUpAmountChange(it, amount)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SIPCartItem(
    item: SipItemDomain,
    onRemoveClick: (String) -> Unit,
    onStepUpEnabled: () -> Unit,
    onStepUpDisabled: () -> Unit,
    onStepUpAmountChange: (String) -> Unit
) {

    var stepUpType by remember(item.id) {
        mutableStateOf(StepUpType.AMOUNT)
    }

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
                text = "₹${item.sipDetails.sipAmount}".withInterRupee(),
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
        HorizontalDivider(
            thickness = 0.5.dp,
            color = titleColor.copy(0.5f),
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(Res.drawable.ic_jagged_arrow),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = titleColor
                )

                Text(
                    text = "Want to Step Up SIP ?",
                    style = subHeading,
                    color = Primary,
                    modifier = Modifier.weight(1f)
                )

                ToggleSwitch(
                    checked = item.stepUpRequired,
                    onCheckedChange = {it->
                        if (it) onStepUpEnabled() else onStepUpDisabled()
                    },
                    width = 44.dp,
                    height = 24.dp,
                    thumbSize = 20.dp,
                    checkedTrackColor = Secondary,
                    uncheckedTrackColor = Color.White,
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Secondary
                )
            }
            AnimatedVisibility(
                visible = item.stepUpRequired
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                stepUpType = StepUpType.AMOUNT
                            }
                        ) {
                            RadioButton(
                                selected = stepUpType == StepUpType.AMOUNT,
                                onClick = {
                                    stepUpType = StepUpType.AMOUNT
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = Secondary)
                            )

                            Text(
                                text = "By Amount",
                                style = titlesStyle
                            )
                        }


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                stepUpType = StepUpType.PERCENTAGE
                            }
                        ) {
                            RadioButton(
                                selected = stepUpType == StepUpType.PERCENTAGE,
                                onClick = {
                                    stepUpType = StepUpType.PERCENTAGE
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = Secondary)
                            )

                            Text(
                                text = "By Percentage",
                                style = titlesStyle
                            )
                        }
                    }

                    when (stepUpType) {

                        StepUpType.PERCENTAGE -> {

                            var percentage by remember(item.id) {
                                mutableIntStateOf(
                                    ((item.stepUpAmount * 100f) / item.sipDetails.sipAmount)
                                        .roundToInt()
                                )

                            }

                            val isBelowMinimum = item.stepUpAmount < item.minStepUpAmount

                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "Percentage",
                                        style = titlesStyle
                                    )

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFF5F7FA))
                                            .padding(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${percentage}%",
                                            color = Primary,
                                            style = subHeading
                                        )
                                    }
                                }

                                Slider(
                                    value = percentage.toFloat(),
                                    onValueChange = {
                                        percentage = ((it / 5f).roundToInt() * 5).coerceAtLeast(item.minStepUpPercent.toInt())

                                        val amount =
                                            ((item.sipDetails.sipAmount * percentage) / 100f).toLong()

                                        onStepUpAmountChange(
                                            amount.coerceAtLeast(item.minStepUpAmount).toString()
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    valueRange = 10f..100f,
                                    steps = 17,

                                    thumb = {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(Secondary)
                                        )
                                    },

                                    track = { sliderScope ->

                                        val progressFraction =
                                            (sliderScope.value - sliderScope.valueRange.start) /
                                                    (sliderScope.valueRange.endInclusive -
                                                            sliderScope.valueRange.start)

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(8.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xff307FE2).copy(alpha = 0.3f))
                                        ) {

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(progressFraction)
                                                    .height(8.dp)
                                                    .clip(CircleShape)
                                                    .background(Secondary)
                                            )
                                        }
                                    }
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {

                                    Text(
                                        text = "Step-Up Amount : ₹${item.stepUpAmount}".withInterRupee(),
                                        style = titlesStyle,
                                        color = titleColor
                                    )

                                    AnimatedVisibility(isBelowMinimum) {
                                        Text(
                                            text = "Minimum step-up amount is ₹${item.minStepUpAmount}"
                                                .withInterRupee(),
                                            color = appRed,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontFamily = InterFontFamily
                                        )
                                    }
                                }
                            }
                        }

                        StepUpType.AMOUNT -> {
                            StepUpAmountField(
                                value = item.stepUpAmount.takeIf { it > 0 }?.toString().orEmpty(),
                                minAmount = item.minStepUpAmount,
                                onValueChange = {
                                    onStepUpAmountChange(it)
                                }
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = titleColor.copy(0.5f),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StepUpAmountField(
    value: String,
    minAmount: Long,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val isBelowMinimum =
        value.toLongOrNull()?.let { it < minAmount } == true

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Enter Amount",
            style = subHeading,
            color = Color.Black
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = Secondary,
                    shape = RoundedCornerShape(12.dp)
                )
        ) { innerTextField ->

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "₹",
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {

                    if (value.isBlank()) {
                        Text(
                            text = "Enter step Up Amount",
                            color = Color(0xffA0A7B5),
                            fontSize = 16.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    innerTextField()
                }

                Text(
                    text = "Min ₹${minAmount}".withInterRupee(),
                    color = Color(0xffA0A7B5),
                    style = titlesStyle
                )
            }
        }
        AnimatedVisibility(isBelowMinimum) {
            Text(
                text = "Step-up amount must be at least ₹$minAmount"
                    .withInterRupee(),
                color = appRed,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = InterFontFamily
            )
        }
    }
}



@Preview
@Composable
fun SIPScreenContentPreview() {
    JantaNiveshTheme {
        Box(modifier = Modifier.background(Color.White).fillMaxSize()) {
            SIPScreenContent(
                items = listOf(
                    SipItemDomain(
                        id = "1",
                        amcName = "SBI Mutual Fund",
                        productName = "SBI Bluechip Fund Direct Growth",
                        amount = 5000,
                        type = CartType.SIP,
                        date = "2023-10-27",
                        sipDetails = SipDetails(
                            startDate = "2023-11-01",
                            endDate = "2028-11-01",
                            frequency = "Monthly",
                            day = 5,
                            sipAmount = 2000
                        ),
                        imageUrl = "",
                        inv_id = "",
                        prodCode = "",
                        stepUpRequired = true,
                        stepUpAmount = 100,
                        minStepUpAmount = 500,
                        amcCode = "",
                        minStepUpPercent = 10.0,
                    )
                ),
                onRefresh = {},
                onRemoveClick = {},
                onStepUpEnabled = {},
                onStepUpDisabled = {},
                onStepUpAmountChange = { _, _ -> }
            )
        }
    }
}

