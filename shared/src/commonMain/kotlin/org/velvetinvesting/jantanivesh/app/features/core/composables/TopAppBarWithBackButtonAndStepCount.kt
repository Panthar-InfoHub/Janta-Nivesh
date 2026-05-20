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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.arrowback_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.theme.Primary

@Composable
fun TopAppBarWithBackButtonAndStepCount(stepCount: Int, totalSteps: Int, onBack: () -> Unit) {
    Column(modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)) {
        Icon(
            painter = painterResource(Res.drawable.arrowback_icon),
            contentDescription = "Go Back",
            modifier = Modifier.clickable(onClick = onBack)
        )
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Proper spacing between segments
        ) {
            for (i in 1..totalSteps) {
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (i <= stepCount) Primary else GreyBox)
                )
            }
        }
    }
}