package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.info_filled_icon
import jantanivesh.shared.generated.resources.monument_icon
import jantanivesh.shared.generated.resources.verification_amount_kyc
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.lightBlue
import org.velvetinvesting.jantanivesh.app.core.theme.tagColor
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.VerifyBankAccountEvent
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.VerifyBankAccountUiState

@Composable
fun VerifyBankAccountScreen(
    state: VerifyBankAccountUiState,
    handleEvent: (VerifyBankAccountEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(Spacing.dp24)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        text = "Verify your bank account",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "We'll verify your bank details through a secure UPI transaction to ensure accurate fund transfers",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray444,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                PennyDropCard()
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    StepCard(
                        number = "1",
                        text = "Open your UPI app when prompted",
                    )
                    StepCard(
                        number = "2",
                        text = "Complete the ₹1 verification payment",
                    )
                    StepCard(
                        number = "3",
                        text = "We'll automatically fetch your bank details",
                    )
                }
            }

            item {
                ImportantNoteCard()
            }
        }

        AppButton(
            text = "Verify your UPI",
            onClick = { handleEvent(VerifyBankAccountEvent.OnVerifyUpiClick) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.dp24)
        )
    }
}

@Composable
private fun PennyDropCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .padding(Spacing.dp20),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp48)
                    .clip(RoundedCornerShape(Spacing.dp12))
                    .background(lightBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.monument_icon),
                    contentDescription = "Bank Icon",
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp24)
                )
            }
            Text(
                text = "Penny Drop",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Black
            )
        }
        Text(
            text = "Quick & secure bank verification using UPI",
            style = MaterialTheme.typography.bodyLarge,
            color = Black
        )
    }
}

@Composable
private fun StepCard(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (number == "2") {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(Spacing.dp4)
                    .background(Secondary)
            )
        }

        Row(
            modifier = Modifier.padding(
                start = if (number == "2") Spacing.dp16 else Spacing.dp16 + Spacing.dp4,
                end = Spacing.dp16,
                top = Spacing.dp16,
                bottom = Spacing.dp16
            ),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp32)
                    .clip(CircleShape)
                    .background(lightBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.titleMedium,
                    color = Primary
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Gray444
            )
        }
    }
}

@Composable
private fun ImportantNoteCard() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp24))
            .border(
                width = Spacing.dp1,
                color = FilterChipUnselected,
                RoundedCornerShape(Spacing.dp24)
            )
            .background(tagColor)
            .padding(Spacing.dp20),
    ) {
        Icon(
            painter = painterResource(Res.drawable.info_filled_icon),
            contentDescription = "Info Icon",
            tint = Primary,
            modifier = Modifier.size(Spacing.dp20)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            Text(
                text = "Important Note",
                style = MaterialTheme.typography.titleMedium,
                color = Primary
            )
            Text(
                text = "The ₹1 verification amount will be instantly refunded to your account. This process is 100% secure and RBI compliant./ " + stringResource(Res.string.verification_amount_kyc),
                style = MaterialTheme.typography.bodyLarge,
                color = Black,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifyBankAccountScreenPreview() {
    JantaNiveshTheme {
        VerifyBankAccountScreen(
            state = VerifyBankAccountUiState(),
            handleEvent = {}
        )
    }
}