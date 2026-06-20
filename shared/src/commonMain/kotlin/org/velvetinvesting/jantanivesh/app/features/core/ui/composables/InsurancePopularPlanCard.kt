package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.Gray65
import org.velvetinvesting.jantanivesh.app.core.theme.LogoBackgroundColor
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.UploadBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.tagColor
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow

@Composable
fun InsurancePopularPlansCard(
    id: String,
    icon:String,
    title:String,
    subTitle:String,
    tag:String,
    coverAmount:String,
    premium:String,
    onClick:(String)->Unit,

){
    Box(modifier = Modifier.genericDropShadow(RoundedCornerShape(Spacing.dp24)).clip(RoundedCornerShape(Spacing.dp24)).border(1.dp, color = UploadBoxBorder, shape = RoundedCornerShape(
        Spacing.dp24)).background(color = White, shape = RoundedCornerShape(
        Spacing.dp24)).clickable{onClick(id)}.padding(16.dp)){
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(modifier = Modifier.clip(RoundedCornerShape(Spacing.dp12)).background(color = LogoBackgroundColor, shape = RoundedCornerShape(
                Spacing.dp12)))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(38.dp)
                        .genericDropShadow()
                        .background(Color.White),
                    model = icon,
                    contentDescription = null,
                    loading = {
                        MutualFundIcon(
                            schemeName = title,
                            size = 38.dp
                        )
                    },
                    error = {
                        MutualFundIcon(
                            schemeName = title,
                            size = 38.dp
                        )
                    },
                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.labelLarge, color = Black)
                    Text(subTitle, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal), color = Gray65)
                }

                Box(modifier=Modifier.background(tagColor, shape = RoundedCornerShape(50.dp)).padding(horizontal = 12.dp, vertical = 4.dp)){
                    Text(tag, style = MaterialTheme.typography.titleSmall, color = Primary)
                }
            }

            Box(Modifier.clip(RoundedCornerShape(Spacing.dp12)).background(color =UploadBoxBorder).padding(
                Spacing.dp16)){
                Row(Modifier.fillMaxWidth()) {
                    Column (modifier = Modifier.weight(1f)){
                        Text("Cover Amount", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal), color = Gray444)
                        Text(coverAmount, style = MaterialTheme.typography.labelLarge)

                    }

                    Column (modifier = Modifier.weight(1f)){
                        Text("Premium", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal), color = Gray444)
                      Row {
                          Text(premium, style = MaterialTheme.typography.labelLarge, color = Black)
                          }
                    }
                }


            }

            }
        }
    }

@Composable
fun MutualFundIcon(
    schemeName: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    cornerRadius: Dp = 12.dp,
    backgroundColor: Color = LogoBackgroundColor,
    textColor: Color = Primary
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = schemeName
                .take(1).capitalize(Locale.current),
            style = MaterialTheme.typography.headlineSmall,
            color = textColor
        )
    }
}


@Composable
fun InsurancePopularPlansCardGeneral(
    id: String,
    icon:String,
    title:String,
    subTitle:String,
    coverAmount:String,
    premium:String,
    onClick:(String)->Unit,

    ){
    Box(modifier = Modifier.genericDropShadow(RoundedCornerShape(Spacing.dp24)).clip(RoundedCornerShape(Spacing.dp24)).border(1.dp, color = UploadBoxBorder, shape = RoundedCornerShape(
        Spacing.dp24)).background(color = White, shape = RoundedCornerShape(
        Spacing.dp24)).clickable{onClick(id)}.padding(16.dp)){
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(modifier = Modifier.clip(RoundedCornerShape(Spacing.dp12)).background(color = LogoBackgroundColor, shape = RoundedCornerShape(
                Spacing.dp12)))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(38.dp)
                        .genericDropShadow()
                        .background(Color.White),
                    model = icon,
                    contentDescription = null,
                    loading = {
                        MutualFundIcon(
                            schemeName = title,
                            size = 38.dp
                        )
                    },
                    error = {
                        MutualFundIcon(
                            schemeName = title,
                            size = 38.dp
                        )
                    },
                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.labelLarge, color = Black)
                    Text(subTitle, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal), color = Gray65)
                }
            }

            Box(Modifier.clip(RoundedCornerShape(Spacing.dp12)).background(color =UploadBoxBorder).padding(
                Spacing.dp16)){
                Row(Modifier.fillMaxWidth()) {
                    Column (modifier = Modifier.weight(1f)){
                        Text("Cover Amount", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Gray65)
                        Text(coverAmount, style = MaterialTheme.typography.labelSmall, color = Black,
                            fontWeight = FontWeight.Bold)

                    }

                    Column (modifier = Modifier.weight(1f)){
                        Text("Premium", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Gray65)

                            Text(premium, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),color = Black)

                    }
                }


            }

        }
    }
}



