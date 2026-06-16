package org.velvetinvesting.jantanivesh.app.features.core.domain.models

import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain

data class UserDataDomain (
    val name:String,
    val email:String,
    val mobile: String,
    val goals: List<GoalsSummaryDomain>,
    val kycVerified: Boolean ,
    val tradingAccountVerified: Boolean
)