package org.velvetinvesting.jantanivesh.app.features.core.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.velvetinvesting.jantanivesh.app.core.platform.Log

object AppEventsController {

    private val _appEvent= MutableSharedFlow<AppEvent?>(replay = 1)
    val appEvent=_appEvent.asSharedFlow()


    suspend fun sendEvent(event: AppEvent){
        _appEvent.emit(event)
    }

    suspend fun sendHomeRefreshEvent(){
        _appEvent.emit(AppEvent.HomeEventRefresh)
        Log("Refresh Event", "Home Refresh Called")
    }

    suspend fun sendGoalRefreshEvent(){
        _appEvent.emit(AppEvent.GoalEventRefresh)
        Log("Refresh Event", "Goal Refresh Called")
    }

    suspend fun sendLoanRefreshEvent(){
        _appEvent.emit(AppEvent.LoanEventRefresh)
        Log("Refresh Event", "Loan Refresh Called")
    }

    suspend fun sendPortfolioRefreshEvent(){
        _appEvent.emit(AppEvent.PortfolioRefreshEvent)
        Log("Refresh Event", "Portfolio Refresh Called")
    }

    suspend fun sendFireRefreshEvent(){
        _appEvent.emit(AppEvent.FireRefreshEvent)
        Log("Refresh Event", "Fire Refresh Called")
    }


    suspend fun clear() {
        _appEvent.emit(null)
    }

    suspend fun logout(){
        _appEvent.emit(AppEvent.LogOut)
    }

}


sealed interface AppEvent{
    data object HomeEventRefresh: AppEvent
    data object GoalEventRefresh: AppEvent
    data object LoanEventRefresh: AppEvent
    data object FireRefreshEvent: AppEvent

    data object PortfolioRefreshEvent: AppEvent
    data object LogOut: AppEvent
}