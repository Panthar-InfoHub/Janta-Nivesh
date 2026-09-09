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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
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
import jantanivesh.shared.generated.resources.download_ic
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
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
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ContinueBackButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.MutualFundIcon
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ShadowCard
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.MFPortfolioDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.MFPortfolioSideEffects
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MFPortfolioDetailsScreen(
    onBackClick: () -> Unit,
    data: Route.SIPPortfolioDetails,
    /** Redeeming is its own screen now, reached with the figures this one already has. */
    onRedeemClick: () -> Unit,
) {

    val viewModel: MFPortfolioDetailsViewModel = koinViewModel()
    val screenState by viewModel.loadingState.collectAsStateWithLifecycle()
    val soaDownloading by viewModel.soaDownloading.collectAsStateWithLifecycle()
    val showCancelSheet by viewModel.showCancelSheet.collectAsStateWithLifecycle()
    val selectedCancelReason by viewModel.selectedCancelReason.collectAsStateWithLifecycle()
    val isSubmitting by viewModel.isSubmitting.collectAsStateWithLifecycle()
    val cancelSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect {
            when (it) {
                MFPortfolioSideEffects.OrderCancelled -> {
                    scope.launch {
                        AppEventsController.sendPortfolioRefreshEvent()
                        onBackClick()
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SidedBackHeader(
            heading = "Order details",
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
                        text = "Report",
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
                    ErrorScreen(
                        (screenState as LoadingState.Error).error,
                        onRetryClick = onBackClick
                    )
                }

                LoadingState.Loading -> {
                    LoaderScreen()
                }

                LoadingState.Success -> {
                    SIPDetailsLoadedScreen(
                        data = data,
                        onRedeem = onRedeemClick,
                        onCancelClick = viewModel::onShowCancelSheet
                    )
                }
            }
        }
    }

    if (showCancelSheet) {
        CancelSipReasonSheet(
            sheetState = cancelSheetState,
            selectedReason = selectedCancelReason,
            isSubmitting = isSubmitting,
            onReasonSelected = viewModel::onCancelReasonSelected,
            onConfirm = { viewModel.cancelSipPlan(data.holdingId) },
            onDismiss = viewModel::onDismissCancelSheet
        )
    }
}

/**
 * The order screen: who the holding is, then what it is worth.
 *
 * Both cards are rendered straight from the route — the portfolio screen that opens this already
 * holds every number, so there is nothing to fetch here.
 */
@Composable
fun SIPDetailsLoadedScreen(
    data: Route.SIPPortfolioDetails,
    onRedeem: () -> Unit,
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
            item { FundHeaderCard(data = data) }
            item { FundSummaryCard(data = data) }
            item { Spacer(Modifier.height(16.dp)) }
        }

        if (data.isSip){
            ContinueBackButtonFooter(
                continueText = "Redeem",
                backText = "Cancel",
                onContinue = onRedeem,
                onBack = onCancelClick
            )
        }
        else{
            NextButtonFooter(
                value = "Proceed to withdraw",
                onClick = onRedeem,
            )
        }
    }
}

/** Icon, scheme name, and the two badges that say what kind of order this is. */
@Composable
fun FundHeaderCard(
    data: Route.SIPPortfolioDetails,
) {
    ShadowCard(
        modifier = Modifier.border(
            width = 1.dp,
            color = Color.LightGray.copy(alpha = 0.5f),
            shape = LocalShapes.current.roundedDp15
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.size(44.dp)
                    .clip(LocalShapes.current.roundedDp12)
                    .background(Color.White),
                model = data.img_url,
                contentDescription = null,
                loading = { MutualFundIcon(schemeName = data.title, size = 44.dp) },
                error = { MutualFundIcon(schemeName = data.title, size = 44.dp) },
                success = { SubcomposeAsyncImageContent() }
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusChip(status = data.status)
                    Text(
                        text = if (data.isSip) "SIP" else "Lumpsum",
                        style = titlesStyle,
                        color = TextGray
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    if (status.isBlank()) return

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Secondary.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = status.uppercase(),
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Secondary
            )
        )
    }
}

/**
 * Current value up top, the six supporting figures in a two-column grid under it, and the
 * invested amount last — the one number that is not a derived one.
 */
