package org.velvetinvesting.jantanivesh.app.core.webview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.icon_cross
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.utils.AppBackHandler

@Composable
fun WebViewScreen(
    config: WebViewConfig,
    onExitUrlReached: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberWebViewState(config.url)



    Column(modifier = modifier.fillMaxSize()) {
        WebViewHeader(
            heading = config.title.orEmpty(),
            showBack = true,
            onBackClick = onBackClick
        )

        Box(modifier = Modifier.fillMaxSize()) {
            PlatformWebView(
                state = state,
                modifier = Modifier.fillMaxSize(),
                onUrlChanged = { url ->
                    if (WebViewExitUrlMatcher.matches(
                            url,
                            config.exitUrlPatterns,
                            config.matchType
                        )
                    ) {
                        onExitUrlReached(url)
                    }
                }
            )
        }
    }
}


@Composable
private fun WebViewHeader(
    heading: String,
    showBack: Boolean = false,
    modifier:Modifier=Modifier.fillMaxWidth(),
    onBackClick: () -> Unit={},
    rightContent: @Composable () -> Unit={}
){
    Box(
        modifier=modifier
            .background(Color.White)
            .padding(bottom = 16.dp, start = 12.dp, end = 12.dp, top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = heading,
            style = MaterialTheme.typography.headlineSmall,
            color = Primary
        )

        if (showBack){
            Icon(
                painter = painterResource(Res.drawable.icon_cross),
                contentDescription = null,
                modifier = Modifier.size(22.dp).clickable(
                    onClick = onBackClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ).align(Alignment.CenterStart)
            )
        }

        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            rightContent()
        }
    }
}