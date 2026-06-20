package org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.general_insurance
import jantanivesh.shared.generated.resources.health_insurance
import jantanivesh.shared.generated.resources.term_insurance
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InsurancePopularPlansCard

data class InsuranceUiState(
    val loading: Boolean = false,
    var termInsurancePlansList: List<TermInsurancePlan> = emptyList(),
    var healthInsurancePlansList: List<HealthInsurancePlan> = emptyList(),
    var generalInsurancePlansList: List<GeneralInsurancePlan> = emptyList(),
    val requestId: String = "INS-2026-0417"
)

data class TermInsurancePlan(
    val id:String,
    val icon:String,
    val title: String,
    val subString: String,
    val tag:String,
    val coverAmount:String,
    val premiumAmount: String
)
data class HealthInsurancePlan(
    val id:String,
    val icon:String,
    val title: String,
    val subString: String,
    val tag:String,
    val coverAmount:String,
    val premiumAmount: String
)
data class GeneralInsurancePlan(
    val id:String,
    val icon:String,
    val title: String,
    val subString: String,
    val coverAmount:String,
    val premiumAmount: String
)


sealed interface InsuranceEvent{
    data object OnRequestCallbackClick: InsuranceEvent
    data object OnTermClicked: InsuranceEvent
    data object OnHealthClicked: InsuranceEvent
    data object OnGeneralClicked: InsuranceEvent

    //Term Insurance Screen
    data object OnBackClickedTerm: InsuranceEvent
    data object OnRequestCallBackClickedTerm: InsuranceEvent
    data class OnProductClickedTerm(val id:String): InsuranceEvent
    //HealthInsurance Screen

    data object OnBackClickedHealth: InsuranceEvent
    data object OnRequestCallBackClickedHealth: InsuranceEvent
    data class OnProductClickedHealth(val id:String): InsuranceEvent

    //GeneralInsurance Screen

    data object OnBackClickedGeneral: InsuranceEvent
    data object OnRequestCallBackClickedGeneral: InsuranceEvent
    data class OnProductClickedGeneral(val id:String): InsuranceEvent
    //requestCallback Screen

    //CallBackSuccess
    data object BackToHomeClicked: InsuranceEvent
}

sealed interface InsuranceEffect{
    data object RequestCallback_Navigate_to_next: InsuranceEffect
    data object TermInsurance_Navigate_to_next: InsuranceEffect
    data object HealthInsurance_Navigate_to_next: InsuranceEffect
    data object GeneralInsurance_Navigate_to_Next: InsuranceEffect
    //Term Insurance Screen
    data object RequestCallbackTermNavigateToNext: InsuranceEffect
    data object BackTermNavigateToNext: InsuranceEffect
    data class ProductTermNavigateToNext(val id:String): InsuranceEffect
    //Health Insurance Screen
    data object RequestCallbackHealthNavigateToNext: InsuranceEffect
    data object BackHealthNavigateToNext: InsuranceEffect
    data class ProductHealthNavigateToNext(val id:String): InsuranceEffect
    data object RequestCallbackGeneralNavigateToNext: InsuranceEffect
    data object BackGeneralNavigateToNext: InsuranceEffect
    data class ProductGeneralNavigateToNext(val id:String): InsuranceEffect
    //request callback screen
        data object  SubmitClickedNavigateToNext: InsuranceEffect
    //CallBack Success Screen
    data object  BackToHomeNavigateToNext: InsuranceEffect

}

