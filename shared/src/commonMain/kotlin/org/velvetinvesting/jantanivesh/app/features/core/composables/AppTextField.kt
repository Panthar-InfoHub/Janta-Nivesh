package org.velvetinvesting.jantanivesh.app.features.core.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.TextFieldBorder

data class AppTextFieldStyle(
    val shape: Shape,
    val textStyle: TextStyle,
    val colors: TextFieldColors
)

object AppTextFieldDefaults {
    @Composable
    fun style(
        shape: Shape = LocalShapes.current.roundedDp12,
        textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
        unfocusedBorderColor: androidx.compose.ui.graphics.Color = BoxBorder,
        focusedBorderColor: androidx.compose.ui.graphics.Color = TextFieldBorder
    ): AppTextFieldStyle {
        return AppTextFieldStyle(
            shape = shape,
            textStyle = textStyle,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = unfocusedBorderColor,
                focusedBorderColor = focusedBorderColor
            )
        )
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    interactionSource: MutableInteractionSource? = null,
    style: AppTextFieldStyle = AppTextFieldDefaults.style()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = style.textStyle,
        label = label,
        placeholder = placeholder,
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

@Preview(showBackground = true)
@Composable
fun AppTextFieldPreview() {
    JantaNiveshTheme {
        AppTextField(
            value = "Sample Text",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
