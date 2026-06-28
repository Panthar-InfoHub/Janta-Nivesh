package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.presentation.portfolio.models.StopSipReason
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle

@Composable
fun StopSipReasonSelection(
    reasons: List<StopSipReason>,
    selectedId: Int?,
    onReasonSelected: (StopSipReason) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        reasons.forEach { reason ->
            val isSelected = selectedId == reason.id
            ReasonItem(
                reason = reason,
                isSelected = isSelected,
                onClick = { onReasonSelected(reason) }
            )
        }
    }
}

@Composable
fun ReasonItem(
    reason: StopSipReason,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFFB5892E) else Color(0xFFE0E0E0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min=52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(Modifier.padding(horizontal = 2.dp))
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Text(
            text = reason.id.toString().padStart(2, '0'),
            style = titlesStyle,
            color = titleColor
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = reason.title,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black,
        )
    }
}

