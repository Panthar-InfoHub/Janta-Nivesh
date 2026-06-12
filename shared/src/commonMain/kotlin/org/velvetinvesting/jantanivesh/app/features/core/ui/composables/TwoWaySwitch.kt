package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle

@Composable
fun TwoWaySwitch(
    isFirstSelected: Boolean,
    firstText: String,
    secondText: String,
    onFirstClick: () -> Unit,
    onSecondClick: () -> Unit,
) {

    val sipColor by animateColorAsState(
        targetValue = if (isFirstSelected) Primary else Primary.copy(alpha = 0.5f),
        animationSpec = tween(250),
        label = ""
    )

    val oneTimeColor by animateColorAsState(
        targetValue = if (!isFirstSelected) Primary else Primary.copy(alpha = 0.5f),
        animationSpec = tween(250),
        label = ""
    )

    var parentWidth by remember { mutableStateOf(0) }

    val offsetPx by animateFloatAsState(
        targetValue = if (isFirstSelected) 0f else parentWidth / 2f,
        animationSpec = tween(250),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .onSizeChanged { parentWidth = it.width }
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xffF3F4F6))
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .graphicsLayer {
                    translationX = offsetPx
                }
                .padding(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        )

        Row(Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier.weight(1f)
                    .fillMaxSize()
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ){ onFirstClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(firstText, style = titlesStyle, color = sipColor)
            }

            Box(
                modifier = Modifier.weight(1f)
                    .fillMaxSize()
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onSecondClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(secondText, style = titlesStyle, color = oneTimeColor)
            }
        }
    }
}