package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.insurance_shield_icon
import jantanivesh.shared.generated.resources.wallet_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.FdIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LightBlueBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileTitleColor
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.grayColor
import org.velvetinvesting.jantanivesh.app.core.theme.leafIconBackground
import org.velvetinvesting.jantanivesh.app.core.theme.redColor
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.EmptyFundScreen
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.ActiveMandate
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.MandateDisplayStatus
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ActiveMandatesEvent
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ActiveMandatesUiState

/** Pending sits between the two outcomes, so it gets the warm middle rather than a second red. */
private val PendingAmber = Color(0xffF97316)

@Composable
fun ActiveMandatesScreen(
    state: ActiveMandatesUiState,
    onEvent: (ActiveMandatesEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().background(White)) {
        ActiveMandatesHeader(
            count = state.mandates.size,
            onBack = { onEvent(ActiveMandatesEvent.OnBackClicked) }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading && state.mandates.isEmpty() ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                state.error != null && state.mandates.isEmpty() -> Text(
                    text = state.error,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    color = redColor,
                    modifier = Modifier.align(Alignment.Center).padding(Spacing.dp24)
                )

                state.mandates.isEmpty() -> EmptyFundScreen(
                    onBrowseClick = { onEvent(ActiveMandatesEvent.OnBackClicked) },
                    text = "You don't have any mandates yet",
                    buttonText = "Go Back"
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(Spacing.dp24),
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp32)
                ) {
                    items(state.mandates, key = { it.id }) { mandate ->
                        MandateCard(mandate)
                    }
                    item { MandateInfoCard() }
                }
            }
        }
    }
}

/**
 * The count sits in the header rather than above the list because it is the one fact about the
 * whole set; the list itself is all per-mandate detail.
 */
@Composable
private fun ActiveMandatesHeader(count: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(start = Spacing.dp24, end = Spacing.dp16, bottom = Spacing.dp8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppBackButton(onClick = onBack)
        Text(
            text = "Active Mandates",
            fontFamily = InterFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ProfileTitleColor,
            modifier = Modifier.weight(1f)
        )
        if (count > 0) {
            Text(
                text = if (count == 1) "$count Item" else "$count Items",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = White,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Secondary)
                    .padding(horizontal = Spacing.dp12, vertical = Spacing.dp4)
            )
        }
    }
}

@Composable
private fun MandateCard(mandate: ActiveMandate) {
    val statusColor = mandate.status.color()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            // The status tints the card itself, so the state is readable before the pill is read.
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(statusColor.copy(alpha = 0.10f), Color.Transparent),
                        center = Offset(size.width * 0.85f, 0f),
                        radius = size.width * 0.4f
                    )
                )
            }
            .padding(vertical = Spacing.dp8)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp40)
                    .clip(RoundedCornerShape(Spacing.dp14))
                    .background(FdIconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.wallet_icon),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp20)
                )
            }
            Spacer(Modifier.size(Spacing.dp12))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Provider",
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = titleColor
                )
                Text(
                    text = mandate.providerName,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProfileTitleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.size(Spacing.dp8))
            MandateStatusPill(mandate.status, statusColor)
        }

        Spacer(Modifier.height(Spacing.dp16))
        HorizontalDivider(thickness = 1.dp, color = statusColor.copy(alpha = 0.25f))
        Spacer(Modifier.height(Spacing.dp16))

        Row(modifier = Modifier.fillMaxWidth()) {
            MandateFact(
                label = "Amount",
                value = "₹${mandate.amount}".withInterRupee(),
                modifier = Modifier.weight(1f)
            )
            MandateFact(
                label = "Start Date",
                value = AnnotatedString(mandate.startDate),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MandateFact(label: String, value: AnnotatedString, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontFamily = InterFontFamily,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            color = titleColor
        )
        Text(
            text = value,
            fontFamily = InterFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = ProfileTitleColor
        )
    }
}

@Composable
private fun MandateStatusPill(status: MandateDisplayStatus, statusColor: Color) {
    Row(
        modifier = Modifier
            .padding(end = Spacing.dp8)
            .clip(CircleShape)
            .background(statusColor.copy(alpha = 0.10f))
            .padding(horizontal = Spacing.dp10, vertical = Spacing.dp2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp6)
    ) {
        Box(
            modifier = Modifier
                .size(Spacing.dp6)
                .clip(CircleShape)
                .background(statusColor)
        )
        Text(
            text = status.label(),
            fontFamily = InterFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = statusColor
        )
    }
}

/** Explains what a mandate is, for the many users meeting the word for the first time. */
@Composable
private fun MandateInfoCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(leafIconBackground)
            .border(
                width = Spacing.dp1,
                color = LightBlueBorder,
                shape = RoundedCornerShape(Spacing.dp16)
            )
            .padding(Spacing.dp20)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp10)
        ) {
            Icon(
                painter = painterResource(Res.drawable.insurance_shield_icon),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(Spacing.dp16)
            )
            Text(
                text = "Automate your peace of mind",
                fontFamily = InterFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }
        Spacer(Modifier.height(Spacing.dp12))
        Text(
            text = "Mandates allow trusted providers to automatically deduct payments for your " +
                "subscriptions or investments. You stay in control and can pause or cancel " +
                "them at any time to maintain your financial wellness.",
            fontFamily = InterFontFamily,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = grayColor
        )
    }
}

private fun MandateDisplayStatus.color(): Color = when (this) {
    MandateDisplayStatus.ACTIVE -> appGreen
    MandateDisplayStatus.PENDING -> PendingAmber
    MandateDisplayStatus.FAILED -> redColor
    MandateDisplayStatus.CANCELLED -> grayColor
    MandateDisplayStatus.UNKNOWN -> grayColor
}

private fun MandateDisplayStatus.label(): String = when (this) {
    MandateDisplayStatus.ACTIVE -> "ACTIVE"
    MandateDisplayStatus.PENDING -> "PENDING"
    MandateDisplayStatus.FAILED -> "FAILED"
    MandateDisplayStatus.CANCELLED -> "CANCELLED"
    MandateDisplayStatus.UNKNOWN -> "UNKNOWN"
}

@Preview
@Composable
fun ActiveMandatesScreenPreview() {
    JantaNiveshTheme {
        ActiveMandatesScreen(
            state = ActiveMandatesUiState(
                mandates = listOf(
                    ActiveMandate(
                        id = "1",
                        providerName = "CYBRILLAPOA",
                        amount = "1,00,000",
                        startDate = "Aug 07, 2026",
                        status = MandateDisplayStatus.PENDING
                    ),
                    ActiveMandate(
                        id = "2",
                        providerName = "CYBRILLAPOA",
                        amount = "50,000",
                        startDate = "Jul 12, 2026",
                        status = MandateDisplayStatus.ACTIVE
                    )
                )
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun ActiveMandatesEmptyPreview() {
    JantaNiveshTheme {
        ActiveMandatesScreen(state = ActiveMandatesUiState(), onEvent = {})
    }
}
