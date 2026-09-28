package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.ic_bhim
import jantanivesh.shared.generated.resources.ic_cred
import jantanivesh.shared.generated.resources.ic_gpay
import jantanivesh.shared.generated.resources.ic_paytm
import jantanivesh.shared.generated.resources.ic_phonepay
import jantanivesh.shared.generated.resources.img_upi_logo
import jantanivesh.shared.generated.resources.pay_one_rupee
import jantanivesh.shared.generated.resources.pay_one_rupee_by_qr
import jantanivesh.shared.generated.resources.pay_one_rupee_via
import jantanivesh.shared.generated.resources.plus_icon
import jantanivesh.shared.generated.resources.tick_icon
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.deeplink.ExternalAppUrl
import org.velvetinvesting.jantanivesh.app.core.deeplink.rememberExternalAppReturnLauncher
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.HighlightRowBg
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.bgColor4
import org.velvetinvesting.jantanivesh.app.core.utils.AppBackHandler
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.UpiApp
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.UpiPaymentOption
import kotlin.coroutines.cancellation.CancellationException

/**
 * Picks where the ₹1 verification is paid from. Each app opens the payment page aimed at that
 * app; the last row opens the plain page, which shows a QR code to scan from any UPI app.
 *
 * The payment page never opens the UPI app itself: it closes on the app link, and this screen
 * opens it ([appLinkToOpen]) once it is back in front. The user's return then lands here, where
 * every next step is — so dismissing the app chooser never strands them on a blank page.
 *
 * @param onReturnedFromApp gets whether this app was fully backgrounded while the user was away.
 */
