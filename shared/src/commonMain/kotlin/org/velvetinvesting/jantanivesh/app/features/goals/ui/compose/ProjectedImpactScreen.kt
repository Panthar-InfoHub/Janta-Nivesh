package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.delete_box
import jantanivesh.shared.generated.resources.flag_icon
import jantanivesh.shared.generated.resources.ic_chain
import jantanivesh.shared.generated.resources.ic_pointer_right
import jantanivesh.shared.generated.resources.invest_now
import jantanivesh.shared.generated.resources.tick_icon
import jantanivesh.shared.generated.resources.upward_trend_arrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.IconSize
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SecondaryPrimary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectTenureCardColor
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedTenureChipColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.TextGray
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BarHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalHoldingDomain
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectedImpactEvent
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectedImpactUiData

@Composable
fun ProjectedImpactScreen(
    state: UiState<ProjectedImpactUiData>,
    handleEvent: (ProjectedImpactEvent) -> Unit,
    modifier: Modifier = Modifier,
    deleting: Boolean = false
) {
    UiStateContainer(
        uiState = state,
        onRetry = { handleEvent(ProjectedImpactEvent.LoadGoalDetails) }
    ) { data ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.dp16)
        ) {
            BackHeader(
                title = "Projected Impact",
                onBack = { handleEvent(ProjectedImpactEvent.OnBackClicked) },
                modifier = Modifier
            )

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxSize(),
                contentPadding = PaddingValues(top = Spacing.dp16, bottom = Spacing.dp20),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp12)

            ) {
                item {
                    GoalAnalysisCard(
                        data = data,
                        modifier = Modifier
                            .fillMaxWidth()
                            .genericDropShadow(shape = RoundedCornerShape(Spacing.dp32))
                            .background(White, RoundedCornerShape(Spacing.dp32)),
                        onMapClick={handleEvent(ProjectedImpactEvent.OnMapSchemesClick)},
                        deleting = deleting,
                        onDeleteClick = { handleEvent(ProjectedImpactEvent.DeleteGoal) }
                    )
                }

                item {
                    BarHeader(
                        title = "Mapped Funds",
                        modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.dp8)
                    )
                }

                items(data.holdings, key = {it.holdingId}){holding->
                    MappedHoldingCard(
                        holding = holding,
                        removing = false,
                        onRemove = {  },
                        showDelete = false
                    )
                }

            }

            AppButton(
                text = "Invest Now/ " + stringResource(Res.string.invest_now),
                onClick = { handleEvent(ProjectedImpactEvent.OnInvestNowClicked) },
                modifier = Modifier
                    .genericDropShadow()
                    .fillMaxWidth(),
                style = AppButtonDefaults.style(shape = RoundedCornerShape(Spacing.dp16))
            )
        }
    }
}

@Composable
private fun GoalAnalysisCard(
    data: ProjectedImpactUiData,
    modifier: Modifier = Modifier,
    onMapClick: () -> Unit,
    deleting: Boolean = false,
    onDeleteClick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
        modifier = modifier
    ) {
        // Main Content Area
        Column(
            modifier = Modifier.padding(vertical=Spacing.dp24, horizontal = Spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
            GoalAnalysisHeader(
                goalName = data.goalName,
                goalTypeName = data.goalTypeName,
                deleting = deleting,
                onDeleteClick = onDeleteClick
            )

            ProjectedImpactCard(data = data)

            ProgressSection(
                progressPercent = data.progressPercent,
            )

            MapSipSection(
                onMapClick= onMapClick
            )

        }
    }
}

@Composable
private fun MapSipSection(onMapClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(LocalShapes.current.roundedDp12)
            .background(Color(0xffDBEAFE).copy(alpha = 0.4f))
            .border(
                width = 1.dp,
                color = Color(0xffDBEAFE).copy(alpha = 0.8f),
                shape = LocalShapes.current.roundedDp12
            )
            .padding(vertical = Spacing.dp12, horizontal = Spacing.dp12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        Box(
            modifier = Modifier.size(Spacing.dp36)
                .clip(LocalShapes.current.roundedDp12)
                .background(Color(0xffDBEAFE))
        ){
            Icon(
                painter = painterResource(Res.drawable.ic_chain),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .padding(Spacing.dp8),
                tint = Primary
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Have active investments?",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Primary
            )
            Text(
                text = "Link already running SIPs to this goal instead of new ones.",
                style = MaterialTheme.typography.titleSmall,
                color = TextGray
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp2),
            modifier = Modifier
                .clip(LocalShapes.current.roundedDp8)
                .clickable(
                    onClick = onMapClick
                )
                .background(White)
                .border(
                    width = 1.dp,
                    color = Color(0xffDBEAFE).copy(alpha = 0.8f),
                    shape = LocalShapes.current.roundedDp8
                )
                .padding(horizontal = Spacing.dp8, vertical = Spacing.dp8   )
        ){
            Text(
                text = "Map SIPs",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Primary
            )

            Icon(
                painter = painterResource(Res.drawable.ic_pointer_right),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(IconSize.dp12)
            )

        }
    }
}

