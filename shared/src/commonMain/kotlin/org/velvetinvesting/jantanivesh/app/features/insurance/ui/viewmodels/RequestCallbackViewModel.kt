package org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class RequestCallbackUiState(
var selectedInsuranceType :String ="Term Life",
var selectedCallTime :String ="Afternoon",
var fullName:String ="",
var mobileNumber:String="",
var email:String =""
,

val insuranceTypeList : List<String> = listOf(
    "Term Life",
    "Health",
    "Motor",
    "Home",
    "Not Sure"
),
val callTimeList:List<String> = listOf(
    "Morning",
    "Afternoon",
    "Evening"
)
)

sealed interface RequestCallbackEvent{
    data class OnInsuranceTypeSelected(val type:String): RequestCallbackEvent
    data class OnCallTimeSelected(val time:String): RequestCallbackEvent
    data class OnNameChanged(val name:String): RequestCallbackEvent
    data class OnMobileChanged(val number:String): RequestCallbackEvent
    data class OnEmailChanged(val email:String): RequestCallbackEvent
    data object OnSubmitClicked: RequestCallbackEvent
}

sealed interface RequestCallbackEffect{
    data object  SubmitClickedNavigateToNext: RequestCallbackEffect
}


 class RequestCallbackViewModel: ViewModel() {

     private val _uiState= MutableStateFlow(RequestCallbackUiState())
     val uiState = _uiState.asStateFlow()

     private val _effect = Channel<RequestCallbackEffect>()
     val effect = _effect.receiveAsFlow()
     fun handleEvent(event: RequestCallbackEvent) {
         when (event) {
             is RequestCallbackEvent.OnInsuranceTypeSelected -> {
                 _uiState.update {
                     it.copy(selectedInsuranceType = event.type)
                 }
             }

             is RequestCallbackEvent.OnCallTimeSelected -> {
                 _uiState.update {
                     it.copy(selectedCallTime = event.time)
                 }
             }

             is RequestCallbackEvent.OnNameChanged -> {
                 _uiState.update {
                     it.copy(fullName = event.name)
                 }
             }

             is RequestCallbackEvent.OnMobileChanged -> {
                 val validNumber = event.number.all {
                     it.isDigit()
                 }
                 val length = event.number.length

                 if (!validNumber) return
                 if (length > 10) return

                 _uiState.update {
                     it.copy(mobileNumber = event.number)
                 }
             }

             is RequestCallbackEvent.OnEmailChanged -> {
                 _uiState.update {
                     it.copy(email = event.email)
                 }
             }

             is RequestCallbackEvent.OnSubmitClicked -> {
                 sendEffect(RequestCallbackEffect.SubmitClickedNavigateToNext)
             }
         }

         }
         private fun sendEffect(effect:RequestCallbackEffect) {
             viewModelScope.launch {
                 _effect.send(effect)
             }
         }

     }
