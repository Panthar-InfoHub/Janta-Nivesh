package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.push_notifications_rafiki_1
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader

@Composable
fun NotificationScreen(onBack: () -> Unit, ) {
    var selected by remember { mutableStateOf("Alert") }
    Column() {
        BackHeader(
            title = "Notification centre",
            showBack = true,
            onBack = onBack,
            modifier = Modifier.padding(horizontal = Spacing.dp16)
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextBox(
                    text = "Alert",
                    isSelected = selected == "Alert",
                    onSelect = { selected = "Alert" })
                TextBox(
                    text = "Transaction",
                    isSelected = selected == "Transaction",
                    onSelect = { selected = "Transaction" })

            }
            Box(
                modifier = Modifier.fillMaxSize().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.push_notifications_rafiki_1),
                    contentDescription = "Push Notification Image",
                    modifier = Modifier.size(320.dp)
                )
            }
        }
    }
    }




@Composable
fun TextBox(text: String, isSelected: Boolean, onSelect: () -> Unit) {
    Column(modifier = Modifier
        .width(IntrinsicSize.Min)
        .clickable(
        onClick = onSelect,
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    )) {
        Text(
            text = text,
            fontSize = 20.sp,
            color = if (isSelected) Color.Black else Color.Gray,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFontFamily,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        if (isSelected){
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().clip(CircleShape),
                color = Secondary,
                thickness = 2.dp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Prev(){
    JantaNiveshTheme {
        NotificationScreen(onBack = {})
    }
}