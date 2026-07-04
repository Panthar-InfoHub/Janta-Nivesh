package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.arrow_down
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.subHeadingMedium
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CartBottomSheetState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.Duration
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.InvestmentFrequency
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MFPurchaseTypes
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.Metrics
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.FundTypeSelector
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.SelectedFundType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartPopup(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onAmountChange: (String) -> Unit,
    onTypeChange: (MFPurchaseTypes) -> Unit,
    detailState: MutualFundDetailsDomain,
    cartState: CartBottomSheetState,
    onAddClick: () -> Unit,
    showFrequencyDropDown: () -> Unit,
    showDateDropDown: () -> Unit,
    showDurationDropDown: () -> Unit,
) {
    val fundType by FundTypeSelector.fundType.collectAsStateWithLifecycle()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        CartPopupContent(
            fundType = fundType,
            detailState = detailState,
            cartState = cartState,
            onAmountChange = onAmountChange,
            onAddClick = onAddClick,
            showFrequencyDropDown = showFrequencyDropDown,
            showDateDropDown = showDateDropDown,
            showDurationDropDown = showDurationDropDown
        )
    }
}

@Composable
fun CartPopupContent(
    fundType: SelectedFundType,
    detailState: MutualFundDetailsDomain,
    cartState: CartBottomSheetState,
    onAmountChange: (String) -> Unit,
    onAddClick: () -> Unit,
    showFrequencyDropDown: () -> Unit,
    showDateDropDown: () -> Unit,
    showDurationDropDown: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .imePadding()
            .padding(vertical = 20.dp, horizontal = 16.dp)
            .clearFocusOnTap(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add to Cart",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                FundBadge(
                    text = when (fundType) {
                        SelectedFundType.SIP -> "SIP"
                        SelectedFundType.LUMSUM -> "Lump Sum"
                    }
                )
            }

            Text(
                text = detailState.scheme_name,
                style = titlesStyle,
                color = Color.Black,
            )
        }

        when (fundType) {
            SelectedFundType.LUMSUM -> LumpSumCart(
                amount = cartState.amount,
                onAmountChange = onAmountChange,
                onChipClick = { onAmountChange(it.toString()) },
                minAmount = cartState.minLumpSumAmount,
                onAddClick = onAddClick,
                loading = cartState.loading,
            )

            SelectedFundType.SIP -> SIPCart(
                amount = cartState.amount,
                onAmountChange = onAmountChange,
                onChipClick = { onAmountChange(it.toString()) },
                minAmount = cartState.minSipAmount,
                onAddClick = onAddClick,
                loading = cartState.loading,
                frequency = cartState.selectedFrequency?.label,
                duration = cartState.selectedDuration,
                date = cartState.selectedSIPDate,
                showFrequencyDropDown = showFrequencyDropDown,
                showDateDropDown = showDateDropDown,
                showDurationDropDown = showDurationDropDown
            )
        }
    }
}

@Composable
fun SIPCart(
    onChipClick: (Long) -> Unit,
    onAmountChange: (String) -> Unit,
    amount: Long?,
    minAmount: Long,
    onAddClick: () -> Unit,
    loading: Boolean,
    frequency: String?,
    duration: Duration?,
    date: String?,
    showFrequencyDropDown: () -> Unit,
    showDateDropDown: () -> Unit,
    showDurationDropDown: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val chips = generateInvestmentChips(
        minAmount = minAmount,
        isSip = true
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            ShadowlessTextField(
                value = amount?.toString() ?: "",
                onValueChange = onAmountChange,
                placeHolder = "Enter amount (min. ₹${minAmount})",
                label = "Investment Amount",
                mandatory = false,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (amount != null && amount < minAmount) {
                    Text(
                        text = "Amount less than min ₹$minAmount".withInterRupee(),
                        color = appRed,
                        style = titlesStyle.copy(fontSize = 12.sp),
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = "Min ₹$minAmount".withInterRupee(),
                    style = titlesStyle.copy(fontSize = 14.sp),
                    color = titleColor
                )
            }
        }

        AmountChipsGrid(
            amounts = chips,
            onChipClick = { onChipClick(it) },
            currentAmount=amount
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
//            DropDownField(
//                text = frequency ?: "",
//                placeholder = "Select Frequency",
//                onClick = showFrequencyDropDown,
//                label = "Frequency",
//                modifier = Modifier.weight(1f)
//            )
            DropDownField(
                text = date ?: "",
                placeholder = "Select Date",
                onClick = {
                    keyboardController?.hide()
                    showDateDropDown()
                },
                label = "SIP Day",
                modifier = Modifier.weight(1f)
            )
        }


//        DropDownField(
//            text = duration?.label ?: "",
//            placeholder = "Select Duration",
//            onClick = showDurationDropDown,
//            label = "Duration",
//            modifier = Modifier.fillMaxWidth()
//        )

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Start SIP " + ((amount?.let {"of ₹" +formatMoneyAfterL(it)+"/month" })?:""),
            onClick = {
                onAddClick()
            },
            loading = loading,
            enabled = amount != null && amount >= minAmount
        )
    }
}

