package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.tick_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBoxDivider
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedTenureChipColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InvertedAppButton
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchaseMode

/** `start_date` and `scheduled_on` can arrive as full timestamps; only the date part is shown. */
private const val ISO_DATE_LENGTH = 10

/**
 * The end of every purchase, whichever way it was made.
 *
 * What actually happened differs by mode and the copy has to follow: a SIP is a *registration*
 * that will debit later, while a one-time buy is money that has already moved. Telling a user
 * their lumpsum "will be debited automatically through UPI Autopay" would be plainly wrong.
 */
@Composable
fun PurchaseSuccessScreen(
    mode: PurchaseMode,
    schemeName: String,
    amount: String,
    installmentDay: Int?,
    startDate: String?,
    onViewHoldingsClick: () -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(containerColor = White) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.dp24)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(Spacing.dp64)
                        .clip(CircleShape)
                        .background(SelectedTenureChipColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.tick_icon),
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(Spacing.dp32)
                    )
                }

                Text(
                    text = mode.successTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = Spacing.dp24)
                )
                Text(
                    text = mode.successSubtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = GreyText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = Spacing.dp12)
                )

                SummaryCard(
                    mode = mode,
                    schemeName = schemeName,
                    amount = amount,
                    installmentDay = installmentDay,
                    startDate = startDate,
                    modifier = Modifier.padding(top = Spacing.dp32)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
                modifier = Modifier.padding(bottom = Spacing.dp24)
            ) {
                AppButton(
                    text = "View my holdings",
                    onClick = onViewHoldingsClick,
                    modifier = Modifier.fillMaxWidth()
                )
                InvertedAppButton(
                    text = "Done",
                    onClick = onDoneClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(
    mode: PurchaseMode,
    schemeName: String,
    amount: String,
    installmentDay: Int?,
    startDate: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .border(
                width = Spacing.dp1,
                color = GreyBoxDivider,
                shape = RoundedCornerShape(Spacing.dp16)
            )
            .padding(horizontal = Spacing.dp20)
    ) {
        SuccessRow(label = "Scheme", value = schemeName)

        HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
        SuccessRow(label = "Transaction", value = mode.transactionLabel)

        amount.takeIf { it.isNotBlank() }?.let {
            HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
            SuccessRow(label = "Amount", value = "₹$it${mode.amountSuffix}")
        }

        // Only a monthly SIP has a debit day; the other two would show a meaningless number.
        installmentDay?.takeIf { mode.needsInstallmentDay }?.let {
            HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
            SuccessRow(label = "Debit day", value = "$it of every month")
        }

        startDate?.takeIf { it.isNotBlank() }?.let {
            HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
            SuccessRow(label = mode.dateLabel, value = it.take(ISO_DATE_LENGTH))
        }
    }
}

@Composable
private fun SuccessRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.dp16),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Gray444
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Black,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

private val PurchaseMode.successTitle: String
    get() = when (this) {
        PurchaseMode.DAILY -> "Daily SIP Started!"
        PurchaseMode.MONTHLY -> "SIP Registered Successfully!"
        PurchaseMode.ONE_TIME -> "Investment Successful!"
    }

private val PurchaseMode.successSubtitle: String
    get() = when (this) {
        PurchaseMode.DAILY ->
            "Your daily instalments will be debited automatically through UPI Autopay."

        PurchaseMode.MONTHLY ->
            "Your instalments will be debited automatically through UPI Autopay."

        // The money has already moved by the time a lumpsum reaches this screen, so the only
        // thing still outstanding is the unit allotment.
        PurchaseMode.ONE_TIME ->
            "Your payment has gone through. Units will be allotted at the applicable NAV."
    }

private val PurchaseMode.transactionLabel: String
    get() = when (this) {
        PurchaseMode.DAILY -> "Daily SIP"
        PurchaseMode.MONTHLY -> "Monthly SIP"
        PurchaseMode.ONE_TIME -> "One-time purchase"
    }

private val PurchaseMode.amountSuffix: String
    get() = when (this) {
        PurchaseMode.DAILY -> " / day"
        PurchaseMode.MONTHLY -> " / month"
        PurchaseMode.ONE_TIME -> ""
    }

/** A SIP starts and keeps going; a lumpsum is priced on one date and then done. */
private val PurchaseMode.dateLabel: String
    get() = if (isSip) "Starts on" else "Scheduled for"

@Preview(showBackground = true)
@Composable
private fun MonthlySipSuccessPreview() {
    JantaNiveshTheme {
        PurchaseSuccessScreen(
            mode = PurchaseMode.MONTHLY,
            schemeName = "ADITYA BIRLA SUN LIFE LIQUID FUND - GROWTH",
            amount = "2500",
            installmentDay = 10,
            startDate = "2026-09-10",
            onViewHoldingsClick = {},
            onDoneClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DailySipSuccessPreview() {
    JantaNiveshTheme {
        PurchaseSuccessScreen(
            mode = PurchaseMode.DAILY,
            schemeName = "ADITYA BIRLA SUN LIFE LIQUID FUND - GROWTH",
            amount = "100",
            installmentDay = null,
            startDate = "2026-08-26",
            onViewHoldingsClick = {},
            onDoneClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LumpsumSuccessPreview() {
    JantaNiveshTheme {
        PurchaseSuccessScreen(
            mode = PurchaseMode.ONE_TIME,
            schemeName = "ADITYA BIRLA SUN LIFE LIQUID FUND - GROWTH",
            amount = "10000",
            installmentDay = null,
            startDate = "2026-08-26T00:00:00.000Z",
            onViewHoldingsClick = {},
            onDoneClick = {}
        )
    }
}
