package org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.bell_icon
import jantanivesh.shared.generated.resources.education_icon
import jantanivesh.shared.generated.resources.front_arrow_icon
import jantanivesh.shared.generated.resources.home_create_custom_goal_desc
import jantanivesh.shared.generated.resources.home_custom_goal_subtitle
import jantanivesh.shared.generated.resources.home_custom_goal_title
import jantanivesh.shared.generated.resources.home_fixed_deposits
import jantanivesh.shared.generated.resources.home_go_to_goals_desc
import jantanivesh.shared.generated.resources.home_good_morning
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
import jantanivesh.shared.generated.resources.invesy_in_mf_icon
import jantanivesh.shared.generated.resources.monument_icon
import jantanivesh.shared.generated.resources.piggybank_icon
import jantanivesh.shared.generated.resources.plus_icon
import jantanivesh.shared.generated.resources.profile_in_frame_icon
import jantanivesh.shared.generated.resources.progress_icon
import jantanivesh.shared.generated.resources.ring_icon
import jantanivesh.shared.generated.resources.ruppee_circle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.DashedBorderColor
import org.velvetinvesting.jantanivesh.app.core.theme.FdIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Gray65
import org.velvetinvesting.jantanivesh.app.core.theme.GrayScreenBackGround
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.IconBackgroundBlue
import org.velvetinvesting.jantanivesh.app.core.theme.IconSize
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.MutualFundIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.core.theme.ShadowElevation
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.LightBlue
import org.velvetinvesting.jantanivesh.app.core.theme.LightBlueBorder
import org.velvetinvesting.jantanivesh.app.core.theme.LightGreen
import org.velvetinvesting.jantanivesh.app.core.theme.LightGreenBorder
import org.velvetinvesting.jantanivesh.app.core.theme.LightOrange
import org.velvetinvesting.jantanivesh.app.core.theme.LightOrangeBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyWithUnits
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.progressPercent
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.HomeScreenEvent
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.HomeScreenUiState
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ShadowCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.dashedBorder
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow

@Preview(showBackground = true, heightDp = 1204)
@Composable
fun HomeScreenPreview() {
    JantaNiveshTheme {
        Scaffold(modifier = Modifier.navigationBarsPadding().statusBarsPadding()) {
            HomeScreen(
                state = HomeScreenUiState(
                    userName = "Sharad"
                ),
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
    Box(modifier= Modifier.fillMaxSize()
        .background(GrayScreenBackGround)){
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
}
@Composable
fun HomeScreenContent(
    state: HomeScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val horizontalPadding = Modifier.padding(horizontal = Spacing.dp16)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
        modifier = modifier,
        contentPadding = PaddingValues(top = Spacing.dp8)
    ) {
        item {
            HomeHeader(
                userName = state.userName,
                onNotificationClick = { onEvent(HomeScreenEvent.OnNotificationClicked) },
                modifier = horizontalPadding
            )
        }

        item {
            PortfolioCard(
                portfolioValue = state.portfolioValue,
                fixedDepositsAmount = state.fixedDepositsAmount,
                mutualFundsAmount = state.mutualFundsAmount,
                pnlTrend = state.pnlTrend,
                modifier = horizontalPadding
            )
        }

        if (!state.kycVerified) {
            item {
                KycCard(
                    title = stringResource(Res.string.home_kyc_title),
                    buttonText = stringResource(Res.string.home_verify_button),
                    onClick = {
                        onEvent(HomeScreenEvent.OnVerifyKycClicked)
                    },
                    modifier = horizontalPadding
                )
            }
        }

        item {
            QuickInvestActions(
                onInvestInMfClick = { onEvent(HomeScreenEvent.OnInvestInMfClicked) },
                onInvestInFdClick = { onEvent(HomeScreenEvent.OnInvestInFdClicked) },
                modifier = horizontalPadding
            )
        }

        item {
            Text(
                text = "Start Investing Small",
                style = MaterialTheme.typography.labelLarge,
                modifier = horizontalPadding
            )
        }

        item {
            StartInvestingSmallCards(
                onDailySipClick = {
                    onEvent(HomeScreenEvent.OnDailySipClicked)
                },
                onMonthlySipClick = {
                    onEvent(HomeScreenEvent.OnMonthlySipClicked)
                },
                modifier = horizontalPadding
            )
        }

        item {
            HomeBanner(
                heading = "Investing Made Easy",
                subHeading = "One investment. Infinite opportunities. Seamless journey.",
                backgroundColor = LightBlue,
                border = LightBlueBorder,
                modifier = horizontalPadding
            )
        }

        item {
            HomeBanner(
                heading = "Invest with purpose",
                subHeading = "Invest wisely to achieve your personal goals with smart strategies.",
                backgroundColor = LightGreen,
                border = LightGreenBorder,
                modifier = horizontalPadding
            )
        }

        item {
            HomeBanner(
                heading = "Simple Fixed Deposits",
                subHeading = "A fixed deposit plan for security, reliable returns, and growth.",
                backgroundColor = LightOrange,
                border = LightOrangeBorder,
                modifier = horizontalPadding
            )
        }

        item {
            HomeFooter()
        }
    }
}

@Composable
private fun HomeHeader(
    userName: String,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp0)) {
            Text(
                stringResource(Res.string.home_good_morning),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                userName,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .shadow(
                    elevation = ShadowElevation.dp4,
                    shape = LocalShapes.current.circle
                )
                .clip(LocalShapes.current.circle)
                .background(
                    color = White,
                )
                .clickable(onClick = onNotificationClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.bell_icon),
                contentDescription = stringResource(Res.string.home_notifications_desc),
                modifier = Modifier.size(Spacing.dp20),
                tint = Black
            )
        }
    }
}