@Composable
fun SelectUpiAppScreen(
    apps: List<UpiApp>,
    canPayByQr: Boolean,
    selectedOption: UpiPaymentOption?,
    notice: String?,
    canCheckStatus: Boolean,
    appLinkToOpen: String?,
    awaitingAppReturn: Boolean,
    onOptionSelected: (UpiPaymentOption) -> Unit,
    onPayClick: () -> Unit,
    onCheckStatusClick: () -> Unit,
    onBackClick: () -> Unit,
    onAppLinkOpening: () -> Unit,
    onAppLinkNotOpened: () -> Unit,
    onReturnedFromApp: (wasStopped: Boolean) -> Unit,
    onFormSwitch: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppBackHandler(enabled = true, onBack = onBackClick)

    UpiAppLauncherEffect(
        appLinkToOpen = appLinkToOpen,
        awaitingAppReturn = awaitingAppReturn,
        onAppLinkOpening = onAppLinkOpening,
        onAppLinkNotOpened = onAppLinkNotOpened,
        onReturnedFromApp = onReturnedFromApp
    )

    Column(modifier = modifier.fillMaxSize().background(White)) {

        BackHeader(
            title = "",
            onBack = onBackClick,
            showBack = true,
            modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.dp20),
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
            contentPadding = PaddingValues(bottom = Spacing.dp20)
        ) {
            item{
                Text(
                    text = "Select UPI app",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Black
                )
            }
            item{
                Text(
                    text = buildAnnotatedString {
                        append("Authorize the ₹1 penny-drop verification from your bank UPI. This amount will be ")
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = Black)) {
                            append("refunded instantly")
                        }
                        append(".")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray444,
                    modifier = Modifier.padding(bottom = Spacing.dp8)
                )
            }

            item{
                notice?.let {
                    NoticeBanner(
                        message = it,
                        action = if (canCheckStatus) {
                            {
                                Text(
                                    text = "Check Status",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Primary,
                                    modifier = Modifier.clickable(onClick = onCheckStatusClick)
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }

            items(apps){app->
                    val option = UpiPaymentOption.App(app)
                    UpiAppOptionCard(
                        app = app,
                        selected = selectedOption == option,
                        onClick = { onOptionSelected(option) }
                    )
            }

            if (canPayByQr) {
                item {
                    ScanQrOptionCard(
                        selected = selectedOption == UpiPaymentOption.ScanQr,
                        onClick = { onOptionSelected(UpiPaymentOption.ScanQr) }
                    )
                }
            }

            item{
                SwitchToForm(
                    onClick= onFormSwitch
                )
            }

            item{
                ImportantNoteCard(
                    body = buildAnnotatedString {
                        append("The ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("₹1 verification amount")
                        }
                        append(" will be refunded to your account. This process is 100% secure and RBI compliant.")
                    },
                    modifier = Modifier.padding(top = Spacing.dp8)
                )
            }
        }

        NextButtonFooter(
            value = when (selectedOption) {
                is UpiPaymentOption.App -> "Pay ₹1 via ${selectedOption.app.label}/ " +
                        stringResource(Res.string.pay_one_rupee_via, selectedOption.app.label)
                UpiPaymentOption.ScanQr -> "Pay ₹1 by scanning QR/ " +
                        stringResource(Res.string.pay_one_rupee_by_qr)
                null -> "Pay ₹1/ " + stringResource(Res.string.pay_one_rupee)
            },
            onClick = onPayClick,
            enabled = selectedOption != null && appLinkToOpen == null && !awaitingAppReturn,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SwitchToForm(
    onClick: () -> Unit
){
    val shape = RoundedCornerShape(Spacing.dp16)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                onClick = onClick,
            )
            .background( White)
            .border(
                width =  Spacing.dp1,
                color =  BoxBorder,
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.dp14, vertical = Spacing.dp14),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.plus_icon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(Spacing.dp44)
                .padding(Spacing.dp8)
                .clip(RoundedCornerShape(Spacing.dp12))
                .border(Spacing.dp1, BoxBorder, RoundedCornerShape(Spacing.dp12))
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Enter Data Manually",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Black
            )
            Text(
                text = "Fill the details manually",
                style = MaterialTheme.typography.labelMedium,
                color = GreyText
            )
        }

    }
}

/**
 * Opens a UPI app link and reports the user's return from it.
 *
 * The link is only opened once this screen is resumed. It usually arrives as the payment page
 * closes, and that navigation resumes this screen by itself — opening the app before then would
 * let that resume pass for the user coming back. The return itself is a resume that follows a
 * pause; see [org.velvetinvesting.jantanivesh.app.core.deeplink.ExternalAppReturnLauncher].
 */
@Composable
private fun UpiAppLauncherEffect(
    appLinkToOpen: String?,
    awaitingAppReturn: Boolean,
    onAppLinkOpening: () -> Unit,
    onAppLinkNotOpened: () -> Unit,
    onReturnedFromApp: (wasStopped: Boolean) -> Unit
) {
    val returnLauncher = rememberExternalAppReturnLauncher()
    val lifecycleOwner = LocalLifecycleOwner.current
    // Outlives the effect below, which ends as soon as the link is marked as opening.
    val scope = rememberCoroutineScope()

    val currentOnAppLinkOpening by rememberUpdatedState(onAppLinkOpening)
    val currentOnAppLinkNotOpened by rememberUpdatedState(onAppLinkNotOpened)
    val currentOnReturnedFromApp by rememberUpdatedState(onReturnedFromApp)

    // Rebuilt while the user was away (the activity was recreated, say): the watch that would
    // have seen them come back went with the old screen, so arriving here is the return.
    LaunchedEffect(Unit) {
        if (awaitingAppReturn && !returnLauncher.isAwaitingReturn) {
            currentOnReturnedFromApp(true)
        }
    }

    LaunchedEffect(appLinkToOpen) {
        val url = appLinkToOpen ?: return@LaunchedEffect
        lifecycleOwner.lifecycle.currentStateFlow.first { it.isAtLeast(Lifecycle.State.RESUMED) }

        scope.launch {
            try {
                val opened = returnLauncher.launch(url) { wasStopped ->
                    currentOnReturnedFromApp(wasStopped)
                }
                if (!opened) {
                    currentOnAppLinkNotOpened()
                    SnackBarController.showError(ExternalAppUrl.appNotInstalledMessage(url))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                returnLauncher.reset()
                currentOnAppLinkNotOpened()
                SnackBarController.showError(ExternalAppUrl.appNotInstalledMessage(url))
            }
        }
        // Clears the link, which ends this effect — the launch above carries on in its own scope.
        currentOnAppLinkOpening()
    }
}

/** Shown after the payment page while the bank confirms the ₹1. */
@Composable
fun VerifyingPaymentContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.dp24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Primary)
        Text(
            text = "Confirming your ₹1 payment…",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Spacing.dp24)
        )
        Text(
            text = "Please stay on this screen — this can take a few seconds.",
            style = MaterialTheme.typography.bodyMedium,
            color = GreyText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Spacing.dp8)
        )
    }
}

