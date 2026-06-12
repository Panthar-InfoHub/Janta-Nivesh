package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor

@Composable
fun RiskSection(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    riskLevel: Int,
    riskText: String
) {

    val progress = (riskLevel.coerceIn(1, 5)) / 5f

    val riskColor = when (riskLevel) {
        1 -> Color(0xFF2ECC71)
        2 -> Color(0xFF1B8F2A)
        3 -> Color(0xFF8BC34A)
        4 -> Color(0xFFF9A825)
        5 -> Color(0xFFF57C00)
        6 -> Color(0xFFD32F2F)
        else -> Color.Gray
    }

    ExpandableContent(
        modifier = modifier,
        expanded = expanded,
        heading = "Risk",
        onIconClick = onExpandToggle
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Spacer(Modifier.height(8.dp))

            // 🔹 Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Riskometer Level",
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xff4A5565)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(riskColor.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = riskText.uppercase(),
                        fontSize = 12.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = riskColor
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFE5E7EB))
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF1B8F2A),
                                    Color(0xFFF9A825),
                                    Color(0xFFD32F2F)
                                )
                            )
                        )
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = getRiskDescription(riskLevel),
                fontSize = 14.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                color = titleColor,
                lineHeight = 20.sp
            )
        }
    }
}

fun getRiskDescription(riskLevel: Int): String {
    return when (riskLevel) {

        1 -> "This fund carries very low risk. Suitable for highly conservative investors seeking capital preservation with minimal fluctuations."

        2 -> "This fund carries low risk. Suitable for conservative investors looking for stable returns with low volatility."

        3 -> "This fund carries moderately low risk. Suitable for investors seeking slightly better returns than traditional options with limited risk exposure."

        4 -> "This fund carries moderately high risk. Suitable for investors willing to accept moderate volatility in pursuit of higher returns."

        5 -> "This fund carries high risk. Suitable for investors aiming for substantial growth and comfortable with significant market fluctuations."

        6 -> "This fund carries very high risk. Suitable for aggressive investors targeting long-term wealth creation and who can tolerate high volatility."

        else -> "Risk level information is unavailable."
    }
}