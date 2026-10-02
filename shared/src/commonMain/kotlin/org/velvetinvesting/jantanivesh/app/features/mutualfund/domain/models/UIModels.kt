package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models

import org.velvetinvesting.jantanivesh.app.features.cart.data.remote.model.cartaddsip.AddCartSipRequest

data class CategoryMutualFundDomain(
    val categoryName: String,
    val categorySearchReference: String,
    val mutualFunds: List<MutualFundDomain>
)



sealed interface DetailsState {
    data object Loading : DetailsState
    data class Success(val data: MutualFundDetailsDomain) : DetailsState
    data class Error(val error: String) : DetailsState
}

sealed interface GraphState {
    data object Loading : GraphState
    data class Success(val data: MutualFundGraphDomain) : GraphState
    data class Error(val error: String) : GraphState
}

data class MutualFundScreenState(
    val detailsState: DetailsState=DetailsState.Loading,
    val graphState: GraphState=GraphState.Loading,
    val chartPoints: List<MutualFundGraphPointsDomain> = emptyList()
)

data class StableMetricUi(
    val label: String,
    val value: Double
)

data class CalculatorInputState(
    val isSip: Boolean = true,
    val monthlyInvestment: Long = 5000,
    val timeInYears: Int = 5
)

data class CartBottomSheetState(
    val selectedType: MFPurchaseTypes= MFPurchaseTypes.LUMP_SUM,
    val amount:Long?=null,
    val minLumpSumAmount:Long = 500,
    val minSipAmount:Long = 500,
    val loading:Boolean=false,
    val selectedFrequency: InvestmentFrequency?=null,
    val selectedSIPDate:String?=null,
    val selectedDuration: Duration?=null,
    val frequencyDropDownExpanded:Boolean=false,
    val dayDropDownExpanded:Boolean=false,
    val durationDropDownExpanded:Boolean=false
)

enum class MFPurchaseTypes{
    LUMP_SUM,SIP
}

enum class Duration(
    val label: String,
    val months: Int // null = perpetual
) {
    PERPETUAL("Perpetual (Until Cancelled)", 0),

    SIX_MONTHS("6 Months", 6),
    ONE_YEAR("1 Year", 12),
    TWO_YEARS("2 Years", 24),
    THREE_YEARS("3 Years", 36),
    FIVE_YEARS("5 Years", 60),
    TEN_YEARS("10 Years", 120);
}


/**
 * The `POST /mf/cart` SIP body for the sheet's current input, or null while it is incomplete.
 * A daily SIP has no installment day; anything else is sent as a monthly SIP on the picked date.
 */
fun CartBottomSheetState.toSipRequest(productId: String): AddCartSipRequest? {
    val amount = amount ?: return null
    val isDaily = selectedFrequency == InvestmentFrequency.DAILY ||
            selectedFrequency == InvestmentFrequency.DAILY_Z

    if (isDaily) {
        return AddCartSipRequest(
            mf_product_id = productId,
            amount = amount,
            frequency = "DAILY"
        )
    }

    val day = selectedSIPDate?.toIntOrNull() ?: return null
    return AddCartSipRequest(
        mf_product_id = productId,
        amount = amount,
        frequency = "MONTHLY",
        installment_day = day
    )
}
