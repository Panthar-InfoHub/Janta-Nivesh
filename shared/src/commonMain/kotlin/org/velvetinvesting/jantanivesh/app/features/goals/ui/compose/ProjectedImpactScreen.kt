package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.flag_icon
import jantanivesh.shared.generated.resources.tick_icon
import jantanivesh.shared.generated.resources.upward_trend_arrow
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.*
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.*
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectedImpactEvent
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectedImpactUiData
import org.velvetinvesting.jantanivesh.app.core.utils.UiState

@Composable
fun ProjectedImpactScreen(
    pv: PaddingValues,
    state: UiState<ProjectedImpactUiData>,
    handleEvent: (ProjectedImpactEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    UiStateContainer(
        uiState = state,
        onRetry = { handleEvent(ProjectedImpactEvent.LoadGoalDetails) }
    ) { data ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(pv)
                .padding(horizontal = Spacing.dp16)
        ) {
            BackHeader(
                title = "Projected Impact",
                onBack = { handleEvent(ProjectedImpactEvent.OnBackClicked) },
                modifier = Modifier.statusBarsPadding()
            )

            Column(modifier = Modifier.fillMaxSize()) {
                GoalAnalysisCard(
                    data = data,
                    modifier = Modifier
                        .fillMaxWidth()
                        .genericDropShadow()
                        .background(White, RoundedCornerShape(Spacing.dp32))
                )

                Spacer(modifier = Modifier.weight(1f))

                AppButton(
                    text = "Invest Now",
                    onClick = { handleEvent(ProjectedImpactEvent.OnInvestNowClicked) },
                    modifier = Modifier
                        .genericDropShadow()
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    style = AppButtonDefaults.style(shape = RoundedCornerShape(Spacing.dp16))
                )
            }
        }
    }
}

@Composable
private fun GoalAnalysisCard(
    data: ProjectedImpactUiData,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
        modifier = modifier
    ) {
        // Main Content Area
        Column(
            modifier = Modifier.padding(Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
            GoalAnalysisHeader(goalName = data.goalName)

            ProjectedImpactCard(
                todayCost = "₹ ${data.todaysCost}",
                futureValue = "₹ ${data.futureValue.toLong()}",
                timeHorizon = data.targetYear.toString(),
                requiredSip = "₹ ${data.monthlySip.toLong()}"
            )

            FeasibilitySection(feasibilityScore = data.feasibilityScore)
        }

        // Highlighted Status Strip Area
        WealthBuildingStatus(
            increasedBy = data.increasedBy,
            monthlySip = data.monthlySip.toLong().toString()
        )

        Spacer(modifier = Modifier.height(Spacing.dp20))
    }
}

@Composable
private fun GoalAnalysisHeader(goalName: String, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(Res.drawable.flag_icon),
            contentDescription = null,
            modifier = Modifier
                .size(Spacing.dp48)
                .background(color = SelectedTenureChipColor, shape = CircleShape)
                .padding(Spacing.dp16)
        )
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
            Text(
                text = "GOAL ANALYSIS",
                style = MaterialTheme.typography.titleSmall,
                color = GreyText
            )
            Text(
                text = goalName,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun FeasibilitySection(
    feasibilityScore: Float,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
        modifier = modifier
    ) {
        Text("Feasibility Score", style = MaterialTheme.typography.labelSmall)
        LinearProgressIndicator(
            progress = { feasibilityScore },
            modifier = Modifier
                .fillMaxWidth()
                .height(Spacing.dp8)
                .clip(RoundedCornerShape(Spacing.dp6)),
            color = SelectedBoxBorder,
            trackColor = FilterChipUnselected,
            strokeCap = StrokeCap.Round,
            drawStopIndicator = {}
        )
        Text(
            text = "Based on your current savings rate and projected market returns.",
            style = MaterialTheme.typography.titleSmall,
            color = GreyText
        )
    }
}

@Composable
private fun WealthBuildingStatus(
    increasedBy: Double,
    monthlySip: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = SelectTenureCardColor)
            .padding(horizontal = Spacing.dp24, vertical = Spacing.dp20)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
            Icon(
                painter = painterResource(Res.drawable.upward_trend_arrow),
                contentDescription = null,
                tint = Color(0xff4F2400),
                modifier = Modifier.size(Spacing.dp20)
            )
            Text(
                text = "Increased By ₹ ${increasedBy.toLong()}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xff4F2400)
            )
        }

        Spacer(modifier = Modifier.height(Spacing.dp8))

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = Color(0xFFC6E7FF),
                    shape = RoundedCornerShape(Spacing.dp8)
                )
                .padding(vertical = Spacing.dp8, horizontal = Spacing.dp16)
        ) {
            Icon(
                painter = painterResource(Res.drawable.tick_icon),
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .size(Spacing.dp16)
                    .background(color = SecondaryPrimary, shape = CircleShape)
                    .padding(Spacing.dp4)
            )
            Text(
                text = "Req. monthly: ₹ $monthlySip",
                style = MaterialTheme.typography.labelSmall,
                color = SecondaryPrimary
            )
        }
    }
}


@Composable
private fun ReturnDetailItem(
    label: String,
    value: String,
    valueSuffix: String = "",
    valueColor: Color = Black,
    labelColor: Color = GreyText,
    valueStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = labelColor
        )
        Text(
            text = value + valueSuffix,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = valueColor
        )
    }
}

@Composable
private fun ProjectedImpactCard(
    todayCost: String,
    futureValue: String,
    timeHorizon: String,
    requiredSip: String
) {
    Box(
        modifier = Modifier
            .background(GoalIconBg, shape = RoundedCornerShape(Spacing.dp12))
            .fillMaxWidth()
            .padding(Spacing.dp16)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                ReturnDetailItem(
                    label = "Today's Cost",
                    value = todayCost,
                    valueStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                ReturnDetailItem(
                    label = "Target",
                    value = timeHorizon,
                    valueColor = Black,
                    valueStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

            }
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                ReturnDetailItem(
                    label = "Future Value",
                    value = futureValue,
                    valueStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                ReturnDetailItem(
                    label = "Monthly SIP",
                    value = requiredSip,
                    valueColor = Primary,
                    valueStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