@Composable
fun FundSummaryCard(
    data: Route.SIPPortfolioDetails,
) {
    // The portfolio reports a current value; older callers only carry invested + return.
    val currentValue = data.currentValue.takeIf { it != 0.0 } ?: (data.amount + data.returnAmount)
    val returnPercent = data.returnPercentage.toPercentOrNull()

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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Current Value",
                    style = TextStyle(
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = currentValue.asMoneyLabel().withInterRupee(),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Secondary
                        )
                    )
                    if (returnPercent != null) {
                        Text(
                            text = returnPercent.asPercentLabel(),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = returnPercent.signColor()
                            )
                        )
                    }
                }
            }

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                StatRow(
                    left = {
                        StatCell(
                            label = "Returns Amount",
                            value = data.returnAmount.asMoneyLabel(),
                            valueColor = data.dayReturn.signColor(),
                        )
                    },
                    right = {
                        StatCell(
                            label = "XIRR",
                            value = data.xirr.toPercentOrNull()?.asPercentLabel()
                                ?: NOT_AVAILABLE,
                            valueColor = data.xirr.toPercentOrNull()?.signColor() ?: Color.Black
                        )
                    }
                )
                StatRow(
                    left = { StatCell(label = "Current NAV", value = data.currentNav.toString()) },
                    right = { StatCell(label = "Avg NAV", value = data.avgNav.toString()) }
                )
                StatRow(
                    left = {
                        StatCell(label = "Balance Units", value = data.balanceUnits.toString())
                    },
                    right = {
                        StatCell(
                            label = "Folio no.",
                            value = data.actualFolio.ifBlank { data.folio }
                        )
                    }
                )
            }

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Invested Amount",
                    style = subHeadingMedium,
                    color = Color.Black
                )
                Text(
                    text = data.amount.asMoneyLabel().withInterRupee(),
                    style = subHeadingMedium,
                    color = Primary
                )
            }
        }
    }
}

@Composable
private fun StatRow(
    left: @Composable () -> Unit,
    right: @Composable () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.weight(1f)) { left() }
        Box(modifier = Modifier.weight(1f)) { right() }
    }
}

/** One figure in the grid, optionally with a smaller companion beside it (the day-return %). */
@Composable
private fun StatCell(
    label: String,
    value: String,
    valueColor: Color = Color.Black,
    trailing: String? = null,
    trailingColor: Color = Color.Black
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = value.withInterRupee(),
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = valueColor
                )
            )
            if (trailing != null) {
                Text(
                    text = trailing,
                    style = TextStyle(
                        fontFamily = InterFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = trailingColor
                    )
                )
            }
        }
    }
}

private const val NOT_AVAILABLE = "-"

/** Percentages reach this screen as strings, sometimes already carrying the sign or the %. */
private fun String.toPercentOrNull(): Double? =
    removeSuffix("%").trim().toDoubleOrNull()

private fun Double.asPercentLabel(): String = "${trimTo(2)}%"

/**
 * "₹6,010.15", sign kept inside so `withInterRupee` can move it in front of the symbol. Money on
 * this screen is shown to the paisa — a day return rounded to the rupee is usually just "₹0".
 */
private fun Double.asMoneyLabel(): String {
    val absolute = abs(this)

    var whole = absolute.toLong()
    var paise = ((absolute - whole) * 100).roundToInt()
    if (paise == 100) {
        whole += 1
        paise = 0
    }

    val sign = if (this < 0) "-" else ""
    return "₹$sign${formatWithCommas(whole)}.${paise.toString().padStart(2, '0')}"
}

private fun Double.signColor(): Color = if (this < 0) appRed else appGreen

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
                isSip = true,
                startDate = "28-Sep-2020",
                returnPercentage = "56.18%",
                returnAmount = -390424.44,
                xirr = "15.29%",
                currentNav = 82.693,
                avgNav = 52.947,
                folio = "CM_10534533",
                balanceUnits = 13125.567,
                orderId = "",
                actualFolio = "HUDHUW9877",
                currentValue = 6010.15,
                dayReturn = -40.05,
                dayReturnPercent = -4.09,
            ),
            onRedeem = {},
            onCancelClick = {}
        )
    }
}
