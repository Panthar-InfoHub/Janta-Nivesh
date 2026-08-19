package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.Gray65
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.tagColor

@Composable
fun InfoNoteCard(icon: DrawableResource, title: String, subtitle: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp24))
            .border(
                width = Spacing.dp1,
                color = FilterChipUnselected,
                RoundedCornerShape(Spacing.dp24)
            )
            .background(tagColor)
            .padding(Spacing.dp20),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "Info Icon",
            tint = Primary,
            modifier = Modifier.size(Spacing.dp20)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Primary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleSmall,
                color = Gray65,
            )
        }
    }
}