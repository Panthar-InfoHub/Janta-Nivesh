package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object FundTypeSelector {
    private val _fundType  = MutableStateFlow<SelectedFundType>(SelectedFundType.SIP)
    val fundType = _fundType.asStateFlow()

    fun updateFundTypeToSIP() {
        _fundType.value = SelectedFundType.SIP
    }

    fun updateFundTypeToLumpSum() {
        _fundType.value = SelectedFundType.LUMSUM
    }
}

enum class SelectedFundType{
    SIP,LUMSUM
}