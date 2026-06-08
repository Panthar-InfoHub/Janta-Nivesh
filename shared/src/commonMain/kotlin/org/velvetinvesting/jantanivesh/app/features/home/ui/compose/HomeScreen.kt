package org.velvetinvesting.jantanivesh.app.features.home.ui.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.bell_icon
import jantanivesh.shared.generated.resources.create_goal_icon
import jantanivesh.shared.generated.resources.flag_icon
import jantanivesh.shared.generated.resources.front_arrow_icon
import jantanivesh.shared.generated.resources.home_create_custom_goal_desc
import jantanivesh.shared.generated.resources.home_create_goal
import jantanivesh.shared.generated.resources.home_custom_goal_subtitle
import jantanivesh.shared.generated.resources.home_custom_goal_title
import jantanivesh.shared.generated.resources.home_fixed_deposits
import jantanivesh.shared.generated.resources.home_go_to_goals_desc
import jantanivesh.shared.generated.resources.home_good_morning
import jantanivesh.shared.generated.resources.home_insurance
import jantanivesh.shared.generated.resources.home_invest_in_fd
import jantanivesh.shared.generated.resources.home_invest_in_mf
import jantanivesh.shared.generated.resources.home_kyc_desc
import jantanivesh.shared.generated.resources.home_kyc_time
import jantanivesh.shared.generated.resources.home_kyc_title
import jantanivesh.shared.generated.resources.home_mutual_funds
import jantanivesh.shared.generated.resources.home_notifications_desc
import jantanivesh.shared.generated.resources.home_pnl_trend_suffix
import jantanivesh.shared.generated.resources.home_portfolio_value
import jantanivesh.shared.generated.resources.home_verify_button
import jantanivesh.shared.generated.resources.home_your_goals
import jantanivesh.shared.generated.resources.insurance_sharp_shield_icon
import jantanivesh.shared.generated.resources.invesy_in_mf_icon
import jantanivesh.shared.generated.resources.monument_icon
import jantanivesh.shared.generated.resources.plus_icon
import jantanivesh.shared.generated.resources.profile_in_frame_icon
import jantanivesh.shared.generated.resources.progress_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.CreateGoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.FdIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.GrayBackGround
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.InsuranceIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.MutualFundIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.dashedBorder
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.home.ui.viewmodels.HomeScreenEvent
import org.velvetinvesting.jantanivesh.app.features.home.ui.viewmodels.HomeScreenUiState

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun HomeScreenPreview() {
    JantaNiveshTheme {
        HomeScreen(
            state = HomeScreenUiState(),
            onEvent = {},
            modifier = Modifier.padding(Spacing.dp16).fillMaxSize()
        )
    }
}

