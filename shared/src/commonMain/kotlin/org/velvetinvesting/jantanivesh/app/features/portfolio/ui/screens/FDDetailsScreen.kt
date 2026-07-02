package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.back_arrow
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.presentation.portfolio.models.FDNomineeUiModel
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.FDPortFolioDetailsViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.buttonTextStyle
import org.velvetinvesting.jantanivesh.app.core.theme.subHeading
import org.velvetinvesting.jantanivesh.app.core.theme.subHeadingMedium
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ShadowCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FDStatus
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositTransactionDomain
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingAction

@Composable
fun FDPortfolioDetailsScreen(
    onBackClick: () -> Unit,
    id: String,
){

    val viewModel: FDPortFolioDetailsViewModel = koinViewModel{ parametersOf(id) }

    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()

    FDPortfolioDetailsScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRetry = viewModel::loadFDDetails,
        onClick = viewModel::onClick
    )

}

@Composable
fun FDPortfolioDetailsScreenContent(
    uiState: UiState<FixedDepositTransactionDomain>,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onClick: () -> Unit
) {
    UiStateContainer(
        uiState = uiState,
        onRetry = onRetry
    ) { data ->
        FDPortfolioDetailsMain(
            details = data,
            onBackClick = onBackClick,
            onClick = onClick,
        )
    }
}

@Composable
fun FDPortfolioDetailsMain(
    details: FixedDepositTransactionDomain,
    onBackClick: () -> Unit,
    onClick: () -> Unit,
) {
    val shouldShowButton = details.status !in listOf(
        FDStatus.REFUNDED,
        FDStatus.MATURED,
        FDStatus.PREMATURE_WITHDRAWN
    )
    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            stickyHeader {
                FDDetailsHeader(
                    onBackClick = onBackClick,
                    bankName = details.issuerDisplayName,
                    fdId = details.fdAccountNumber?:""
                )
            }
            item {
                InvestmentDetailsCard(details)
            }
//            item { BarHeader(heading = "Nominee Details") }
//
//            item {
//                OrderTimelineCard(details.startDate, details.maturityDate, details.daysRemaining)
//            }
            if (details.isVkycPending){
                item {
                    Text(
                        text="Your KYC is pending. Click on the button to complete the process.",
                        style = MaterialTheme.typography.labelSmall,
                        color = Primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            if (shouldShowButton) {

                item {
                    BreakFDButton(
                        onClick = {
                            onClick()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = if (details.isVkycPending)
                            "Complete KYC"
                        else if (details.status == FDStatus.FD_CREATED)
                            "Break FD"
                        else
                            "Pending Action"
                    )
                }

            } else {

                item {

                    val message = when (details.status) {

                        FDStatus.MATURED ->
                            "This fixed deposit has matured successfully. The maturity amount has been processed as per your selected payout instructions."

                        FDStatus.REFUNDED ->
                            "This fixed deposit request was refunded. Any processed payment has been credited back to the original source account."

                        FDStatus.PREMATURE_WITHDRAWN ->
                            "This fixed deposit has been withdrawn before maturity. Applicable interest adjustments and settlement have been completed."

                        else -> ""
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Primary.copy(alpha = 0.06f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Primary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = details.status.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = Primary
                        )

                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodySmall,
                            color = titleColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BreakFDButton( onClick: () -> Unit,modifier:Modifier=Modifier, text: String) {
    Button(
        onClick=onClick,
        modifier=modifier.fillMaxWidth().height(50.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE53935),
            contentColor = Color.White,
            disabledContentColor = Color.White ,
            disabledContainerColor = appRed.copy(alpha = 0.5f)
        )
    ){
        Text(
            text = text,
            style = buttonTextStyle
        )
    }
}

@Composable
fun OrderTimelineCard(startDate: String, maturityDate: String, remainingDays: String) {
    ShadowCard {
        Column(
            modifier=Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Text("Order Timeline", style = MaterialTheme.typography.labelSmall, color = Primary)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(appGreen))
                    Box(modifier = Modifier.width(1.dp).height(52.dp).background(titleColor))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "Start Date", style = subHeading, color = titleColor)
                    Text(text = startDate, style = titlesStyle.copy(fontWeight = FontWeight.Bold), color = Primary)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(appGreen))
                    Box(modifier = Modifier.width(1.dp).height(52.dp).background(titleColor))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "Maturity Date", style = subHeading, color = titleColor)
                    Text(text = maturityDate, style = titlesStyle.copy(fontWeight = FontWeight.Bold), color = Secondary)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(appGreen))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "Days Remaining", style = subHeading, color = titleColor)
                    Text(text = remainingDays, style = titlesStyle.copy(fontWeight = FontWeight.Bold), color = appGreen)
                }
            }
        }
    }
}

