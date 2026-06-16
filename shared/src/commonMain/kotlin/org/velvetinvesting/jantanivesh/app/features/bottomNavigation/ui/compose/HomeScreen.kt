package org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import jantanivesh.shared.generated.resources.education_icon
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
import jantanivesh.shared.generated.resources.home_kyc_title
import jantanivesh.shared.generated.resources.home_mutual_funds
import jantanivesh.shared.generated.resources.home_notifications_desc
import jantanivesh.shared.generated.resources.home_pnl_trend_suffix
import jantanivesh.shared.generated.resources.home_portfolio_value
import jantanivesh.shared.generated.resources.home_verify_button
import jantanivesh.shared.generated.resources.home_your_goals
import jantanivesh.shared.generated.resources.icon_callender
import jantanivesh.shared.generated.resources.insurance_sharp_shield_icon
import jantanivesh.shared.generated.resources.invesy_in_mf_icon
import jantanivesh.shared.generated.resources.monument_icon
import jantanivesh.shared.generated.resources.plus_icon
import jantanivesh.shared.generated.resources.profile_in_frame_icon
import jantanivesh.shared.generated.resources.progress_icon
import jantanivesh.shared.generated.resources.ring_icon
import jantanivesh.shared.generated.resources.ruppee_circle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.CreateGoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.DashedBorderColor
import org.velvetinvesting.jantanivesh.app.core.theme.FdIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.GrayBackGround
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.InsuranceIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.MutualFundIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyWithUnits
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.progressPercent
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.HomeScreenEvent
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.HomeScreenUiState
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.dashedBorder
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun HomeScreenPreview() {
    JantaNiveshTheme {
        Scaffold(modifier = Modifier.navigationBarsPadding().statusBarsPadding()) {
            HomeScreen(
                state = HomeScreenUiState(),
                onEvent = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun HomeScreen(
    state: HomeScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> {
            LoaderScreen(
                modifier = modifier.fillMaxSize()
            )
        }

        state.showError -> {
            ErrorScreen(
                modifier = modifier.fillMaxSize(),
                errorMessage = state.error,
                onRetryClick = { onEvent(HomeScreenEvent.LoadData) }
            )
        }
        else -> {
            HomeScreenContent(
                state = state,
                onEvent = onEvent,
                modifier = modifier
            )
        }
    }
}
@Composable
fun HomeScreenContent(
    state: HomeScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
        modifier = modifier.padding(horizontal = Spacing.dp16),
        contentPadding = PaddingValues(top = Spacing.dp8, bottom = Spacing.dp16)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp0)) {
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
                modifier = Modifier.fillMaxWidth().genericDropShadow()
                    .clip(RoundedCornerShape(Spacing.dp12))
                    .background(color = Primary)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
                    modifier = Modifier.padding(Spacing.dp24)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
                    ) {
                        Text(
                            stringResource(Res.string.home_portfolio_value),
                            style = MaterialTheme.typography.bodySmall,
                            color = White
                        )
                        Text(
                            "₹${state.portfolioValue}".withInterRupee(),
                            style = MaterialTheme.typography.displayMedium,
                            color = White
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AmountCard(
                            title = stringResource(Res.string.home_fixed_deposits),
                            amount = state.fixedDepositsAmount,
                            modifier = Modifier.weight(1f)
                        )
                        AmountCard(
                            title = stringResource(Res.string.home_mutual_funds),
                            amount = state.mutualFundsAmount,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp4),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(Spacing.dp16)
                        .align(Alignment.TopEnd)
                        .clip(LocalShapes.current.circle)
                        .background(color = SelectedBoxBorder.copy(alpha = 0.25f))
                        .padding(horizontal = Spacing.dp12, vertical = Spacing.dp8)
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
        if (!state.kycVerified){
            item {
                KycCard(
                    title = stringResource(Res.string.home_kyc_title),
                    buttonText = stringResource(Res.string.home_verify_button),
                    onClick = {
                        onEvent(HomeScreenEvent.OnVerifyKycClicked)
                    }
                )
            }
        }
        else{
            if (!state.tradingAccountVerified) {
                item {
                    KycCard(
                        title = "Setup Trading Account to Start Investing",
                        buttonText = stringResource(Res.string.home_verify_button),
                        onClick = {
                            onEvent(HomeScreenEvent.OnTradingSetupClick)
                        }
                    )
                } }
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
                    modifier = Modifier.size(Spacing.dp24)
                        .clickable(onClick = { onEvent(HomeScreenEvent.OnGoToGoalsClicked) }),
                    tint = SelectedBoxBorder
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                for (goal in state.goals) {
                    GoalCard(
                        goal=goal,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().dashedBorder(
                    color = DashedBorderColor,
                    dashLength = Spacing.dp6,
                    gapLength = Spacing.dp2,
                    strokeWidth = Spacing.dp1
                )
                    .clickable(onClick = { onEvent(HomeScreenEvent.OnCreateCustomGoalClicked) })
                    .padding(Spacing.dp12)
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
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp2)) {
                    Text(
                        stringResource(Res.string.home_custom_goal_title),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        stringResource(Res.string.home_custom_goal_subtitle),
                        style = MaterialTheme.typography.titleSmall,
                        color = GreyText
                    )
                }
            }
        }
    }
}

@Composable
private fun KycCard(
    title: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
    ) {
        VerticalDivider(
            thickness = Spacing.dp4,
            color = Primary,
            modifier = Modifier
                .height(88.dp)
                .weight(0.01f)
        )

        Icon(
            painter = painterResource(Res.drawable.profile_in_frame_icon),
            contentDescription = null,
            modifier = Modifier
                .size(Spacing.dp32)
                .clip(CircleShape)
                .background(SelectedBoxColor)
                .padding(Spacing.dp8)
                .weight(0.1f)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
            modifier = Modifier.weight(0.5f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        TextButton(
            onClick = onClick,
            shape = RoundedCornerShape(Spacing.dp8),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = White
            ),
            modifier = Modifier
                .weight(0.3f)
                .padding(end = Spacing.dp20)
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = Spacing.dp8)
            )
        }
    }
}
@Composable
private fun AmountCard(title: String, amount: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
        modifier = modifier
            .clip(RoundedCornerShape(Spacing.dp8))
            .background(color = White.copy(alpha = 0.1f))
            .border(
                width = Spacing.dp1,
                color = White.copy(alpha = 0.05f)
            ).padding(Spacing.dp12)
    ) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = White)
        Text("₹$amount".withInterRupee(), style = MaterialTheme.typography.labelLarge, color = White)
    }
}

