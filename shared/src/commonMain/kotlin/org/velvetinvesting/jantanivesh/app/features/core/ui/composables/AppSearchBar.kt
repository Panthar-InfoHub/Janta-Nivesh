package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.search_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Border
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
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
                color = titleColor,
                modifier= Modifier.padding(start = Spacing.dp8)
            )
        },
        colors = baseStyle.colors,
        trailingIcon = {
            Icon(
                painter = painterResource(Res.drawable.search_icon),
                contentDescription = null,
                tint = if (value.isBlank()) GreyText else Primary,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .height(22.dp)
                    .clickable {
                        if (value.isNotBlank()) {
                            onSearchClick()
                        }
                    }
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

/**
 * A search bar that cannot be typed into — tapping it opens the full search surface instead.
 *
 * Screens that show results inline keep the real [AppSearchBar]. This one is for screens where
 * searching means leaving: it looks identical, so the affordance is unchanged, but there is no
 * IME to raise and dismiss over content the user is still browsing.
 */
@Composable
fun AppSearchBarButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search For Funds....",
    /**
     * An optional action parked at the end of the bar — the fund list hangs its filter tray
     * here. It has its own click target, so tapping it does not also open the search.
     */
    trailingIcon: DrawableResource? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(CircleShape)
            .background(White)
            .border(width = 1.dp, color = Border, shape = CircleShape)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(horizontal = Spacing.dp20),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.search_icon),
            contentDescription = null,
            tint = GreyText,
            modifier = Modifier.height(22.dp)
        )
        Text(
            text = placeholder,
            style = AppTextFieldDefaults.style(shape = CircleShape).textStyle
                .copy(fontSize = 14.sp),
            color = titleColor,
            modifier = Modifier.weight(1f)
        )

        if (trailingIcon != null) {
            Icon(
                painter = painterResource(trailingIcon),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier
                    .size(Spacing.dp22)
                    .clickable(
                        onClick = { onTrailingIconClick?.invoke() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        enabled = onTrailingIconClick != null
                    )
            )
        }
    }
}
