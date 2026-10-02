package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.icon_arrow_right
import jantanivesh.shared.generated.resources.info_filled_icon
import jantanivesh.shared.generated.resources.profile_bank
import jantanivesh.shared.generated.resources.verify_your_upi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.GrayBackGround
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.PrimaryContainer
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow

/**
 * Explains the ₹1 reverse penny drop before the user commits to it. Shown when the server does not
 * hold the bank details yet, and again — with [notice] — when a verification attempt failed.
 */
@Composable
fun ReversePennyDropIntroScreen(
    isInitiating: Boolean,
    notice: String?,
    onVerifyClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        AppBackButton(
            onBackClick,
            modifier= Modifier.padding(horizontal = Spacing.dp20)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.dp20)
                .padding(top = Spacing.dp8),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                Text(
                    text = "Verify your bank account",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Black
                )
                Text(
                    text = "We'll verify your bank details through a secure UPI transaction to " +
                            "ensure accurate fund transfers",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray444
                )
            }

            PennyDropCard()

            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp10)) {
                VerificationStepRow(number = 1, text = "Open your UPI app when prompted")
                VerificationStepRow(
                    number = 2,
                    text = "Complete the ₹1 verification payment",
                    highlighted = true
                )
                VerificationStepRow(number = 3, text = "We'll automatically fetch your bank details")
            }

            notice?.let { NoticeBanner(message = it) }

            ImportantNoteCard()
        }

        AppButton(
            text = "Verify your UPI/ " + stringResource(Res.string.verify_your_upi),
            onClick = onVerifyClick,
            loading = isInitiating,
            trailingIcon = Res.drawable.icon_arrow_right,
            style = AppButtonDefaults.style(height = Spacing.dp58),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp24)
                .padding(bottom = Spacing.dp24)
                .genericDropShadow(RoundedCornerShape(Spacing.dp12))
        )
    }
}

@Composable
private fun PennyDropCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(Spacing.dp20))
            .clip(RoundedCornerShape(Spacing.dp20))
            .background(White)
            .padding(Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp14)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp36)
                    .clip(RoundedCornerShape(Spacing.dp10))
                    .background(PrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.profile_bank),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp20)
                )
            }
            Text(
                text = "Reverse Penny Drop",
                style = MaterialTheme.typography.titleLarge,
                color = Black
            )
        }
        Text(
            text = "Quick & secure bank verification using UPI",
            style = MaterialTheme.typography.bodyMedium,
            color = Black
        )
    }
}

/** One numbered step; [highlighted] marks the step the user actually acts on — the payment. */
@Composable
private fun VerificationStepRow(
    number: Int,
    text: String,
    highlighted: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .genericDropShadow(RoundedCornerShape(Spacing.dp12))
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
    ) {
        Box(
            modifier = Modifier
                .width(Spacing.dp4)
                .fillMaxHeight()
                .background(if (highlighted) Secondary else Color.Transparent)
        )
        Row(
            modifier = Modifier.padding(
                start = Spacing.dp8,
                end = Spacing.dp16,
                top = Spacing.dp14,
                bottom = Spacing.dp14
            ),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp28)
                    .clip(CircleShape)
                    .background(PrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Primary
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Black
            )
        }
    }
}

/** A problem the user needs to act on — a failed verification, a payment not yet confirmed. */
@Composable
internal fun NoticeBanner(
    message: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .border(color=appRed,shape=RoundedCornerShape(Spacing.dp12), width = Spacing.dp1)
            .padding(Spacing.dp14),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = appRed
        )
        action?.invoke()
    }
}

/** The refund reassurance both reverse penny drop screens end on. */
@Composable
internal fun ImportantNoteCard(
    body: AnnotatedString = AnnotatedString(
        "The ₹1 verification amount will be refunded to your account. This process " +
                "is 100% secure and RBI compliant."
    ),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(GrayBackGround.copy(alpha = 0.5f))
            .border(Spacing.dp1, BoxBorder, RoundedCornerShape(Spacing.dp16))
            .padding(Spacing.dp16),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp14)
    ) {
        Icon(
            painter = painterResource(Res.drawable.info_filled_icon),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(Spacing.dp20)
        )
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
            Text(
                text = "Important Note",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = Black
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun ReversePennyDropIntroScreenPreview() {
    JantaNiveshTheme {
        ReversePennyDropIntroScreen(
            isInitiating = false,
            notice = null,
            onVerifyClick = {},
            onBackClick = {}
        )
    }
}
