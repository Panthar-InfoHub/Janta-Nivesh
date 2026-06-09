package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.velvet_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.features.core.utils.loadingQuotes
@Composable
fun LoaderScreen(){

    var quote by remember{mutableStateOf(loadingQuotes.random())}

    Box(
        modifier = Modifier.fillMaxSize()
            .clickable(
                onClick = {
                    quote=loadingQuotes.random()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){

//            VelvetLoader()
            CircularProgressIndicator()
            Text(
                text = quote,
                color = Primary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun VelvetLoader(){
    Box(
        contentAlignment = Alignment.Center
    )
    {

        CircularProgressIndicator(
            modifier = Modifier.size(92.dp).graphicsLayer { scaleX = -1f },
            color = Secondary,
            strokeWidth = 4.dp
        )
        CircularProgressIndicator(
            modifier = Modifier.size(108.dp),
            color = Primary,
            strokeWidth = 4.dp
        )

        Box(
            modifier = Modifier.size(72.dp)
                .clip(CircleShape)
                .background(Primary),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.velvet_logo),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}