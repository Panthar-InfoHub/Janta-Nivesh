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
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan

/** `start_date` can arrive as a full timestamp; only the date part is shown. */
private const val ISO_DATE_LENGTH = 10

@Composable
fun PurchaseSuccessScreen(
    plan: PurchasePlan,
    schemeName: String,
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
                    text = "SIP Registered Successfully!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = Spacing.dp24)
                )
                Text(
                    text = "Your instalments will be debited automatically through UPI Autopay.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = GreyText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = Spacing.dp12)
                )

                SummaryCard(
                    plan = plan,
                    schemeName = schemeName,
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
    plan: PurchasePlan,
    schemeName: String,
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

        plan.amount.takeIf { it.isNotBlank() }?.let {
            HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
            SuccessRow(label = "Amount", value = "₹$it / month")
        }

        plan.installmentDay?.let {
            HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
            SuccessRow(label = "Debit day", value = "$it of every month")
        }

        plan.startDate?.takeIf { it.isNotBlank() }?.let {
            HorizontalDivider(thickness = Spacing.dp1, color = GreyBoxDivider)
            SuccessRow(label = "Starts on", value = it.take(ISO_DATE_LENGTH))
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

@Preview(showBackground = true)
@Composable
private fun PurchaseSuccessScreenPreview() {
    JantaNiveshTheme {
        PurchaseSuccessScreen(
            plan = PurchasePlan(
                id = "mfpp_f3014170e9bd4d0f90f0fcfdaf77fe4f",
                state = "confirmed",
                scheme = "INF209K01RU9",
                folioNumber = null,
                amount = "2500",
                frequency = "monthly",
                installmentDay = 10,
                numberOfInstallments = 12,
                remainingInstallments = 12,
                startDate = "2026-09-10"
            ),
            schemeName = "ADITYA BIRLA SUN LIFE LIQUID FUND - GROWTH",
            onViewHoldingsClick = {},
            onDoneClick = {}
        )
    }
}
