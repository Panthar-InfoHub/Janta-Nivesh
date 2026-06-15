package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.education_icon
import jantanivesh.shared.generated.resources.icon_callender
import jantanivesh.shared.generated.resources.plus_icon
import jantanivesh.shared.generated.resources.ring_icon
import jantanivesh.shared.generated.resources.ruppee_circle
import jantanivesh.shared.generated.resources.upward_trend_arrow
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyWithUnits
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.domain.models.progressPercent
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.GoalOption
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import jantanivesh.shared.generated.resources.arrow_front_icon
import org.velvetinvesting.jantanivesh.app.core.theme.UploadBoxBackground
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults

data class YourGoalsScreenState(
    val totalGoalProgressAmt: String = "35,50,000",
    val goalTargetAmt: String = "2.35 Cr",
    val goalPercentage: String = "75",
    val goals: List<GoalsSummaryDomain> = listOf(
        GoalsSummaryDomain(
            goalTypes = GoalOption("Child Education", GoalType.ChildEducation),
            amount = 100000,
            targetAmount = 2000000,
            goalId = "1"
        )
    )
)

@Preview(showBackground = true)
@Composable
fun YourGoalsScreenPreview() {
    JantaNiveshTheme {
        YourGoalsScreen(YourGoalsScreenState())
    }
}

@Composable
fun YourGoalsScreen(state: YourGoalsScreenState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = Spacing.dp16),
    ) {
        LocalBackHeader(
            title = "Your Goals",
            onBack = { TODO() },
            modifier = Modifier.statusBarsPadding()
        )
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().background(
                        color = GoalIconBg,
                        shape = RoundedCornerShape(Spacing.dp12)
                    ).padding(Spacing.dp24),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp4),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Total Goal Progress",
                            style = MaterialTheme.typography.labelSmall,
                            color = GreyText
                        )
                        Text(
                            "₹ ${state.totalGoalProgressAmt}",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Primary
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp4),
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Text(
                            "Target: ₹ ${state.goalTargetAmt}",
                            style = MaterialTheme.typography.titleSmall,
                            color = GreyText
                        )
                        Row(
                            modifier = Modifier.background(color = FilterChipUnselected).padding(
                                vertical = Spacing.dp4,
                                horizontal = Spacing.dp8
                            ),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.dp4),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.upward_trend_arrow),
                                contentDescription = null,
                                tint = SelectedBoxBorder,
                                modifier = Modifier.size(Spacing.dp12)
                            )
                            Text(
                                "${state.goalPercentage}%",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Primary
                            )
                        }
                    }
                }
            }
            items(state.goals) {
                GoalCard(it)
            }
        }
        AppButton(
            text = "Invest Now",
            onClick = { TODO() },
            modifier = Modifier.genericDropShadow().fillMaxWidth().navigationBarsPadding(),
            style = AppButtonDefaults.style(shape = RoundedCornerShape(Spacing.dp16))
        )
    }
}

@Composable
private fun LocalBackHeader(
    title: String,
    onBack: () -> Unit,
    showBack: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.dp8)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (showBack) {
            AppBackButton(onClick = onBack)
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        IconButton(onClick = { TODO() }) {
            Icon(
                painter = painterResource(Res.drawable.plus_icon),
                contentDescription = null,
                tint = SelectedBoxBorder
            )
        }
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
            .padding(Spacing.dp20),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {

        Row(
            verticalAlignment = Alignment.Top,
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
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                Text(
                    text = goal.goalTypes.title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = goal.goalTypes.type.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText
                )
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = { TODO() },
                shape = CircleShape,
                colors = IconButtonDefaults.iconButtonColors(containerColor = UploadBoxBackground),
                modifier = Modifier.size(Spacing.dp32)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_front_icon),
                    contentDescription = null,
                    tint = SelectedBoxBorder
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "₹ ${goal.amount}"
                    .withInterRupee(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "₹ ${goal.targetAmount}"
                    .withInterRupee(),
                style = MaterialTheme.typography.titleSmall,
                color = GreyText
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.dp8)
                    .clip(RoundedCornerShape(Spacing.dp6)),
                color = Primary,
                trackColor = SelectedBoxBorder,
                strokeCap = StrokeCap.Round,
                drawStopIndicator = {}
            )

            Text(
                text = "${goal.progressPercent()}%",
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SelectedBoxBorder
            )
        }
    }
}
