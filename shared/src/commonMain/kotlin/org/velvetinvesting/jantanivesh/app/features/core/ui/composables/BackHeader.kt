package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing

@Composable
fun BackHeader(
    title: String,
    onBack: () -> Unit,
    showBack: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.dp8)
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        if (showBack){ AppBackButton(onClick = onBack) }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
