package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.LightGrayBorder
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel

@Composable
fun GenericPill(
    text: String,
    accent: Color = Color(0xff334155),
    background: Color = Color(0xffF1F5F9),
    outline: Color = LightGrayBorder,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = tinyLabel.copy(fontWeight = FontWeight.SemiBold),
        color = accent,
        modifier = modifier
            .clip(LocalShapes.current.circle)
            .background(background)
            .border(0.6.dp, outline, LocalShapes.current.circle)
            .padding(horizontal = Spacing.dp12, vertical = Spacing.dp6)
    )
}