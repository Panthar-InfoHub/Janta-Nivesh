package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing

@Composable
fun YesNoRadioGroup(
    title: String,
    selectedCode: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    mandatory: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        // Title Row (Matching your TitledAppTextField / DropDownSelector convention)
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = Color(0xFF44464F) // Standard gray title color used in your app
            )
            if (mandatory) {
                Text(
                    text = "*",
                    color = Color.Red,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = Spacing.dp2)
                )
            }
        }

        // Radio Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
            // "Yes" Option
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null, // Removes ripple to match clean image look if tapped on row
                    onClick = { onValueChange("Y") }
                )
            ) {
                RadioButton(
                    selected = selectedCode == "Y",
                    onClick = { onValueChange("Y") },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Black,
                        unselectedColor = Color(0xffC6C5D1) // Gray unselected outline
                    )
                )
                Text(
                    text = "Yes",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Black,
                )
            }

            // "No" Option
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onValueChange("N") }
                )
            ) {
                RadioButton(
                    selected = selectedCode == "N",
                    onClick = { onValueChange("N") },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Black,
                        unselectedColor = Color(0xFFC5C5C5)
                    )
                )
                Text(
                    text = "No",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Black,
                )
            }
        }
    }
}