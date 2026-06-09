package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.tick_icon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily

@Composable
fun TradingAccountSuccess(
    onButtonClick: () -> Unit,
    buttonText: String = "Start Investing",
) {
    var animationFinished by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2500) // Play animation for 2.5 seconds
        animationFinished = true
    }

    LaunchedEffect(animationFinished) {
        if (animationFinished) {
            showButton = true
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        SuccessIconAnimation()

        if (showButton) {
            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF273E71)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = buttonText,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SuccessIconAnimation() {
    val alphaCheck = remember { Animatable(0f) }
    val alphaRipples = remember { Animatable(0f) }
    val alphaDots = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    LaunchedEffect(Unit) {
        // Main check part fades in and scales up
        launch {
            alphaCheck.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
        }
        
        // Ripples fade in slightly after
        launch {
            delay(200)
            alphaRipples.animateTo(1f, animationSpec = tween(900, easing = LinearOutSlowInEasing))
        }

        // Dots fade in with sequential delays
        alphaDots.forEachIndexed { index, animatable ->
            launch {
                delay(500L + (index * 200L))
                animatable.animateTo(1f, animationSpec = tween(600))
            }
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(300.dp)) {
        // Outer pulsing ripples (Fading in)
        Box(
            modifier = Modifier
                .size(240.dp)
                .alpha(alphaRipples.value * 0.05f)
                .background(Color(0xFF2E5BFF), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(190.dp)
                .alpha(alphaRipples.value * 0.1f)
                .background(Color(0xFF2E5BFF), CircleShape)
        )

        // Main blue ring and white center
        Box(
            modifier = Modifier
                .size(100.dp)
                .alpha(alphaCheck.value)
                .scale(lerp(0.7f, 1f, alphaCheck.value))
                .border(8.dp, Color(0xFF2E5BFF), CircleShape)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.tick_icon),
                contentDescription = null,
                tint = Color(0xFF2E5BFF),
                modifier = Modifier
                    .size(40.dp)
                    .alpha(alphaCheck.value)
                    .scale(alphaCheck.value)
            )
        }

        // Scattered Dots with varied offsets and sizes (Some inside, some outside)
        // Dot 1: Large Yellow (Top Left - Farther out)
        Box(
            modifier = Modifier
                .offset(x = (-85).dp, y = (-75).dp)
                .size(14.dp)
                .alpha(alphaDots[0].value)
                .background(Color(0xFFFFD23F), CircleShape)
        )

        // Dot 2: Medium Yellow (Top Right - Closer in)
        Box(
            modifier = Modifier
                .offset(x = 60.dp, y = (-45).dp)
                .size(12.dp)
                .alpha(alphaDots[1].value)
                .background(Color(0xFFFFD23F), CircleShape)
        )

        // Dot 3: Small Light Blue (Bottom Left - Farther out)
        Box(
            modifier = Modifier
                .offset(x = (-100).dp, y = 40.dp)
                .size(10.dp)
                .alpha(alphaDots[2].value)
                .background(Color(0xFF91B1FF), CircleShape)
        )

        // Dot 4: Small Yellow (Bottom Right - Closer in)
        Box(
            modifier = Modifier
                .offset(x = 55.dp, y = 60.dp)
                .size(9.dp)
                .alpha(alphaDots[3].value)
                .background(Color(0xFFFFD23F), CircleShape)
        )

        // Dot 5: Medium Yellow (Left - Very close to center ring)
        Box(
            modifier = Modifier
                .offset(x = (-65).dp, y = 10.dp)
                .size(11.dp)
                .alpha(alphaDots[4].value)
                .background(Color(0xFFFFD23F), CircleShape)
        )
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float =
    (1 - fraction) * start + fraction * stop
