package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.document_kyc_icon
import jantanivesh.shared.generated.resources.download_ic
import jantanivesh.shared.generated.resources.ic_graph
import jantanivesh.shared.generated.resources.ic_jagged_arrow
import jantanivesh.shared.generated.resources.ic_menu
import jantanivesh.shared.generated.resources.ic_percent
import jantanivesh.shared.generated.resources.icon_callender
import jantanivesh.shared.generated.resources.icon_clock
import jantanivesh.shared.generated.resources.icon_mf
import jantanivesh.shared.generated.resources.info_icon
import jantanivesh.shared.generated.resources.rupeesign
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.MFPortfolioDetailsViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.MFPortfolioSideEffects
import org.velvetinvesting.jantanivesh.app.core.navigation.Route
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.TextGray
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.subHeadingMedium
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ContinueBackButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.MutualFundIcon
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ShadowCard
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.core.utils.rememberBrowserReturnLauncher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MFPortfolioDetailsScreen(
    onBackClick: () -> Unit,
    data: Route.SIPPortfolioDetails,
) {

    val viewModel: MFPortfolioDetailsViewModel = koinViewModel()
    val screenState by viewModel.loadingState.collectAsStateWithLifecycle()
    val showRedemptionSheet by viewModel.showRedemptionSheet.collectAsStateWithLifecycle()
    val selectedRedemptionType by viewModel.selectedRedemptionType.collectAsStateWithLifecycle()
    val selectedInputType by viewModel.selectedInputType.collectAsStateWithLifecycle()
    val redemptionUnits by viewModel.redemptionUnits.collectAsStateWithLifecycle()
    val redemptionAmount by viewModel.redemptionAmount.collectAsStateWithLifecycle()
    val isSubmitting by viewModel.isSubmitting.collectAsStateWithLifecycle()
    val soaDownloading by viewModel.soaDownloading.collectAsStateWithLifecycle()
    val browserLauncher = rememberBrowserReturnLauncher()
    var showCancelDialog by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect {
            when (it) {
                is MFPortfolioSideEffects.openRedeemptionUrl -> {
                    browserLauncher.launch(it.url) {
                        scope.launch {
                            AppEventsController.sendPortfolioRefreshEvent()
                            onBackClick()
                        }
                    }
                }

                MFPortfolioSideEffects.OrderCancelled -> {
                    scope.launch {
                        AppEventsController.sendPortfolioRefreshEvent()
                        onBackClick()
                    }
                }
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SidedBackHeader(
            heading = "Withdraw Fund",
            showBack = true,
            onBackClick = onBackClick,
            trailingContent = {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Secondary.copy(alpha = 0.1f))
                        .clickable { viewModel.downloadSOA(data.folio) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "SOA",
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = Secondary
                        )
                    )
                    if (soaDownloading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(12.dp),
                            strokeWidth = 1.dp,
                            color = Secondary
                        )
                    } else {
                        Icon(
                            painter = painterResource(Res.drawable.download_ic),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Secondary
                        )
                    }
                }
            }
        )

        Box(modifier = Modifier.weight(1f)) {
            when (screenState) {
                is LoadingState.Error -> {
                    ErrorScreen((screenState as LoadingState.Error).error, onRetryClick = {
                        viewModel.submitRedemption(
                            schemeId = data.id,
                            folioNo = data.folio
                        )
                    })
                }

                LoadingState.Loading -> {
                    LoaderScreen()
                }

                LoadingState.Success -> {
                    SIPDetailsLoadedScreen(
                        data = data,
                        onWithdrawClick = { viewModel.onShowRedemptionSheet() },
                        onCancelClick= {
                            showCancelDialog=true
                        }
                    )
                }
            }
        }
    }

    if (showRedemptionSheet) {
        RedemptionBottomSheet(
            sheetState = sheetState,
            onDismiss = { viewModel.onDismissRedemptionSheet() },
            schemeId = data.id,
            folioNo = data.folio,
            selectedRedemptionType = selectedRedemptionType,
            selectedInputType = selectedInputType,
            redemptionUnits = redemptionUnits,
            redemptionAmount = redemptionAmount,
            maxUnits = data.balanceUnits,
            maxAmount = data.amount,
            onRedemptionTypeChange = viewModel::onRedemptionTypeChange,
            onInputTypeChange = viewModel::onInputTypeChange,
            onUnitsChange = viewModel::onUnitsChange,
            onAmountChange = viewModel::onAmountChange,
            onSubmit = {
                viewModel.submitRedemption(
                    schemeId = data.id,
                    folioNo = data.folio
                )
            },
            loading = isSubmitting
        )
    }

    if (showCancelDialog) {
        CancelConfirmationDialog(
            onDismiss = {
                showCancelDialog = false
            },
            onConfirm = {
                viewModel.cancelSipOrder(data.orderId)
                showCancelDialog = false

            }
        )
    }
}

