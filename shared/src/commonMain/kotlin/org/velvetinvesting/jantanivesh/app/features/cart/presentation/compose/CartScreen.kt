package org.velvetinvesting.jantanivesh.app.features.cart.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.back_arrow
import jantanivesh.shared.generated.resources.nav_icon_full_screener
import jantanivesh.shared.generated.resources.no_fund_in_cart
import jantanivesh.shared.generated.resources.pay
import jantanivesh.shared.generated.resources.wallet_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.features.cart.presentation.viewmodel.CartEvent
import org.velvetinvesting.jantanivesh.app.features.cart.presentation.viewmodel.CartUiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.CartType
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.SipDetails
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.SipItemDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.UserCartDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.KYCPopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    state: CartUiState,
    onEvent: (CartEvent) -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        val cart = state.cart
        when {
            state.isLoading -> LoaderScreen()

            cart == null && state.error != null -> ErrorScreen(
                errorMessage = state.error,
                onRetryClick = { onEvent(CartEvent.Retry) }
            )

            cart == null -> LoaderScreen()

            else -> Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CartHeader(
                    onBack = { onEvent(CartEvent.OnBackClicked) }
                )

                MFTypeSelector(
                    selected = state.selectedCartType,
                    lumpsumSize = cart.lumpSumItems.size,
                    sipSize = cart.sipItems.size,
                    onSelect = { onEvent(CartEvent.OnCartTypeSelected(it)) }
                )

                HorizontalDivider(
                    thickness = 0.7.dp,
                    color = titleColor.copy(0.2f),
                    modifier = Modifier.padding(top = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    CartScreenContent(
                        data = cart,
                        selectedType = state.selectedCartType,
                        onRemoveClick = { onEvent(CartEvent.OnRemoveItemClicked(it)) },
                        onRefresh = { onEvent(CartEvent.Refresh) },
                        // Step-up is not offered for now.
                        // onStepUpEnabled = { onEvent(CartEvent.OnStepUpEnabled(it)) },
                        // onStepUpDisabled = { onEvent(CartEvent.OnStepUpDisabled(it)) },
                        // onStepUpAmountChange = { item, amount ->
                        //     onEvent(CartEvent.OnStepUpAmountChanged(item, amount))
                        // }
                    )
                }

                NextButtonFooter(
                    onClick = { onEvent(CartEvent.OnPayClicked) },
                    value = if (state.totalAmount == 0L) "No Fund Added to Cart/ " + stringResource(Res.string.no_fund_in_cart) else "Pay/ " + stringResource(Res.string.pay) + " ₹${
                        formatMoneyAfterL(
                            state.totalAmount
                        )
                    }",
                    enabled = state.isPurchaseEnabled,
                    loading = state.isProcessing,
                )
            }
        }

        if (state.showCutOffPopup) {
            CartCutOffPopup(
                visible = state.showCutOffPopup,
                onDismiss = { onEvent(CartEvent.OnCutOffPopupDismissed) },
                onPurchase = { onEvent(CartEvent.OnPurchaseConfirmed) }
            )
        }

        if (state.showKycPopup) {
            KYCPopup(
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onDismiss = { onEvent(CartEvent.OnKycPopupDismissed) },
                onClick = { onEvent(CartEvent.OnCompleteKycClicked) }
            )
        }
    }
}

@Composable
fun MFTypeSelector(selected: CartType, onSelect: (CartType) -> Unit, lumpsumSize: Int, sipSize: Int, ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        CartTypeChip(
            text = "Lumpsum ($lumpsumSize)",
            onClick = { onSelect(CartType.LUMPSUM) },
            selected = selected == CartType.LUMPSUM,
            icon = Res.drawable.wallet_icon
        )
        CartTypeChip(
            text = "SIP ($sipSize)",
            onClick = { onSelect(CartType.SIP) },
            selected = selected == CartType.SIP,
            icon = Res.drawable.nav_icon_full_screener
        )
    }
}

