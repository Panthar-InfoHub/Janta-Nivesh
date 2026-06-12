package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.search_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor

@Composable
fun AppSearchBar(
    onTextChange: (String) -> Unit,
    value: String,
    placeholder: String = "Search For Funds....",
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit
) {
    val baseStyle = AppTextFieldDefaults.style(
        shape = CircleShape
    )

    OutlinedTextField(
        value = value,
        onValueChange = onTextChange,
        modifier = modifier.height(52.dp),
        shape = baseStyle.shape,
        singleLine = true,
        textStyle = baseStyle.textStyle.copy(fontSize = 14.sp),
        placeholder = {
            Text(
                text = placeholder,
                style = baseStyle.textStyle.copy(fontSize = 14.sp),
                color = titleColor
            )
        },
        colors = baseStyle.colors,
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.search_icon),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .height(22.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClick() }
        )
    )
}