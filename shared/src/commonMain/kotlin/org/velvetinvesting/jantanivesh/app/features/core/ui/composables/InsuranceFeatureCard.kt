package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.ins_high_coverage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.FeatureCardText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.UploadBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow


@Composable
fun InsuranceFeatureCard(modifier: Modifier= Modifier, icon: DrawableResource, text: String){

        Box(
            modifier = modifier.height(110.dp).genericDropShadow(RoundedCornerShape(Spacing.dp12)).clip(RoundedCornerShape(Spacing.dp12)).border(
                width = 1.dp, color = UploadBoxBorder, shape = RoundedCornerShape(
                    Spacing.dp12
                )
            ).background(color = White, shape = RoundedCornerShape(Spacing.dp12)).padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                        .background(color = UploadBoxBorder, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = "high coverage icon", tint = Primary
                    )
                }
                Text(
                    text, maxLines = 2,
                    style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold,
                    color = FeatureCardText
                )
            }

    }

}
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun InsuranceFeatureCardPreview() {
//    JantaNiveshTheme {
//        InsuranceFeatureCard()
//    }
//}
