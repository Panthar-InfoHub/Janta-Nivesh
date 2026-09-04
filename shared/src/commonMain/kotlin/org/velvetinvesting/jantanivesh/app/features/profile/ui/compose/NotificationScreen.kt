package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.bell_icon
import jantanivesh.shared.generated.resources.down_stock
import jantanivesh.shared.generated.resources.ic_clock
import jantanivesh.shared.generated.resources.icon_warning
import jantanivesh.shared.generated.resources.push_notifications_rafiki_1
import jantanivesh.shared.generated.resources.up_stock
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.grayColor
import org.velvetinvesting.jantanivesh.app.core.theme.redColor
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.EmptyFundScreen
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.NotificationDomain
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.NotificationSubType
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.NotificationViewModel

@Composable
fun NotificationScreen(
    onBack: () -> Unit,
    viewModel: NotificationViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        BackHeader(
            title = "Notification centre",
            showBack = true,
            onBack = onBack,
            modifier = Modifier.padding(horizontal = Spacing.dp16)
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading && state.notifications.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null && state.notifications.isEmpty()) {
                Text(
                    text = state.error ?: "Something went wrong",
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    color = Color.Red
                )
            } else if (state.notifications.isEmpty()) {
                EmptyFundScreen(
                    onBrowseClick = onBack,
                    text = "You don't have any notifications yet",
                    buttonText = "Go Back"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.notifications) { notification ->
                        NotificationItem(notification)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationDomain) {
    val style = getNotificationStyle(notification.subType)

    Box(
        Modifier.fillMaxWidth().drawBehind {
            val radius = 20.dp.toPx()
            drawRoundRect(
                color = Color(0xffF0F0F0),
                topLeft = Offset(0f, 1.5.dp.toPx()),
                cornerRadius = CornerRadius(radius, y = radius),
                size = size
            )
        }.border(1.dp, color = Color(0xffF0F0F0), shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White, shape = RoundedCornerShape(20.dp)).padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = style.color.copy(alpha = 0.05f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(style.icon),
                        contentDescription = "Notification Icon",
                        tint = style.color,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        notification.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = InterFontFamily,
                        color = Secondary
                    )
                    Text(
                        text = notification.body,
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = grayColor
                    )
                }
            }
            Text(
                DateTimeUtils.getRelativeTime(notification.createdAt),
                fontSize = 12.sp,
                fontFamily = InterFontFamily,
                color = Color(0xff8D94A5)
            )
        }
    }
}

data class NotificationStyle(
    val icon: DrawableResource,
    val color: Color
)

@Composable
fun getNotificationStyle(subType: NotificationSubType): NotificationStyle {
    return when (subType) {
        NotificationSubType.ALERT -> NotificationStyle(Res.drawable.icon_warning, appRed)
        NotificationSubType.FUND_INC -> NotificationStyle(Res.drawable.up_stock, appGreen)
        NotificationSubType.FUND_DEC -> NotificationStyle(Res.drawable.down_stock, redColor)
        NotificationSubType.REMINDER -> NotificationStyle(Res.drawable.ic_clock, Color(0xffF97316)) // Orange
        NotificationSubType.NOTIFICATION -> NotificationStyle(Res.drawable.bell_icon, Color(0xff3B82F6)) // Blue
        NotificationSubType.UNKNOWN -> NotificationStyle(Res.drawable.bell_icon, Color.Gray)
    }
}

@Preview
@Composable
fun NotificationScreenPreview() {
    JantaNiveshTheme{
        val sampleNotifications = listOf(
            NotificationDomain(
                id = "1",
                title = "FD Maturity Alert",
                body = "Your Fixed Deposit of ₹2,00,000 is maturing in 7 days. Consider reinvesting for better returns.",
                subType = NotificationSubType.ALERT,
                createdAt = "2026-07-16T14:32:55.974Z",
                isRead = false
            ),
            NotificationDomain(
                id = "2",
                title = "MF NAV Update",
                body = "HDFC Flexi Cap Fund has reached your target NAV of ₹850. Time to review your investment.\nNAV: ₹850.50 (+2.4%)",
                subType = NotificationSubType.FUND_INC,
                createdAt = "2026-07-16T10:32:55.974Z",
                isRead = false
            ),
            NotificationDomain(
                id = "3",
                title = "SIP Reminder",
                body = "Your SIP installment of ₹5,000 for Axis Bluechip Fund is scheduled for tomorrow.",
                subType = NotificationSubType.REMINDER,
                createdAt = "2026-07-15T14:32:55.974Z",
                isRead = false
            ),
            NotificationDomain(
                id = "4",
                title = "Market Alert",
                body = "Your Mutual Fund portfolio has decreased by 3.2% today due to market volatility.\nPortfolio: ₹4,85,000",
                subType = NotificationSubType.FUND_DEC,
                createdAt = "2026-07-15T10:32:55.974Z",
                isRead = false
            ),
            NotificationDomain(
                id = "5",
                title = "Interest Rate Update",
                body = "FD interest rates for senior citizens have been revised to 7.25% p.a. for 5-year tenure.",
                subType = NotificationSubType.NOTIFICATION,
                createdAt = "2026-07-14T14:32:55.974Z",
                isRead = false
            )
        )

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            BackHeader(
                title = "Notification centre",
                showBack = true,
                onBack = {}
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleNotifications) { notification ->
                    NotificationItem(notification)
                }
            }
        }
    }
}

@Preview
@Composable
fun NotificationScreenEmptyPreview() {
    JantaNiveshTheme {
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            BackHeader(
                title = "Notification centre",
                showBack = true,
                onBack = {}
            )
            Box(modifier = Modifier.fillMaxSize()) {
                EmptyFundScreen(
                    onBrowseClick = {},
                    text = "You don't have any notifications yet",
                    buttonText = "Go Back"
                )
            }
        }
    }
}