@Composable
fun HomeScreen(
    state: HomeScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
        modifier = modifier
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        stringResource(Res.string.home_good_morning),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        state.username,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                IconButton(
                    onClick = { onEvent(HomeScreenEvent.OnNotificationClicked) },
                    shape = CircleShape,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = GrayBackGround,
                        contentColor = Black
                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.bell_icon),
                        contentDescription = stringResource(Res.string.home_notifications_desc),
                        modifier = Modifier.size(Spacing.dp20)
                    )
                }
            }
        }
        item {
            Box(
                modifier = Modifier.genericDropShadow().clip(RoundedCornerShape(Spacing.dp12))
                    .background(color = Primary).fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
                    modifier = Modifier.padding(Spacing.dp24)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
                    ) {
                        Text(
                            stringResource(Res.string.home_portfolio_value),
                            style = MaterialTheme.typography.bodySmall,
                            color = White
                        )
                        Text(
                            "₹${formatPrice(state.portfolioValue)}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = White
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AmountCard(
                            title = stringResource(Res.string.home_fixed_deposits),
                            amount = formatPrice(state.fixedDepositsAmount),
                            modifier = Modifier.weight(1f)
                        )
                        AmountCard(
                            title = stringResource(Res.string.home_mutual_funds),
                            amount = formatPrice(state.mutualFundsAmount),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp4),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(Spacing.dp8)
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(Spacing.dp58))
                        .background(color = SelectedBoxBorder.copy(alpha = 0.1f))
                        .padding(all = Spacing.dp12)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.progress_icon),
                        contentDescription = stringResource(Res.string.home_notifications_desc),
                        modifier = Modifier.size(Spacing.dp12),
                        tint = SelectedBoxBorder
                    )
                    Text(
                        "${state.pnlTrend}${stringResource(Res.string.home_pnl_trend_suffix)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SelectedBoxBorder,
                    )
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
                    IconButtonCard(
                        onClick = { onEvent(HomeScreenEvent.OnInvestInFdClicked) },
                        title = stringResource(Res.string.home_invest_in_fd),
                        icon = Res.drawable.monument_icon,
                        iconBackground = FdIconBg,
                        modifier = Modifier.weight(1f)
                    )
                    IconButtonCard(
                        onClick = { onEvent(HomeScreenEvent.OnInvestInMfClicked) },
                        title = stringResource(Res.string.home_invest_in_mf),
                        icon = Res.drawable.invesy_in_mf_icon,
                        iconBackground = MutualFundIconBg,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
                    IconButtonCard(
                        onClick = { onEvent(HomeScreenEvent.OnCreateGoalClicked) },
                        title = stringResource(Res.string.home_create_goal),
                        icon = Res.drawable.create_goal_icon,
                        iconBackground = CreateGoalIconBg,
                        modifier = Modifier.weight(1f)
                    )
                    IconButtonCard(
                        onClick = { onEvent(HomeScreenEvent.OnInsuranceClicked) },
                        title = stringResource(Res.string.home_insurance),
                        icon = Res.drawable.insurance_sharp_shield_icon,
                        iconBackground = InsuranceIconBg,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().genericDropShadow()
                    .clip(RoundedCornerShape(Spacing.dp12))
                    .background(White)
            ) {
                VerticalDivider(
                    thickness = Spacing.dp4,
                    color = Primary,
                    modifier = Modifier.height(88.dp)
                )
                Icon(
                    painterResource(Res.drawable.profile_in_frame_icon),
                    contentDescription = stringResource(Res.string.home_kyc_desc),
                    modifier = Modifier.size(Spacing.dp32).clip(CircleShape)
                        .background(SelectedBoxColor)
                        .padding(Spacing.dp8)
                )
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        stringResource(Res.string.home_kyc_title),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        stringResource(Res.string.home_kyc_time),
                        style = MaterialTheme.typography.titleSmall,
                        color = GreyText
                    )
                }
                TextButton(
                    onClick = { onEvent(HomeScreenEvent.OnVerifyKycClicked) },
                    shape = RoundedCornerShape(Spacing.dp8),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = White
                    )
                ) {
                    Text(
                        stringResource(Res.string.home_verify_button),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = Spacing.dp8)
                    )
                }
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(Res.string.home_your_goals),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    painter = painterResource(Res.drawable.front_arrow_icon),
                    contentDescription = stringResource(Res.string.home_go_to_goals_desc),
                    modifier = Modifier.size(Spacing.dp24).clickable(onClick = { onEvent(HomeScreenEvent.OnGoToGoalsClicked) }),
                    tint = SelectedBoxBorder
                )
            }
        }
        item {
            for (goal in state.goals) {
                GoalCard(
                    name = goal.name,
                    amount = formatPrice(goal.amount),
                    goalProgress = goal.progress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().dashedBorder(
                    color = BoxBorder,
                    dashLength = Spacing.dp4,
                    gapLength = Spacing.dp4,
                    strokeWidth = Spacing.dp1
                ).padding(Spacing.dp16)
            ) {
                IconButton(
                    onClick = { onEvent(HomeScreenEvent.OnCreateCustomGoalClicked) },
                    shape = RoundedCornerShape(Spacing.dp10),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Primary.copy(alpha = 0.1f),
                        contentColor = Primary
                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.plus_icon),
                        contentDescription = stringResource(Res.string.home_create_custom_goal_desc),
                        modifier = Modifier.size(Spacing.dp20)
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(stringResource(Res.string.home_custom_goal_title), style = MaterialTheme.typography.labelLarge)
                    Text(
                        stringResource(Res.string.home_custom_goal_subtitle),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun AmountCard(title: String, amount: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
        modifier = modifier
            .clip(RoundedCornerShape(Spacing.dp8))
            .background(color = White.copy(alpha = 0.1f))
            .border(
                width = Spacing.dp1,
                color = White.copy(alpha = 0.05f)
            ).padding(Spacing.dp12)
    ) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = White)
        Text("₹$amount", style = MaterialTheme.typography.labelLarge, color = White)
    }
}

@Composable
private fun GoalCard(
    name: String,
    amount: String,
    goalProgress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(goalProgress) {
        animatedProgress.animateTo(
            targetValue = goalProgress,
            animationSpec = tween(1000, easing = FastOutSlowInEasing)
        )
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(Spacing.dp24))
            .padding(Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
        ) {
            Icon(
                painter = painterResource(Res.drawable.flag_icon),
                contentDescription = null,
                tint = Black,
                modifier = Modifier
                    .background(GoalIconBg, CircleShape)
                    .padding(Spacing.dp12)
                    .size(Spacing.dp17)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "₹$amount",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        LinearProgressIndicator(
            progress = { animatedProgress.value },
            modifier = Modifier
                .fillMaxWidth()
                .height(Spacing.dp4)
                .clip(RoundedCornerShape(Spacing.dp6)),
            color = Primary,
            trackColor = SelectedBoxBorder,
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "${(animatedProgress.value * 100).toInt()}%",
            modifier = Modifier.align(Alignment.End),
            fontSize = 10.sp,
            color = GreyText
        )
    }
}

@Composable
private fun IconButtonCard(
    onClick: () -> Unit,
    title: String,
    icon: DrawableResource,
    iconBackground: Color,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.genericDropShadow(RoundedCornerShape(Spacing.dp12))
            .clip(
                RoundedCornerShape(Spacing.dp24)
            )
            .background(color = White)
            .clickable(onClick = onClick)
            .padding(Spacing.dp16)
    ) {
        Icon(
            painterResource(icon),
            contentDescription = title,
            modifier = Modifier.size(Spacing.dp48).clip(CircleShape)
                .background(iconBackground)
                .padding(Spacing.dp14)
        )
        Text(
            title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Black
        )

    }
}

fun formatPrice(price: String): String {
    val cleanPrice = price.filter { it.isDigit() }
    if (cleanPrice.length <= 3) return cleanPrice
    val lastThree = cleanPrice.takeLast(3)
    val remaining = cleanPrice.dropLast(3)
    val formattedRemaining = remaining
        .reversed()
        .chunked(2)
        .joinToString(",")
        .reversed()
    return "$formattedRemaining,$lastThree"
}