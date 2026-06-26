package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.graphics_portfolio_empty
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton

@Composable
fun EmptyFundScreen(onBrowseClick: () -> Unit, text:String, buttonText:String) {

    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = painterResource(Res.drawable.graphics_portfolio_empty),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                contentScale = ContentScale.FillWidth
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text=text,
                style = titlesStyle,
                color = titleColor,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            AppButton(
                text = buttonText,
                onClick = onBrowseClick
            )

        }

    }

}