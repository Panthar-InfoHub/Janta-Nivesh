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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.getPreferredReturn
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyWithUnits
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TwoWaySwitch
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CalculatorInputState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.Metrics
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatIfCalculator(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    input: CalculatorInputState,
    metrics: Metrics?,
    onModeChange: (Boolean) -> Unit,
    onInvestmentChange: (Long) -> Unit,
    onTimeChange: (Int) -> Unit,

    ) {

    val expectedReturn = metrics?.getPreferredReturn() ?: 12f

    val monthlyRate = expectedReturn / 100f / 12f

    val totalInvested = if (input.isSip) {
        input.monthlyInvestment * 12 * input.timeInYears
    } else {
        input.monthlyInvestment
    }

    val totalValue = if (input.isSip) {
        val months = input.timeInYears * 12

        (input.monthlyInvestment *
                (((1 + monthlyRate).toDouble().pow(months.toDouble()) - 1) / monthlyRate) *
                (1 + monthlyRate)).toLong()
    } else {
        (input.monthlyInvestment *
                (1 + expectedReturn / 100).toDouble()
                    .pow(input.timeInYears.toDouble())).toLong()
    }

    val amountRange =if (input.isSip) 500f..50000f else 500f..100000f

    val returns = totalValue - totalInvested

    ExpandableContent(
        modifier=Modifier.padding(horizontal = 16.dp),
        expanded = expanded,
        heading = "What if Calculator",
        onIconClick = onExpandToggle
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {

            TwoWaySwitch(
                isFirstSelected = input.isSip,
                onFirstClick = { onModeChange(true) },
                onSecondClick = { onModeChange(false) },
                firstText = "Monthly SIP",
                secondText = "One-Time"
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Investment Amount",
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color =Color(0xff4A5565)
                )
                Text(
                    "₹ ${input.monthlyInvestment}".withInterRupee(),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            Slider(
                value = input.monthlyInvestment.toFloat(),
                onValueChange = { onInvestmentChange(it.toLong()) },
                valueRange = amountRange,
                modifier = Modifier.fillMaxWidth(),
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Secondary)
                    )
                },
                track = { sliderScope ->
                    val progress =
                        (sliderScope.value - sliderScope.valueRange.start) /
                                (sliderScope.valueRange.endInclusive - sliderScope.valueRange.start)

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xff307FE2).copy(alpha = 0.3f))
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(progress)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Secondary)
                        )
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    "Time Period", fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xff4A5565)
                )
                Text(
                    "${input.timeInYears} Years",
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            Slider(
                value = input.timeInYears.toFloat(),
                onValueChange = { onTimeChange(it.toInt()) },
                valueRange = 1f..30f,
                modifier = Modifier.fillMaxWidth(),
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Secondary)
                    )
                },
                track = { sliderScope ->
                    val progress =
                        (sliderScope.value - sliderScope.valueRange.start) /
                                (sliderScope.valueRange.endInclusive - sliderScope.valueRange.start)

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xff307FE2).copy(alpha = 0.3f))
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(progress)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Secondary)
                        )
                    }
                }
            )

            Spacer(Modifier.height(20.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xffF3F4F6))
                    .padding(16.dp)
            ) {

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Invested",
                        fontSize = 14.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = titleColor)
                    Text("₹ ${formatMoneyWithUnits(totalInvested)}".withInterRupee(),
                        fontSize = 14.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium)
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Est. Returns" ,
                        fontSize = 14.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = titleColor)
                    Text(
                        "₹ ${formatMoneyWithUnits(returns)}".withInterRupee(),
                        color = if (returns>0) appGreen else appRed,
                        fontSize = 14.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(12.dp))

                HorizontalDivider(color = titleColor.copy(0.3f))

                Spacer(Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Total Value",
                        fontSize = 16.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                    Text(
                        "₹ ${formatMoneyWithUnits(totalValue)}".withInterRupee(),
                        fontSize = 18.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                }
            }
        }
    }
}

