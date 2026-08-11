package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.ic_leaf
import jantanivesh.shared.generated.resources.img_aditya_logo
import jantanivesh.shared.generated.resources.img_amfi_logo
import jantanivesh.shared.generated.resources.img_kycsplash
import jantanivesh.shared.generated.resources.img_ondc_logo
import jantanivesh.shared.generated.resources.img_upi_logo
import jantanivesh.shared.generated.resources.upward_trend_arrow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.leafIconBackground
import org.velvetinvesting.jantanivesh.app.core.theme.lightBlue
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.KycSplashEvent

@Composable
fun KycSplashScreen(
    handleEvent: (KycSplashEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = Spacing.dp24)
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = Spacing.dp24)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                        Text(
                            text = "Start growing your saving in minutes",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 26.sp),
                            color = Primary,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Janta Nivesh simplifies your wealth management with an empathetic approach to saving, investing, and growing.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Gray444,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp20)) {
                        TitleBox(
                            title = "KYC & Bank Verification",
                            subtitle = "Identify your life goals from buying a home to early retirement.",
                            number = "1"
                        )
                        TitleBox(
                            title = "Setup Savings",
                            subtitle = "Our smart engine allocates funds based on your comfort level and pace.",
                            number = "2"
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ImageBox()
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
                    modifier = Modifier.fillMaxWidth().padding(bottom = Spacing.dp8)
                ) {
                    Text(
                        text = "INTEGRATION PARTNERS",
                        style = MaterialTheme.typography.titleMedium.copy(letterSpacing = 1.4.sp),
                        color = Gray444
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp20)) {
                        LogoBox(Res.drawable.img_upi_logo, "Upi Logo")
                        LogoBox(Res.drawable.img_amfi_logo, "A M F I Logo")
                        LogoBox(Res.drawable.img_aditya_logo, "Aditya Birla Group Logo")
                        LogoBox(Res.drawable.img_ondc_logo, "O N D C Logo")
                    }
                }
            }
        }

        AppButton(
            text = "Proceed",
            onClick = { handleEvent(KycSplashEvent.OnProceedClick) },
            modifier = Modifier
                .padding(top = Spacing.dp16)
                .genericDropShadow()
                .fillMaxWidth()
        )
    }
}

@Composable
private fun TitleBox(title: String, subtitle: String, number: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        verticalAlignment = Alignment.Top
    ) {

        Box(
            modifier = Modifier.genericDropShadow(CircleShape).size(Spacing.dp48).background(lightBlue, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Primary
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = Gray444
            )
        }
    }
}

@Composable
private fun ImageBox() {
    Box(
        modifier = Modifier.padding(Spacing.dp32)
    ) {
        Box(
            modifier = Modifier
                .genericDropShadow(shape = RoundedCornerShape(Spacing.dp40))
                .border(
                    width = Spacing.dp12,
                    color = White,
                    shape = RoundedCornerShape(Spacing.dp40)
                )
                .clip(RoundedCornerShape(Spacing.dp40))
                .aspectRatio(1f)
        ) {
            Image(
                painter = painterResource(Res.drawable.img_kycsplash),
                contentDescription = "KYC placeholder image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            WellnessScoreBox(
                modifier = Modifier.padding(Spacing.dp24).align(Alignment.BottomStart)
            )
        }

        Box(
            modifier = Modifier
                .size(Spacing.dp72)
                .align(Alignment.TopEnd)
                .offset(x = Spacing.dp16, y = (-Spacing.dp16))
                .background(leafIconBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_leaf),
                contentDescription = "Decorative leaf icon",
                tint = Primary,
                modifier = Modifier.size(Spacing.dp34)
            )
        }
    }
}

@Composable
private fun WellnessScoreBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(White, CircleShape)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            modifier = Modifier.padding(Spacing.dp12, vertical = Spacing.dp8)
        ) {
            Box(
                modifier = Modifier.size(Spacing.dp48).background(lightBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.upward_trend_arrow),
                    contentDescription = "upward trend icon",
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp20)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "WELLNESS SCORE",
                    fontSize = 8.sp,
                    letterSpacing = 0.6.sp,
                    color = Primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "84/100",
                    style = MaterialTheme.typography.titleLarge,
                    color = Primary
                )
            }
        }
    }
}

@Composable
private fun LogoBox(logo: DrawableResource, contentDescription: String) {
    Box(
        modifier = Modifier
            .genericDropShadow(shape = RoundedCornerShape(Spacing.dp10))
            .size(65.dp, 53.dp)
            .clip(RoundedCornerShape(Spacing.dp10))
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(logo),
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(Spacing.dp8)
        )
    }
}

@Preview
@Composable
private fun KycSplashScreenPreview() {
    JantaNiveshTheme {
        KycSplashScreen(
            handleEvent = {}
        )
    }
}