@Composable
private fun PortfolioCard(
    portfolioValue: String,
    fixedDepositsAmount: String,
    mutualFundsAmount: String,
    pnlTrend: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth().genericDropShadow()
            .clip(LocalShapes.current.roundedDp16)
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
                    color = White,
                    letterSpacing = 1.sp
                )
                Text(
                    "₹$portfolioValue".withInterRupee(),
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
                    amount = fixedDepositsAmount,
                    modifier = Modifier.weight(1f)
                )
                AmountCard(
                    title = stringResource(Res.string.home_mutual_funds),
                    amount = mutualFundsAmount,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        PnlTrendChip(
            pnlTrend = pnlTrend,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

@Composable
private fun PnlTrendChip(
    pnlTrend: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp4),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(Spacing.dp16)
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
            "$pnlTrend${stringResource(Res.string.home_pnl_trend_suffix)}",
            style = MaterialTheme.typography.labelSmall,
            color = SelectedBoxBorder,
        )
    }
}

@Composable
private fun QuickInvestActions(
    onInvestInMfClick: () -> Unit,
    onInvestInFdClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        modifier = modifier
    ) {
        IconButtonCard(
            onClick = onInvestInMfClick,
            title = stringResource(Res.string.home_invest_in_mf),
            icon = Res.drawable.invesy_in_mf_icon,
            iconBackground = MutualFundIconBg,
            modifier = Modifier.weight(1f)
        )
        IconButtonCard(
            onClick = onInvestInFdClick,
            title = stringResource(Res.string.home_invest_in_fd),
            icon = Res.drawable.monument_icon,
            iconBackground = FdIconBg,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StartInvestingSmallCards(
    onDailySipClick: () -> Unit,
    onMonthlySipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        InvestingCard(
            icon = Res.drawable.piggybank_icon,
            heading = "Daily Micro-SIP",
            subHeading = "Start with just ₹10/day",
            onClick = onDailySipClick,
            modifier = Modifier.weight(1f)
        )
        InvestingCard(
            icon = Res.drawable.icon_callender,
            heading = "Monthly SIP",
            subHeading = "Invest ₹500/month.",
            onClick = onMonthlySipClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GoalsSectionHeader(
    onGoToGoalsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
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
                .clickable(onClick = onGoToGoalsClick),
            tint = SelectedBoxBorder
        )
    }
}

@Composable
private fun GoalsList(
    goals: List<GoalsSummaryDomain>,
    onGoalClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
        modifier = modifier
    ) {
        for (goal in goals) {
            GoalCard(
                goal = goal,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onGoalClick(goal.goalId) }
            )
        }
    }
}

@Composable
private fun CreateCustomGoalCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().dashedBorder(
            color = DashedBorderColor,
            dashLength = Spacing.dp6,
            gapLength = Spacing.dp2,
            strokeWidth = Spacing.dp1
        )
            .clickable(onClick = onClick)
            .padding(Spacing.dp12)
    ) {
        IconButton(
            onClick = onClick,
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
    modifier: Modifier = Modifier,
    onClick: ()-> Unit
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
            .clickable(onClick=onClick)
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
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .genericDropShadow(
                shape=LocalShapes.current.roundedDp20,
                radius = ShadowElevation.dp16
            )
            .clip(
                LocalShapes.current.roundedDp20
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
            color = Black,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )

    }
}

@Composable
private fun InvestingCard(
    icon: DrawableResource,
    heading: String,
    subHeading: String,
    onClick: () -> Unit,
    modifier: Modifier= Modifier
){
    ShadowCard(
        modifier=modifier.fillMaxWidth(),
        shape = LocalShapes.current.roundedDp24
    ) {
        Column(
            modifier= Modifier.fillMaxWidth()
                .padding(vertical = Spacing.dp16, horizontal = Spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
        ) {
            Box(
                modifier= Modifier.size(Spacing.dp40)
                    .clip(LocalShapes.current.circle)
                    .background(IconBackgroundBlue),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(IconSize.dp20),
                    tint = Primary
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
            ){
                Text(
                    text = heading,
                    style = MaterialTheme.typography.labelSmall
                        .copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = subHeading,
                    style = MaterialTheme.typography.titleSmall,
                    color = Gray65
                )
            }
            AppButton(
                onClick = onClick,
                text = "Start Now",
                modifier = Modifier.height(Spacing.dp36)
            )
        }
    }
}

@Composable
private fun HomeBanner(
    heading: String,
    subHeading: String,
    backgroundColor: Color,
    border: Color,
    modifier: Modifier = Modifier
){
    Column(
        modifier= modifier.fillMaxWidth()
            .genericDropShadow(
                shape = LocalShapes.current.roundedDp16,
                radius = ShadowElevation.dp16
            )
            .clip(LocalShapes.current.roundedDp16)
            .background(backgroundColor)
            .border(
                width = Spacing.dp1,
                color = border,
                shape = LocalShapes.current.roundedDp16
            )
            .padding(Spacing.dp20),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
    ) {
        Text(
            text = heading,
            style = MaterialTheme.typography.labelSmall
                .copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = subHeading,
            style = MaterialTheme.typography.titleSmall,
            modifier= Modifier.padding(end = Spacing.dp28)
        )
    }
}

@Composable
private fun HomeFooter(
    modifier: Modifier= Modifier
){
    val title = buildAnnotatedString {
        append("Ab Har Jan Karega")
        append("\n")

        withStyle(
            SpanStyle(color = Secondary)
        ) {
            append("Nivesh")
        }
    }
    Box(modifier=modifier.fillMaxWidth()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xffF0F2F8), Secondary.copy(0.3f)
                )
            )
        )){
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(start = Spacing.dp16, bottom = Spacing.dp32, top = Spacing.dp12)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Built for everyone, crafted in India!",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}