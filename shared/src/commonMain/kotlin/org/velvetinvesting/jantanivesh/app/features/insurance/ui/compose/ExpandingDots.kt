package org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.LightGray
import org.velvetinvesting.jantanivesh.app.core.theme.Primary

@Composable
fun ExpandingDots(
    numberOfDots:Int=4,
    currentIndexOfDot:Int=0
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(numberOfDots){
            Box(
                modifier=Modifier.height(10.dp)
                    .width(
                        if (currentIndexOfDot==it) 48.dp else 10.dp
                    )
                    .animateContentSize()
                    .clip(CircleShape)
                    .background(if(currentIndexOfDot==it)Primary else LightGray
                        ,CircleShape)
            )
        }
    }
}