package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.velvet_logo
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing

data class PoweredByVelvetColors(
    val velvetTextColor: Color = Color(0xffD7AF6A),
    val investingTextColor: Color = Color(0xff1B3163),
    val poweredByTextColor: Color = Color(0xff656565),
)

@Preview(showBackground = true)
@Composable
fun PoweredByVelvet() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            Spacing.dp4
        )
    ) {
        Text("Powered By", fontWeight = FontWeight.Normal, fontSize = 10.sp, color = PoweredByVelvetColors().poweredByTextColor)
        Icon(
            painter = painterResource(Res.drawable.velvet_logo),
            contentDescription = "Velvet Logo",
            modifier = Modifier.size(Spacing.dp20),
            tint = Color.Unspecified
        )
        Text("VELVET", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = PoweredByVelvetColors().velvetTextColor)
        Text("Investing", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = PoweredByVelvetColors().investingTextColor)
    }
}