@Composable
private fun GoalCard(
    goal: GoalsSummaryDomain,
    modifier: Modifier = Modifier
) {
    val icon = when (goal.goalTypes.type) {
        GoalType.ChildEducation -> Res.drawable.education_icon
        GoalType.ChildMarriage -> Res.drawable.ring_icon
        GoalType.Retirement -> Res.drawable.icon_callender
        GoalType.WealthBuilding -> Res.drawable.ruppee_circle
    }

    val progress = goal.progressPercent() / 100f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(
                White,
                LocalShapes.current.roundedDp16
            )
            .padding(Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
        ) {

            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier
                    .background(
                        GoalIconBg,
                        CircleShape
                    )
                    .padding(Spacing.dp12)
                    .size(Spacing.dp17)
            )

            Text(
                text = goal.goalTypes.title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            text = "₹${formatMoneyAfterL(goal.amount)}/${formatMoneyWithUnits(goal.targetAmount)}"
                .withInterRupee(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Column {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.dp4)
                    .clip(RoundedCornerShape(Spacing.dp6)),
                color = Primary,
                trackColor = SelectedBoxBorder,
                strokeCap = StrokeCap.Round
            )

            Text(
                text = "${goal.progressPercent()}%",
                modifier = Modifier.align(Alignment.End),
                fontSize = 10.sp,
            )
        }
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
                LocalShapes.current.roundedDp12
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