class InsuranceViewModel: ViewModel() {
    private val _uiState= MutableStateFlow(InsuranceUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<InsuranceEffect>()
    val effect = _effect.receiveAsFlow()

init {

    _uiState.update {
        it.copy(
            termInsurancePlansList = listOf(
                TermInsurancePlan(
                    id = "1",
                    icon = "",
                    title = "LIC Tech Term",
                    subString = "Life Insurance Corp.",
                    tag = "Govt. Backed",
                    coverAmount = "₹1 Cr",
                    premiumAmount = "₹8,400/yr"
                ),
                TermInsurancePlan(
                    id = "2",
                    icon = "",
                    title = "Click 2 Protect",
                    subString = "HDFC Life",
                    tag = "Popular",
                    coverAmount = "₹1 Cr",
                    premiumAmount = "₹9,200/yr"
            ),
                TermInsurancePlan(
                    id = "3",
                    icon = "",
                    title = "iProtect Smart",
                    subString = "ICICI Prudential",
                    tag = "Best Value",
                    coverAmount = "₹1 Cr",
                    premiumAmount = "₹8,150/yr"
                )
            ),
            healthInsurancePlansList = listOf(
                HealthInsurancePlan(
                    id = "1",
                    icon = "",
                    title = "Care Supreme",
                    subString = "Care Health",
                    tag = "Most Popular",
                    coverAmount = "₹10 L",
                    premiumAmount = "₹850/mo"
                ),
                HealthInsurancePlan(
                    id = "2",
                    icon = "",
                    title = "Optima Secure",
                    subString = "HDFC ERGO",
                    tag = "Cashless",
                    coverAmount = "₹10 L",
                    premiumAmount = "₹1,100/mo"
                ),
                HealthInsurancePlan(
                    id = "3",
                    icon = "",
                    title = "Star Comprehensive",
                    subString = "IStar Health",
                    tag = "Best Value",
                    coverAmount = "₹1 Cr",
                    premiumAmount = "₹920/mo"
                )
            )
        , generalInsurancePlansList = listOf(
                GeneralInsurancePlan(
                    id = "1",
                    icon = "",
                    title = "Car Insurance",
                    subString = "Reliance General",

                    coverAmount = "₹25,000",
                    premiumAmount = "₹45/mo"
                ),
                GeneralInsurancePlan(
                    id = "2",
                    icon = "",
                    title = "Bike Insurance",
                    subString = "HDFC ERGO",

                    coverAmount = "₹5,000",
                    premiumAmount = "₹12/mo"
                ),
                GeneralInsurancePlan(
                    id = "3",
                    icon = "",
                    title = "Home Insurance",
                    subString = "ICICI Lombard",

                    coverAmount = "₹250,000",
                    premiumAmount = "₹85/mo"
                )
            ))


    }
}
    fun handleEvent(event: InsuranceEvent){
        when(event){
            is InsuranceEvent.OnRequestCallbackClick->{
                sendEffect(
                InsuranceEffect.RequestCallback_Navigate_to_next
                )
            }

           is InsuranceEvent.OnTermClicked->{
               sendEffect(InsuranceEffect.TermInsurance_Navigate_to_next)
           }
            is InsuranceEvent.OnHealthClicked->{
                sendEffect(InsuranceEffect.HealthInsurance_Navigate_to_next)
            }
            is InsuranceEvent.OnGeneralClicked->{
                sendEffect(InsuranceEffect.GeneralInsurance_Navigate_to_Next)
            }
            //Term Screen
            is InsuranceEvent.OnRequestCallBackClickedTerm->{
                sendEffect(InsuranceEffect.RequestCallbackTermNavigateToNext)
            }
            is InsuranceEvent.OnBackClickedTerm -> {
                sendEffect(InsuranceEffect.BackTermNavigateToNext)

            }
            is InsuranceEvent.OnProductClickedTerm -> {
                sendEffect(InsuranceEffect.ProductTermNavigateToNext(event.id))
            }

            //Health Screen
            is InsuranceEvent.OnRequestCallBackClickedHealth->{
                sendEffect(InsuranceEffect.RequestCallbackHealthNavigateToNext)
            }
            is InsuranceEvent.OnBackClickedHealth -> {
                sendEffect(InsuranceEffect.BackHealthNavigateToNext)

            }
            is InsuranceEvent.OnProductClickedHealth -> {
                sendEffect(InsuranceEffect.ProductHealthNavigateToNext(event.id))
            }
            //General Screen
            is InsuranceEvent.OnRequestCallBackClickedGeneral->{
                sendEffect(InsuranceEffect.RequestCallbackGeneralNavigateToNext)
            }
            is InsuranceEvent.OnBackClickedGeneral -> {
                sendEffect(InsuranceEffect.BackGeneralNavigateToNext)

            }
            is InsuranceEvent.OnProductClickedGeneral -> {
                sendEffect(InsuranceEffect.ProductGeneralNavigateToNext(event.id))
            }
//Request Callback Screen

            is InsuranceEvent.BackToHomeClicked->{
                sendEffect(InsuranceEffect.BackToHomeNavigateToNext)
            }
        }
    }
    private fun sendEffect(effect: InsuranceEffect){
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