@Composable
fun NomineeDetailsCard(nominee: FDNomineeUiModel) {
    ShadowCard {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
               Text("Nominee Info", style = subHeadingMedium, color = Color.Black)
                Text("Edit", style = MaterialTheme.typography.labelSmall, color = Primary, modifier = Modifier.clickable(onClick={}))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Column(
                    modifier=Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text("Full Name", style = titlesStyle, color = titleColor)
                    Text(nominee.fullName, style = subHeading, color = Secondary)
                }
                Column(
                    modifier=Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text("Relationship", style = titlesStyle, color = titleColor)
                    Text(nominee.relationship, style =subHeading, color = Secondary)
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("DOB", style = titlesStyle, color = titleColor)
                Text(nominee.dateOfBirth, style =subHeading, color = Secondary)
            }
        }
    }
}

@Composable
fun InvestmentDetailsCard(details: FixedDepositTransactionDomain) {
    ShadowCard {
        Column(
            modifier=Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text("Investment Details", style = MaterialTheme.typography.labelSmall, color = Primary)

            InfoTextColumn(
                title = "Principal Amount",
                value = "₹${formatMoneyAfterL(details.amount.toLong())}".withInterRupee(),
                valueColor = Primary,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                InfoTextColumn(
                    title = "Interest Rate",
                    value = details.roiAtBooking+ "%",
                    valueColor = Secondary,
                    modifier = Modifier.weight(1f)
                )

                InfoTextColumn(
                    title = "Maturity Amount",
                    value = details.maturityAmount?.let { "₹${formatMoneyAfterL(it.toLong())}".withInterRupee() }?: "N/A",
                    valueColor = appGreen,
                    modifier = Modifier.weight(1f),
                )
            }

            InfoTextColumn(
                title = "Tenure",
                value = details.tenureAtBooking.toString() + " Days",
                valueColor = Primary,
                modifier = Modifier.fillMaxWidth()
            )
//
//            InfoTextColumn(
//                title = "Interest Earned Till Date",
//                value = details.inte,
//                valueColor = appGreen,
//                modifier = Modifier.fillMaxWidth()
//            )

        }
    }
}

@Composable
fun FDDetailsHeader(onBackClick: () -> Unit, bankName: String, fdId: String) {
    Box(modifier = Modifier.fillMaxWidth().background(Color.White)){
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier.padding(top = 8.dp).size(20.dp).clickable(
                    onClick = onBackClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            )
            Column(
                modifier = Modifier.weight(1f)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = bankName,
                    style = MaterialTheme.typography.titleLarge,
                    color = Primary,
                )
            }

        }
    }
}

@Composable
fun InfoTextColumn(
    title: String,
    value: Any,
    valueColor: Color,
    modifier: Modifier = Modifier,
    spacing: Dp = 4.dp
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Text(
            text = title,
            style = titlesStyle,
            color = titleColor
        )

        when (value) {
            is String -> {
                Text(
                    text = value,
                    style = subHeading,
                    color = valueColor
                )
            }
            is AnnotatedString -> {
                Text(
                    text = value,
                    style = subHeading,
                    color = valueColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FDPortfolioDetailsScreenPreview() {
    val sampleData = FixedDepositTransactionDomain(
        id = "1",
        userId = "user123",
        paymentCompletedAt = "2023-10-12",
        isVkycPending = false,
        amount = "100000",
        roiAtBooking = "7.5",
        tenureAtBooking = 365,
        payoutFrequency = "Monthly",
        status = FDStatus.FD_CREATED,
        maturityAmount = "107500",
        maturityDate = "2024-10-12",
        maturityInstruction = "Reinvest",
        paymentTxId = "tx123",
        fdAccountNumber = "FD123456",
        onboardedAt = "2023-10-12",
        vkycCompletedAt = "2023-10-12",
        fdIssuedAt = "2023-10-12",
        refundDate = null,
        vkycFailureReason = null,
        failureReason = null,
        createdAt = "2023-10-12",
        updatedAt = "2023-10-12",
        productId = "prod1",
        issuerId = "issuer1",
        issuerFullName = "HDFC Bank Limited",
        issuerDisplayName = "HDFC Bank",
        issuerType = "Private",
        issuerLogoUrl = "",
        issuerBannerUrl = "",
        issuerRatingText = "AAA",
        pendingAction = PendingAction.COMPLETED
    )

    JantaNiveshTheme {
        FDPortfolioDetailsScreenContent(
            uiState = UiState.Success(sampleData),
            onBackClick = {},
            onRetry = {},
            onClick = {}
        )
    }
}
