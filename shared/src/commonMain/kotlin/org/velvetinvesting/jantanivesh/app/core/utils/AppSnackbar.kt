package org.velvetinvesting.jantanivesh.app.core.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.icon_cross
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppSnackbar(
    snackbarData: SnackbarData,
    type: SnackBarType?
) {

    val containerColor = when (type) {
        is SnackBarType.Success -> Color(0xFF2E7D32)
        is SnackBarType.Error -> Color(0xFFC62828)
        is SnackBarType.Warning -> Color(0xFFF9A825)
        is SnackBarType.Info -> Color(0xFF1565C0)
        is SnackBarType.Neutral, null -> Color.DarkGray
    }

    Snackbar(
        containerColor = containerColor,
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = snackbarData.visuals.message,
                modifier = Modifier.weight(1f),
                color = Color.White,
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 0.3.sp
            )

            IconButton(
                onClick = snackbarData::dismiss
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_cross),
                    contentDescription = "Dismiss",
                    tint = Color.White
                )
            }
        }
    }
}