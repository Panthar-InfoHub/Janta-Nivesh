package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import kotlin.String

@Preview(showBackground = true)
@Composable
fun TitledAppTextFieldPreview() {
    JantaNiveshTheme {
        TitledAppTextField(
            title = "title",
            value = "value",
            onValueChange = {},
            mandatory = true,
            placeholder = "placeholder"
        )
    }
}

@Composable
fun TitledAppTextField(
    title: String = "",
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    interactionSource: MutableInteractionSource? = null,
    style: AppTextFieldStyle = AppTextFieldDefaults.style(),
    mandatory: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8), modifier = modifier) {
        if(title.isNotEmpty())
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                    color = Color(0xff44464F)
                )
                if (mandatory) Text("*", color = MaterialTheme.colorScheme.error)
            }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = style.textStyle,
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                    color = GreyText
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            isError = isError,
            keyboardOptions = keyboardOptions,
            interactionSource = interactionSource ?: remember { MutableInteractionSource() },
            shape = style.shape,
            colors = style.colors
        )
    }
}