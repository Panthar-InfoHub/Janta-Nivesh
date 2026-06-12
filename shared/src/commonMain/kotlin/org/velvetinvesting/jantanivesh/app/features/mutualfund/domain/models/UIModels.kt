package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Clock

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


fun CartBottomSheetState.toSipRequest(
    productId: String,
    folioId: String?
): AddCartSipRequest? {

    val amount = amount ?: return null
    val frequency = selectedFrequency ?: return null
    val duration = selectedDuration ?: return null
    val day = selectedSIPDate?.toIntOrNull() ?: return null

    if (amount < minSipAmount) return null

    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    val startDate = today.plus(DatePeriod(days = 31))

    val maxEndDate = startDate
        .plus(DatePeriod(years = 40))
        .minus(DatePeriod(days = 1))

    val endDate = if (duration == Duration.PERPETUAL) {
        maxEndDate
    } else {
        val calculated = startDate.plus(
            DatePeriod(months = duration.months)
        )
        if (calculated > maxEndDate) maxEndDate else calculated
    }

    return AddCartSipRequest(
        amount = amount,
        mf_product_id = productId,
        sip_st_date = startDate.toString(),
        sip_en_date = endDate.toString(),
        sip_freq = frequency.code,
        sip_day = day,
        sip_amt = amount,
        folio = folioId?:""
    )
}

@Serializable
data class AddCartSipRequest(
    val amount: Long,
    val mf_product_id: String,
    val sip_st_date: String,
    val sip_en_date: String,
    val sip_freq: String,
    val sip_day: Int,
    val sip_amt: Long,
    val folio:String = ""
)