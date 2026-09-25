package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.features.core.utils.loadingQuotes
@Composable
fun LoaderScreen(modifier: Modifier= Modifier) {

    var quote by remember{mutableStateOf(loadingQuotes.random())}

    Box(
        modifier = modifier.fillMaxSize()
            .background(Color.White)
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){

//            VelvetLoader()
            CircularProgressIndicator()
            Text(
                text = quote,
                color = Primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun GenericLoader(){
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

    }
}