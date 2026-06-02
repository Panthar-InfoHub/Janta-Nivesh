package org.velvetinvesting.jantanivesh.app.features.core.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.dob_dropdown_icon
import jantanivesh.shared.generated.resources.dropdown_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Border
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.Primary

@Composable
fun<T> DropDownSelector(
    value: String,
    onValueChange: (T) -> Unit,
    placeHolder: String,
    mandatory: Boolean =false,
    label: String,
    modifier: Modifier = Modifier,
    list: List<T>,
    textConvertor:(T)->String
){
    var extended by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                        fontFamily = MaterialTheme.typography.labelMedium.fontFamily
                    )
                ) {
                    append(label)
                }

                if (mandatory) {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Red,
                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                            fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                            fontFamily = MaterialTheme.typography.labelMedium.fontFamily
                        )
                    ) {
                        append(" *")
                    }
                }
            },
            style = MaterialTheme.typography.labelMedium
        )
        Column(
            modifier=Modifier
                .shadow(elevation = 8.dp,RoundedCornerShape(15.dp), spotColor = Color.Black.copy(alpha = 0.4f))
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White, RoundedCornerShape(15.dp))
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = BoxBorder
                )
                .animateContentSize()
        ) {
            GenericDropDownHeader(
                value = value,
                placeHolder = placeHolder,
                onClick = {extended=!extended},
                extended =extended
            )
            if (extended){
                GenericDropDownContent(
                    list=list,
                    textConvertor=textConvertor,
                    onSelected={it->
                        onValueChange(it)
                        extended=false
                    }
                )
            }
        }
    }
}

@Composable
private fun <T>GenericDropDownContent(
    onSelected: (T) -> Unit,
    list: List<T>,
    textConvertor: (T) -> String
) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {

        list.forEach { it->
            Text(
                text = textConvertor(it),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    .clickable(
                        onClick = {onSelected(it)}
                    )
            )
        }

    }

}

@Composable
private fun GenericDropDownHeader(value:String, placeHolder: String, onClick: () -> Unit, extended: Boolean) {
    val animatedIcon by animateFloatAsState(
        targetValue = if (extended) 180f else 0f,
        label = "arrow")
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(54.dp)
            .clickable(
                onClick={onClick()}
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text= (value.ifEmpty { placeHolder }).capitalize(Locale.current),
            style = MaterialTheme.typography.labelSmall,
            color = if (value.isBlank()) Border else Primary,
            modifier = Modifier.weight(1f).padding(start = 16.dp)
        )
        Icon(
            painter = painterResource(Res.drawable.dropdown_icon),
            contentDescription = null,
            modifier = Modifier.padding(end = 24.dp).rotate(animatedIcon),
            tint = Primary
        )
    }

}