@Composable
private fun GoalAnalysisHeader(
    goalName: String,
    goalTypeName: String,
    modifier: Modifier = Modifier,
    deleting: Boolean = false,
    onDeleteClick: () -> Unit = {}
) {
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
        Column(
            modifier= Modifier.weight(1f)
        ) {
            Text(
                text = goalTypeName.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                color = GreyText
            )
            Text(
                text = goalName,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        // While the delete is in flight the button becomes its own loader, so the goal cannot
        // be deleted twice on a slow response.
        IconButton(
            onClick = onDeleteClick,
            enabled = !deleting
        ){
            if (deleting) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = GreyText,
                    modifier = Modifier.size(IconSize.dp20)
                )
            } else {
                Icon(
                    painter = painterResource(Res.drawable.delete_box),
                    contentDescription = "Delete goal",
                    tint = GreyText,
                    modifier = Modifier.size(IconSize.dp20)
                )
            }
        }
    }
}

/**
 * Two bars, because they answer different questions: progress is what has been saved against the
 * target, feasibility is what those savings will have grown into by the target date.
 */
@Composable
private fun ProgressSection(
    progressPercent: Int,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Goal Progress", style = MaterialTheme.typography.labelSmall)
            Text(
                text = "$progressPercent%",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SelectedBoxBorder
            )
        }
        GoalBar(progress = (progressPercent / 100f).coerceIn(0f, 1f))
    }
}

@Composable
private fun GoalBar(progress: Float, modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier
            .fillMaxWidth()
            .height(Spacing.dp8)
            .clip(RoundedCornerShape(Spacing.dp6)),
        color = SelectedBoxBorder,
        trackColor = FilterChipUnselected,
        strokeCap = StrokeCap.Round,
        drawStopIndicator = {}
    )
}

@Composable
private fun WealthBuildingStatus(
    increasedBy: Double,
    monthlySip: Double,
    lumpsumToday: Double,
    isFixedCorpus: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = SelectTenureCardColor)
            .padding(horizontal = Spacing.dp24, vertical = Spacing.dp20)
    ) {
        // A named corpus is not inflated, so there is no increase to report for one.
        if (!isFixedCorpus) {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                Icon(
                    painter = painterResource(Res.drawable.upward_trend_arrow),
                    contentDescription = null,
                    tint = Color(0xff4F2400),
                    modifier = Modifier.size(Spacing.dp20)
                )
                Text(
                    text = "Inflation adds ${increasedBy.asRupeesText()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xff4F2400)
                )
            }

            Spacer(modifier = Modifier.height(Spacing.dp8))
        }

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
                text = "Req. monthly: ${monthlySip.asRupeesText()}",
                style = MaterialTheme.typography.labelSmall,
                color = SecondaryPrimary
            )
        }

        Spacer(modifier = Modifier.height(Spacing.dp8))

        Text(
            text = "Or ${lumpsumToday.asRupeesText()} invested today.",
            style = MaterialTheme.typography.titleSmall,
            color = GreyText
        )
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
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = valueColor
        )
    }
}

@Composable
private fun ProjectedImpactCard(
    data: ProjectedImpactUiData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
                    label = if (data.isFixedCorpus) "Target Corpus" else "Today's Cost",
                    value = data.todaysCost.asRupeesText()
                )
                ReturnDetailItem(
                    label = "Target",
                    value = data.targetYear?.toString()
                        ?: "${data.yearsRemaining} yrs"
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                ReturnDetailItem(
                    label = "Future Value",
                    value = data.futureValue.asRupeesText()
                )
                ReturnDetailItem(
                    label = "Monthly SIP",
                    value = data.monthlySip.asRupeesText(),
                    valueColor = Primary
                )
            }
        }
    }
}

/** Rupee amounts are rounded to whole rupees: paise in a 20-year projection are noise. */
private fun Double.asRupeesText(): String = "₹ ${formatWithCommas(this.toLong())}"


@Preview(showBackground = true, locale = "hi")
@Composable
private fun ProjectedImpactScreenPreview() {
     val previewHoldings = listOf(
        GoalHoldingDomain(
            holdingId = "holding-1",
            fundName = "HDFC Top 100 Fund – Growth",
            folioNumber = "123456",
            units = 150.25,
            nav = 332.8,
            currentValue = 50_000.0,
            imageUrl = null
        ),
        GoalHoldingDomain(
            holdingId = "holding-2",
            fundName = "ICICI Pru Bluechip – Direct Gr.",
            folioNumber = "884210",
            units = 85.50,
            nav = 98.4,
            currentValue = 35_000.0,
            imageUrl = null
        ),
        GoalHoldingDomain(
            holdingId = "holding-3",
            fundName = "Parag Parikh Flexi Cap Fund",
            folioNumber = "441092",
            units = 42.10,
            nav = 712.5,
            currentValue = 30_000.0,
            imageUrl = null
        )
    )
    val sampleData = ProjectedImpactUiData(
        goalId = "940a7f46-212e-4c53-a5bd-09399cb2bad2",
        goalTypeId = 4,
        goalName = "My New Car",
        goalTypeName = "Buy a Vehicle",
        todaysCost = 1_000_000.0,
        futureValue = 1_276_281.56,
        currentSavings = 100_000.0,
        fvCurrentSavings = 161_051.0,
        netRequiredCorpus = 1_115_230.56,
        monthlySip = 14_401.77,
        lumpsumToday = 692_470.43,
        yearsRemaining = 5,
        targetYear = 2031,
        progressPercent = 8,
        feasibilityScore = 0.13f,
        increasedBy = 276_281.56,
        isFixedCorpus = false,
        holdings = previewHoldings
    )
    JantaNiveshTheme {
        ProjectedImpactScreen(
            state = UiState.Success(sampleData),
            handleEvent = {}
        )
    }
}