@Composable
private fun UpiAppOptionCard(
    app: UpiApp,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(Spacing.dp16)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) HighlightRowBg.copy(alpha = 0.5f) else White)
            .border(
                width = if (selected) Spacing.dp2 else Spacing.dp1,
                color = if (selected) bgColor4 else BoxBorder,
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.dp14, vertical = Spacing.dp14),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UpiAppLogo(app = app)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = app.label,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Black
            )
            Text(
                text = app.subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = GreyText
            )
        }

        SelectionIndicator(selected = selected)
    }
}

/**
 * The generic payment page, which shows a QR code. For a UPI app not listed above, or paying
 * from another phone.
 */
@Composable
private fun ScanQrOptionCard(
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(Spacing.dp16)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) HighlightRowBg.copy(alpha = 0.5f) else White)
            .border(
                width = if (selected) Spacing.dp2 else Spacing.dp1,
                color = if (selected) bgColor4 else BoxBorder,
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.dp14, vertical = Spacing.dp14),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.img_upi_logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(Spacing.dp44)
                .clip(RoundedCornerShape(Spacing.dp12))
                .border(Spacing.dp1, BoxBorder, RoundedCornerShape(Spacing.dp12))
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "UPI",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Black
            )
            Text(
                text = "Pay from any UPI app",
                style = MaterialTheme.typography.labelMedium,
                color = GreyText
            )
        }

        SelectionIndicator(selected = selected)
    }
}

@Composable
private fun SelectionIndicator(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(Spacing.dp20)
            .clip(CircleShape)
            .background(if (selected) bgColor4 else Color.Transparent)
            .border(
                width = Spacing.dp1_2,
                color = if (selected) bgColor4 else GreyText,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                painter = painterResource(Res.drawable.tick_icon),
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(Spacing.dp12)
            )
        }
    }
}

/**
 * Placeholder tiles in each app's colours. Swap each branch for the app's official logo drawable
 * once it is added to composeResources.
 */
@Composable
private fun UpiAppLogo(app: UpiApp) {
    val shape = RoundedCornerShape(Spacing.dp12)
    val tile = Modifier
        .size(Spacing.dp44)
        .clip(shape)

    val icon = when (app) {
        UpiApp.GOOGLE_PAY -> Res.drawable.ic_gpay
        UpiApp.PHONEPE -> Res.drawable.ic_phonepay
        UpiApp.PAYTM -> Res.drawable.ic_paytm
        UpiApp.CRED -> Res.drawable.ic_cred
        UpiApp.BHIM -> Res.drawable.ic_bhim
    }

    Image(
        modifier = tile
            .border(Spacing.dp1, BoxBorder, shape),
        painter = painterResource(icon),
        contentDescription = null
    )
}

private val UpiApp.label: String
    get() = when (this) {
        UpiApp.GOOGLE_PAY -> "Google Pay"
        UpiApp.PHONEPE -> "PhonePe"
        UpiApp.PAYTM -> "Paytm UPI"
        UpiApp.CRED -> "CRED UPI"
        UpiApp.BHIM -> "BHIM"
    }

private val UpiApp.subtitle: String
    get() = when (this) {
        UpiApp.GOOGLE_PAY -> "Pay via GPay app"
        UpiApp.PHONEPE -> "Pay via PhonePe app"
        UpiApp.PAYTM -> "Pay via PayTm app"
        UpiApp.CRED -> "Pay via CRED app"
        UpiApp.BHIM -> "Pay via BHIM app"
    }

@Preview(showBackground = true, heightDp = 1000)
@Composable
private fun SelectUpiAppScreenPreview() {
    JantaNiveshTheme {
        SelectUpiAppScreen(
            apps = UpiApp.entries,
            canPayByQr = true,
            selectedOption = UpiPaymentOption.App(UpiApp.GOOGLE_PAY),
            notice = "Failed to verify ₹1 you can try again",
            canCheckStatus = true,
            appLinkToOpen = null,
            awaitingAppReturn = false,
            onOptionSelected = {},
            onPayClick = {},
            onCheckStatusClick = {},
            onBackClick = {},
            onAppLinkOpening = {},
            onAppLinkNotOpened = {},{},{}
        )
    }
}
