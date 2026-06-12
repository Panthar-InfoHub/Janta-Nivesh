package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.arrow_down
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Primary

@Composable
fun ExpandableContent(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    heading: String,
    onIconClick: () -> Unit,
    content: @Composable () -> Unit,
) {

    val animatedIcon = animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = ""
    )

    Column(
        modifier=modifier,
    ) {

        Row(
            modifier=Modifier.fillMaxWidth().padding(vertical = 20.dp).clickable(
                onClick = onIconClick,
                indication = null,
                interactionSource = remember{ MutableInteractionSource() }
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text=heading,
                color = Primary,
                style = MaterialTheme.typography.labelLarge
            )

            Icon(
                painter = painterResource(Res.drawable.arrow_down),
                contentDescription=null,
                tint= Primary,
                modifier=Modifier
                    .padding(end = 16.dp)
                    .graphicsLayer{
                        rotationZ=animatedIcon.value
                    }
            )
        }

        AnimatedVisibility(expanded){
            Column{
                content()
                Spacer(modifier=Modifier.height(20.dp))
            }
        }

    }

}