@Composable
fun SIPDetailsLoadedScreen(
    data: Route.SIPPortfolioDetails,
    onWithdrawClick: () -> Unit,
    folioIcon: DrawableResource = Res.drawable.document_kyc_icon,
    calendarIcon: DrawableResource = Res.drawable.icon_callender,
    returnPercentIcon: DrawableResource = Res.drawable.icon_mf,
    returnAmountIcon: DrawableResource = Res.drawable.rupeesign,
    xirrIcon: DrawableResource = Res.drawable.ic_percent,
    currentNavIcon: DrawableResource = Res.drawable.ic_jagged_arrow,
    avgNavIcon: DrawableResource = Res.drawable.ic_graph,
    balanceUnitsIcon: DrawableResource = Res.drawable.icon_clock,
    investmentTypeIcon: DrawableResource = Res.drawable.ic_menu,
    infoIcon: DrawableResource = Res.drawable.info_icon,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { FundSummaryCard(data = data) }

            item {
                Text(
                    text = "Fund details",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                ShadowCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        FundDetailItem(folioIcon, "Folio", data.folio)
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(calendarIcon, "Start date", data.startDate)
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(returnPercentIcon, "Return %", data.returnPercentage, valueColor = if (data.returnPercentage.contains("-")) appRed else appGreen)
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(returnAmountIcon, "Return amount", "₹${formatWithCommas(data.returnAmount.toLong())}", valueColor = if (data.returnAmount>0) appGreen else appRed)
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(xirrIcon, "XIRR", data.xirr)
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(currentNavIcon, "Current NAV", "₹${data.currentNav}")
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(avgNavIcon, "Average NAV", "₹${data.avgNav}")
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(balanceUnitsIcon, "Balance units", data.balanceUnits.toString())
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(investmentTypeIcon, "Investment type", if (data.isSip) "SIP" else "Lumpsum")
                    }
                }
            }

            item {
                BottomNotice(infoIcon)
            }

            item { Spacer(Modifier.height(16.dp)) }
        }

        if (data.isSip){
            ContinueBackButtonFooter(
                continueText = "Withdraw",
                backText = "Cancel",
                onContinue = onWithdrawClick,
                onBack = onCancelClick
            )
        }
        else{
            NextButtonFooter(
                value = "Proceed to withdraw",
                onClick = onWithdrawClick,
            )
        }
    }
}

@Composable
fun FundSummaryCard(
    data: Route.SIPPortfolioDetails,
) {
    ShadowCard(
        modifier = Modifier.border(
            width = 1.dp,
            color = Color.LightGray.copy(alpha = 0.5f),
            shape = LocalShapes.current.roundedDp15
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(48.dp)
                        .clip(LocalShapes.current.roundedDp12)
                        .background(Color.White),
                    model = data.img_url,
                    contentDescription = null,

                    loading = {
                        MutualFundIcon(
                            schemeName = data.title, size = 48.dp
                        )
                    },

                    error = {
                        MutualFundIcon(
                            schemeName = data.title, size = 48.dp
                        )
                    },

                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black
                    )
                    Text(
                        text = data.category,
                        style = titlesStyle,
                        color = TextGray
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp),
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            Row(modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier) {
                    Text(
                        text = "Invested amount",
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    )
                    Text(
                        text = "₹${formatWithCommas(data.amount.toLong())}".withInterRupee(),
                        style = subHeadingMedium,
                        color = Primary
                    )
                }
                Column(modifier= Modifier) {
                    Text(
                        text = "Current value",
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    )
                    Text(
                        text = "₹${formatWithCommas((data.amount + data.returnAmount).toLong())}".withInterRupee(),
                        style = subHeadingMedium,
                        color = Secondary
                    )
                }
            }
        }
    }
}

@Composable
fun FundDetailItem(
    icon: DrawableResource,
    label: String,
    value: String,
    valueColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Primary.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Primary
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value.withInterRupee(),
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = valueColor
            )
        )

    }
}

@Composable
fun CancelConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                text = "Cancel Withdrawal?",
                style = MaterialTheme.typography.headlineSmall
            )
        },

        text = {
            Text(
                text = "Are you sure you want to cancel? Any unsaved progress will be lost.",
                style = MaterialTheme.typography.bodyMedium
            )
        },

        dismissButton = {
            Text(
                text = "No",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onDismiss)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = Primary,
                fontWeight = FontWeight.Medium
            )
        },

        confirmButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(appRed)
                    .clickable(onClick = onConfirm)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Yes, Cancel",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}

@Composable
fun BottomNotice(icon: DrawableResource) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Primary.copy(alpha = 0.05f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Primary
        )
        Text(
            text = "All redemptions are processed as per AMC timelines.",
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black.copy(alpha = 0.7f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SIPDetailsLoadedScreenPreview() {
    JantaNiveshTheme {
        SIPDetailsLoadedScreen(
            data = Route.SIPPortfolioDetails(
                id = 1,
                title = "Parag Parikh Flexi Cap Fund - Reg - Gr",
                category = "MF-Flexi-cap Fund",
                amount = 694965.34,
                isSip = false,
                startDate = "28-Sep-2020",
                returnPercentage = "56.18%",
                returnAmount = -390424,
                xirr = "15.29%",
                currentNav = 82.693,
                avgNav = 52.947,
                folio = "CM_10534533",
                balanceUnits = 13125.567,
                orderId = ""
            ),
            {}
        ) {}
    }
}
