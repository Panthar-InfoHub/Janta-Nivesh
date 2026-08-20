package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.icon_cross
import jantanivesh.shared.generated.resources.info_icon
import jantanivesh.shared.generated.resources.link_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.subHeading
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ContinueBackButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.utils.rememberBrowserReturnLauncher
//import org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens.ConsentSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCutOffPopup(
    visible:Boolean,
    onDismiss:()->Unit,
    onPurchase:()->Unit
){
    var isChecked by remember{ mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = null,
        containerColor = Color.White,
    ){
        Column(
            modifier=Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Row(
                modifier=Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cut-Off Timings",
                    style = MaterialTheme.typography.headlineSmall
                )

                Icon(
                    painter = painterResource(Res.drawable.icon_cross),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                        .clickable(
                            onClick = onDismiss,
                            indication = null,
                            interactionSource = remember{ MutableInteractionSource() }
                        )
                )
            }

            HorizontalDivider(color = titleColor.copy(0.4f))
            Text(
                text = "NAV cut-off timings for mutual fund transactions",
                style = titlesStyle.copy(fontSize = 14.sp),
                color = titleColor,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LOFCard()
                OtherSchemeCard()
                RuleCard()
            }
            val openBrowserLauncher = rememberBrowserReturnLauncher()
            val link = "https://www.amfiindia.com/"
            LinkSection(onLinkClick = {
                openBrowserLauncher.launch(link) {}
            })
//            ConsentSection(
//                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
//                isChecked = isChecked,
//                onToggle = {isChecked=!isChecked},
//                text = "I have read and understood the cut-off timings and NAV rules"
//            )
            Footer(
                onProceedClick= {
                    onPurchase()
                },
                onCancelClick = onDismiss,
                enabled=isChecked
            )
        }
    }

}

@Composable
fun Footer(onProceedClick: () -> Unit, onCancelClick: () -> Unit, enabled: Boolean) {
    ContinueBackButtonFooter(
        continueText = "Proceed",
        backText = "Cancel",
        onContinue = onProceedClick,
        onBack = onCancelClick,
        pv = PaddingValues(0.dp),
        enabled=enabled
    )
}

@Composable
fun LinkSection(onLinkClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.padding(horizontal = 16.dp).clickable(
            onClick = onLinkClick,
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        )
    ){
        Text(
            text = "Read more at AMFI Website",
            style = titlesStyle.copy(fontSize = 14.sp),
            color = Primary,
        )
        Icon(
            painter = painterResource(Res.drawable.link_icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Primary
        )
    }
}

@Composable
fun RuleCard() {
    val textColor= Color(0xffBB4D00)
    Box(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .border(shape = RoundedCornerShape(15.dp), color = Color(0xffFEE685), width = 0.7.dp)
            .background(color = Color(0xffFFFBEB))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.info_icon),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = textColor
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.SemiBold)
                    ) {
                        append("SEBI Rule:")
                    }
                    append(" NAV depends on fund realization before cut-off time.")
                },
                style = titlesStyle,
                color = textColor
            )
        }
    }
}

@Composable
fun OtherSchemeCard() {

    val primary = Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = 0.7.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(14.dp)
            )
            .background(Color(0xFFF9FAFB))
            .padding(16.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Title
            Text(
                text = "All Other Schemes",
                style = subHeading,
                color = Color(0xff111827),
                fontWeight = FontWeight.SemiBold
            )

            // Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Subscription / Redemption",
                    style = titlesStyle,
                    color = Color(0xff364153)
                )

                Text(
                    text = "3:00 p.m.",
                    style = titlesStyle.copy(fontSize = 14.sp),
                    color = primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun LOFCard() {

    val primary = Primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = 0.7.dp,
                color = Color(0xFFDBEAFE),
                shape = RoundedCornerShape(14.dp)
            )
            .background(Color(0xFFEFF6FF))
            .padding(16.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Liquid & Overnight Funds",
                style = subHeading,
                color = primary,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subscription",
                    style = titlesStyle,
                    color = Color(0xff364153)
                )

                Text(
                    text = "1:30 p.m.",
                    style = titlesStyle.copy(fontSize = 14.sp),
                    color = primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Redemption",
                    style = titlesStyle,
                    color = Color(0xff364153)
                )

                Text(
                    text = "3:00 p.m.",
                    style = titlesStyle,
                    color = primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview
@Composable
fun CartCutOffPopupPreview() {
    JantaNiveshTheme {
        Box(modifier = Modifier.background(Color.White)) {
            CartCutOffPopup(
                visible = true,
                onDismiss = {},
                onPurchase = {}
            )
        }
    }
}

@Preview
@Composable
fun FooterPreview() {
    JantaNiveshTheme {
        Footer(onProceedClick = {}, onCancelClick = {}, enabled = true)
    }
}

@Preview
@Composable
fun LinkSectionPreview() {
    JantaNiveshTheme {
        LinkSection(onLinkClick = {})
    }
}

@Preview
@Composable
fun RuleCardPreview() {
    JantaNiveshTheme {
        RuleCard()
    }
}

@Preview
@Composable
fun OtherSchemeCardPreview() {
    JantaNiveshTheme {
        OtherSchemeCard()
    }
}

@Preview
@Composable
fun LOFCardPreview() {
    JantaNiveshTheme {
        LOFCard()
    }
}