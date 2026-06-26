package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.subHeadingMedium

@Composable
fun MoneyTextField(
    value:String,
    onValueChange:(String)->Unit,
    placeHolder:String,
    label:String?=null,
    mandatory: Boolean=false,
    showWarning: Boolean= false,
    warningText: String= "",
    modifier: Modifier = Modifier
){

    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        label?.let{
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = label,
                    style = subHeadingMedium,
                    color = Color.Black
                )
                if (mandatory) {
                    Text(
                        text = "*",
                        color = Color.Red,
                        style = subHeadingMedium
                    )
                }
            }
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            visualTransformation = IndianCurrencyVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier.fillMaxWidth()
                .height(54.dp)
                .shadow(elevation = 8.dp,RoundedCornerShape(15.dp), spotColor = Color.Black.copy(alpha = 0.4f))
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White, RoundedCornerShape(15.dp))
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = if (showWarning) Color.Red else Color(0xFFC5A572)
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                horizontalArrangement  = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹",
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier=Modifier.padding(start = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(end = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeHolder,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xffC5C5C5)
                        )
                    }
                    it()
                }
            }
        }

        AnimatedVisibility(showWarning){
            Text(
                text = warningText,
                style = MaterialTheme.typography.displaySmall,
                color = Color.Red
            )
        }

    }
}

class IndianCurrencyVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {

        val input = text.text.filter { it.isDigit() }

        if (input.isEmpty()) {
            return TransformedText(
                AnnotatedString(""),
                OffsetMapping.Identity
            )
        }

        val formatted = formatIndianNumber(input)

        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                val originalText = input.take(offset)
                return formatIndianNumber(originalText).length
            }

            override fun transformedToOriginal(offset: Int): Int {
                var digitCount = 0

                formatted.take(offset).forEach {
                    if (it.isDigit()) digitCount++
                }

                return digitCount.coerceAtMost(input.length)
            }
        }

        return TransformedText(
            AnnotatedString(formatted),
            offsetMapping
        )
    }

    private fun formatIndianNumber(input: String): String {

        if (input.length <= 3) return input

        val lastThree = input.takeLast(3)
        val remaining = input.dropLast(3)

        val formattedRemaining = remaining
            .reversed()
            .chunked(2)
            .joinToString(",")
            .reversed()

        return "$formattedRemaining,$lastThree"
    }
}