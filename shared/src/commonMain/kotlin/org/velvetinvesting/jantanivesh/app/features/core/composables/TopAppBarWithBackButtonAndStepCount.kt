package org.velvetinvesting.jantanivesh.app.features.core.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.arrowback_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.Primary

@Composable
fun TopAppBarWithBackButtonAndStepCount(stepCount: Int, totalSteps: Int, onBack: () -> Unit) {
    Column(modifier = Modifier.padding(top = Spacing.dp16, bottom = Spacing.dp24)) {
        AppBackButton(
            onClick = onBack,
        )
        Row(
            modifier = Modifier
                .padding(top = Spacing.dp16)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            for (i in 1..totalSteps) {
                Box(
                    modifier = Modifier
                        .height(Spacing.dp4)
                        .weight(1f)
                        .clip(LocalShapes.current.roundedDp12)
                        .background(if (i <= stepCount) Primary else GreyBox)
                )
            }
        }
    }
}