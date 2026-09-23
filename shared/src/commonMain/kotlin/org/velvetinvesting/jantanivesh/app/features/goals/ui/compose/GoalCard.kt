package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.arrow_front_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.PathGray
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.UploadBoxBackground
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow

@Composable
fun GoalCard(
    goal: GoalsSummaryDomain,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = goalIconFor(goal.goalTypes.type)

    val progress = (goal.progressPercent / 100f).coerceIn(0f, 1f)

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
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
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
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                Text(
                    text = goal.title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = goal.goalTypes.type?.displayName ?: goal.goalTypes.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText
                )
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = onClick,
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
                text = "₹ ${formatWithCommas(goal.amount)}"
                    .withInterRupee(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "₹ ${formatWithCommas(goal.targetAmount)}"
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
                trackColor = PathGray,
                strokeCap = StrokeCap.Round,
                drawStopIndicator = {}
            )

            Text(
                text = "${goal.progressPercent}%",
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SelectedBoxBorder
            )
        }
    }
}