@Composable
fun CartScreenContent(
    onRemoveClick: (String) -> Unit,
    onRefresh: () -> Unit,
    selectedType: CartType,
    data: UserCartDomain,
    // Step-up is not offered for now.
    // onStepUpEnabled: (SipItemDomain) -> Unit,
    // onStepUpDisabled: (SipItemDomain) -> Unit,
    // onStepUpAmountChange: (SipItemDomain, String) -> Unit
) {
    when(selectedType){
        CartType.SIP -> {
            SIPScreenContent(
                items = data.sipItems,
                onRefresh=onRefresh,
                onRemoveClick=onRemoveClick,
                // onStepUpEnabled = onStepUpEnabled,
                // onStepUpDisabled = onStepUpDisabled,
                // onStepUpAmountChange = onStepUpAmountChange
            )
        }
        CartType.LUMPSUM -> {
            LumpSumScreenContent(
                items = data.lumpSumItems,
                onRefresh=onRefresh,
                onRemoveClick=onRemoveClick
            )
        }
    }
}

@Composable
fun CartHeader(onBack: () -> Unit) {
    Row(
        modifier=Modifier.fillMaxWidth()
            .padding(vertical = 16.dp, horizontal =  16.dp ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.back_arrow),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
                .clickable(
                    onClick = onBack,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        )

        Text(
            text = "Cart",
            color = Primary,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}


@Composable
fun CartTypeChip(text: String, onClick: () -> Unit, selected: Boolean, icon: DrawableResource) {
    val selectedColor= Primary
    val unselectedColor= Color(0xffF3F4F6)
    val selectedTextColor= Color.White
    val unselectedTextColor= Color(0xff6A7282)

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(if (selected) selectedColor else unselectedColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ){
            Icon(
                painter=painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (selected) selectedTextColor else unselectedTextColor
            )
            Text(
                text = text,
                style = titlesStyle.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp),
                color = if (selected) selectedTextColor else unselectedTextColor,
            )
        }
    }
}


@Preview
@Composable
fun CartScreenContentEmptyPreview() {
    JantaNiveshTheme {
        Box(modifier = Modifier.background(Color.White).fillMaxSize()) {
            CartScreenContent(
                onRemoveClick = {},
                onRefresh = {},
                selectedType = CartType.LUMPSUM,
                data = UserCartDomain(emptyList(), emptyList()),
            )
        }
    }
}


@Preview
@Composable
fun CartScreenContentPreview() {
    JantaNiveshTheme {
        Box(modifier = Modifier.background(Color.White)) {
            CartScreenContent(
                onRemoveClick = {},
                onRefresh = {},
                selectedType = CartType.SIP,
                data = UserCartDomain(
                    listOf(
                        SipItemDomain(
                            id = "1",
                            amcName = "SBI Mutual Fund",
                            productName = "SBI Bluechip Fund Direct Growth",
                            amount = 5000,
                            type = CartType.LUMPSUM,
                            date = "2023-10-27",
                            sipDetails = SipDetails(
                                startDate = "2023-11-01",
                                endDate = "2028-11-01",
                                frequency = "Monthly",
                                day = 5,
                                sipAmount = 2000
                            ),
                            imageUrl = "",
                            inv_id = "",
                            prodCode = "",
                            stepUpRequired = true,
                            minStepUpAmount = 500,
                            amcCode = "",
                            minStepUpPercent = 10.0,
                        ),
                        SipItemDomain(
                            id = "2",
                            amcName = "HDFC Mutual Fund",
                            productName = "HDFC Mid-Cap Opportunities Fund",
                            amount = 2000,
                            type = CartType.SIP,
                            date = "2023-10-27",
                            sipDetails = SipDetails(
                                startDate = "2023-11-01",
                                endDate = "2028-11-01",
                                frequency = "Monthly",
                                day = 5,
                                sipAmount = 2000
                            ),
                            imageUrl = "",
                            inv_id = "",
                            prodCode = "",
                            minStepUpAmount = 500,
                            amcCode = "",
                            minStepUpPercent = 10.0,
                        )
                    ), emptyList()
                ),
            )
        }
    }
}
