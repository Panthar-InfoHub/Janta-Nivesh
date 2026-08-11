package org.velvetinvesting.jantanivesh.app.features.splashscreen.ui.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.ic_pagerbackground
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.ImageSize
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.PagerImageCircle
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.SplashGrey
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.JantaNiveshAndVelvetLogo
import org.velvetinvesting.jantanivesh.app.features.splashscreen.domain.models.pagerImageList
import org.velvetinvesting.jantanivesh.app.features.splashscreen.ui.viewmodels.SplashScreenEvent
import org.velvetinvesting.jantanivesh.app.features.splashscreen.ui.viewmodels.SplashScreenUiState

@Composable
fun SplashScreen(
    handleEvent: (SplashScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { pagerImageList.size })

    Box(modifier = modifier.fillMaxSize().background(White)) {
        Image(
            painter = painterResource(Res.drawable.ic_pagerbackground),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxHeight().padding(top = Spacing.dp48)
                .padding(horizontal = Spacing.dp24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Spacing.dp22)
            ) {
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = Spacing.dp12,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier.clip(LocalShapes.current.roundedDp24)
                                .aspectRatio(1f)
                        ) {
                            Image(
                                painter = painterResource(pagerImageList[page].image),
                                contentDescription = "Pager Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(ImageSize.dp320)
                            )
                        }
                        Text(
                            pagerImageList[page].title,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Secondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = Spacing.dp12)
                                .padding(top = Spacing.dp32)
                        )
                        Text(
                            pagerImageList[page].subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = SplashGrey,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = Spacing.dp16)
                                .padding(top = Spacing.dp12)
                        )
                    }
                }
            }
            Row(
                Modifier
                    .padding(bottom = Spacing.dp24),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Secondary else PagerImageCircle
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color)
                            .animateContentSize()
                            .height(Spacing.dp8)
                            .width(if (pagerState.currentPage == iteration) Spacing.dp32 else Spacing.dp8)
                    )
                }
            }
            JantaNiveshAndVelvetLogo()
            AppButton(
                text = "Get Started",
                onClick = { handleEvent(SplashScreenEvent.OnGetStartedClick) },
                modifier = Modifier.fillMaxWidth().padding(bottom = Spacing.dp8)
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    JantaNiveshTheme {
        SplashScreen(handleEvent = {})
    }
}