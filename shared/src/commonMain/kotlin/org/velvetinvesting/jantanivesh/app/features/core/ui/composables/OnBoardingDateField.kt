package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.dob_dropdown_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder

@Preview
@Composable
fun OnBoardingDateFieldPreview(){
    JantaNiveshTheme {
        OnBoardingDateField(value = "Value", placeholder = "Placeholder", label = "Label", onClick = {})
    }
}

@Composable
fun OnBoardingDateField(
    value: String,
    placeholder: String,
    label: String,
    mandatory: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(verticalAlignment = Alignment.Top) {

            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                color = Color(0xff44464F)
            )

            if (mandatory) {
                Text(
                    text = "*",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(15.dp),
                    spotColor = Color.Black.copy(alpha = 0.4f)
                )
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = SelectedBoxBorder // TODO add isSelected to change border dynamically
                )
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            Row(
                modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = value.ifEmpty { placeholder },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (value.isEmpty()) Color(0xffC5C5C5) else Color.Black,
                    maxLines = 1,
                    modifier=Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(Res.drawable.dob_dropdown_icon),
                    contentDescription = null,
                    modifier=Modifier.padding(horizontal = 8.dp).size(20.dp),
                    tint= Primary
                )
            }
        }
    }
}