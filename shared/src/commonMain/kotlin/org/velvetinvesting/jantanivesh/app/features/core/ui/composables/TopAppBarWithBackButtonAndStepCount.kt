package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.Primary

@Composable
fun TopAppBarWithBackButtonAndStepCount(
    stepCount: Int,
    totalSteps: Int,
    onBack: () -> Unit,
    modifier: Modifier= Modifier
) {
    Column(modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
        AppBackButton(
            onClick = onBack,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            for (i in 1..totalSteps) {

                val progress by animateFloatAsState(
                    targetValue = when {
                        i < stepCount -> 1f
                        i == stepCount -> 1f
                        else -> 0f
                    },
                    label = "step_progress",
                    animationSpec = tween(
                        easing = FastOutSlowInEasing
                    )
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(Spacing.dp4)
                        .clip(LocalShapes.current.roundedDp12)
                        .background(GreyBox)

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .clip(LocalShapes.current.roundedDp12)
                            .background(Primary)
                    )
                }
            }
        }
    }
}