@Composable
fun LumpSumCart(
    onChipClick: (Long) -> Unit,
    onAmountChange: (String) -> Unit,
    amount: Long?,
    minAmount: Long,
    onAddClick: () -> Unit,
    loading: Boolean,

    ) {
    val chips = generateInvestmentChips(
        minAmount = minAmount,
        isSip = false
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            ShadowlessTextField(
                value = amount?.toString() ?: "",
                onValueChange = onAmountChange,
                placeHolder = "Enter amount (min. ₹${minAmount})",
                label = "Investment Amount",
                mandatory = false,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (amount != null && amount < minAmount) {
                    Text(
                        text = "Amount less than min ₹$minAmount".withInterRupee(),
                        color = appRed,
                        style = titlesStyle.copy(fontSize = 12.sp),
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = "Min ₹$minAmount".withInterRupee(),
                    style = titlesStyle.copy(fontSize = 14.sp),
                    color = titleColor
                )
            }
        }

        AmountChipsGrid(
            amounts = chips,
            onChipClick = { onChipClick(it) },
            currentAmount=amount
        )

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Add " + ((amount?.let {"of ₹" +formatMoneyAfterL(it) })?:"")+" to Cart" ,
            onClick = {
                onAddClick()
            },
            loading = loading,
            enabled = amount != null && amount >= minAmount
        )
    }
}

@Composable
fun AmountChipsGrid(
    amounts: List<Long>,
    onChipClick: (Long) -> Unit,
    currentAmount: Long?,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        amounts.forEach { amount ->
            ChipItem(
                text = ("₹" + formatMoneyAfterL(amount)),
                onClick = { onChipClick(amount) },
                selected = amount == currentAmount
            )
        }
    }
}

@Composable
fun ChipItem(text: String, onClick: () -> Unit, selected: Boolean) {
    Box(
        modifier = Modifier
            .widthIn(min=52.dp)
            .clip(CircleShape)
            .background(if (selected) Secondary else Color.White)
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = if (selected) Secondary else titleColor.copy(0.2f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text.withInterRupee(),
            style = titlesStyle.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp),
            color = if (selected) Color.White else Primary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun ShadowlessTextField(
    value:String,
    onValueChange:(String)->Unit,
    placeHolder:String,
    label:String?=null,
    mandatory: Boolean=false,
    modifier: Modifier = Modifier,
){

    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        label?.let{
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = label,
                    style = subHeadingMedium,
                    color = Color.Black
                )
                if (mandatory) {
                    Text(
                        text = "*",
                        color = Color.Red,
                        style = subHeadingMedium
                    )
                }
            }
        }

        BasicTextField(
            value = value,
            onValueChange = {it-> onValueChange(it) },
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier.fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White, RoundedCornerShape(15.dp))
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = Secondary
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                horizontalArrangement  = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹",
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier=Modifier.padding(start = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(end = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeHolder.withInterRupee(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xffC5C5C5)
                        )
                    }
                    it()
                }
            }
        }
    }
}

