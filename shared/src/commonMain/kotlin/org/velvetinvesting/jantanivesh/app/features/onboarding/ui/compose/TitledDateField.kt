package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White

/**
 * Looks like a [org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField]
 * but is a plain box, so a tap simply opens a picker — a date picker, or the goal year wheel. A
 * read-only text field still runs its own tap handling, which on iOS swallows the tap before the
 * picker can open.
 */
@Composable
fun TitledDateField(
    title: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "DD-MM-YYYY",
    mandatory: Boolean = true,
    /** Drawn at the end of the box, like an OutlinedTextField's trailing icon. */
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val shape = LocalShapes.current.roundedDp12

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4), modifier = modifier) {
        if (title.isNotEmpty())
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                Text(
                    title,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xff434843)
                )
                if (mandatory) Text("*", color = MaterialTheme.colorScheme.error)
            }
        // Matches the unfocused OutlinedTextField: its minimum height, 16dp inner padding,
        // 1dp border and white container.
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = OutlinedTextFieldDefaults.MinHeight)
                .clip(shape)
                .background(White)
                .border(OutlinedTextFieldDefaults.UnfocusedBorderThickness, BoxBorder, shape)
                .clickable(onClick = onClick)
                .padding(horizontal = Spacing.dp16)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        placeholder,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                        color = GreyText
                    )
                } else {
                    Text(
                        value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            trailingIcon?.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TitledDateFieldPreview() {
    JantaNiveshTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
            TitledDateField(title = "Date of Birth", value = "", onClick = {})
            TitledDateField(title = "Date of Birth", value = "12-05-1994", onClick = {})
        }
    }
}
