package org.velvetinvesting.jantanivesh.app.features.core.domain.models

import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserFinance

data class UserDataDomain (
    val name:String,
    val email:String,
    val mobile: String,
    val goals: List<GoalsSummaryDomain>,
    val kycVerified: Boolean ,
    val tradingAccountVerified: Boolean,
    val dob: String,
    val userFinance: UserFinance?
)