@Composable
fun DropDownField(
    text: String,
    placeholder: String,
    onClick: () -> Unit,
    label: String? = null,
    mandatory: Boolean = false,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        label?.let {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = label,
                    style = subHeadingMedium,
                    color = Color.Black
                )
                if (mandatory) {
                    Text(
                        text = "*",
                        color = Color.Red,
                        style = subHeadingMedium
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .border(
                    width = 0.7.dp,
                    shape = RoundedCornerShape(15.dp),
                    color = Secondary
                )
                .clickable { onClick() }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = text.ifEmpty { placeholder },
                    style = if (text.isEmpty())
                        MaterialTheme.typography.bodySmall.copy(color = Color(0xffC5C5C5))
                    else
                        TextStyle(
                            fontFamily = InterFontFamily,
                            fontSize = 16.sp,
                            color = Color.Black,
                        ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    painter = painterResource(Res.drawable.arrow_down),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = titleColor
                )
            }
        }
    }
}

@Composable
fun FundBadge(text: String){
    Box(
        modifier = Modifier.clip(CircleShape).background(Primary)
    ){
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFontFamily,
            modifier = Modifier.padding(vertical = 2.dp, horizontal = 12.dp)
        )
    }
}

fun generateInvestmentChips(
    minAmount: Long,
    isSip: Boolean
): List<Long> {

    val baseAmount = if (minAmount > 0) minAmount else 500L

    val multipliers = if (isSip) {
        listOf(1L, 2L, 5L, 10L, 20L)
    } else {
        listOf(1L, 2L, 5L, 10L)
    }

    return multipliers
        .map { baseAmount * it }
        .distinct()
}

private val sampleMetrics = Metrics(
    nav_change_pct = 1.2,
    return_1y = 15.5,
    return_30d = 2.1,
    return_3y = 12.0,
    return_6m = 5.0,
    return_90d = 3.5,
    return_5y = 10.5
)

private val sampleMutualFundDetails = MutualFundDetailsDomain(
    amc_code = "AMC001",
    amc_id = "1",
    amc_name = "Sample AMC",
    asset_type = "Equity",
    createdAt = "2023-01-01T00:00:00Z",
    id = "fund123",
    isin = "INE123A01011",
    latest_nav = "150.25",
    latest_nav_date = "2023-10-27T00:00:00Z",
    mapping_code = "M123",
    maturity_date = null,
    metrics = sampleMetrics,
    nfo_end_date = null,
    nse_scheme_code = "S123",
    platform_code = "P123",
    purchase_allowed = true,
    redemption_allowed = true,
    risk_level = 4,
    risk_name = "Moderately High",
    scheme_id = "SCH123",
    scheme_name = "Sample Bluechip Fund",
    scheme_type = "Open Ended",
    sip_allowed = true,
    structure = "Growth",
    switch_allowed = true,
    sipAllowedDated = listOf(1, 5, 10, 15, 20, 25),
    investmentFrequency = listOf(InvestmentFrequency.MONTHLY, InvestmentFrequency.QUARTERLY),
    updatedAt = "2023-10-27T00:00:00Z",
    icon = "",
    minAmount = 5000,
    minSipAmount = 500,
    minLumpSumAmount = 5000
)

@Preview(showBackground = true)
@Composable
fun CartPopupSIPPreview() {
    JantaNiveshTheme {
        CartPopupContent(
            fundType = SelectedFundType.SIP,
            detailState = sampleMutualFundDetails,
            cartState = CartBottomSheetState(
                amount = 1000,
                selectedSIPDate = "10"
            ),
            onAmountChange = {},
            onAddClick = {},
            showFrequencyDropDown = {},
            showDateDropDown = {},
            showDurationDropDown = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartPopupLumpSumPreview() {
    JantaNiveshTheme {
        CartPopupContent(
            fundType = SelectedFundType.LUMSUM,
            detailState = sampleMutualFundDetails,
            cartState = CartBottomSheetState(
                amount = 5000
            ),
            onAmountChange = {},
            onAddClick = {},
            showFrequencyDropDown = {},
            showDateDropDown = {},
            showDurationDropDown = {}
        )
    }
}
