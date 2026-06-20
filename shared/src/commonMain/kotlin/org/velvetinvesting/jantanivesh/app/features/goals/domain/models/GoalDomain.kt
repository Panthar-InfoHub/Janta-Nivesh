package org.velvetinvesting.jantanivesh.app.features.goals.domain.models

data class GoalDomain(
    val childAge: Int?,
    val childName: String?,
    val createdAt: String,
    val currentAge: Int?,
    val currentGoalCost: Double?,
    val currentMonthlyExpense: String?,
    val currentSavedAmount: String,
    val goalId: Int,
    val goalItemId: Int?,
    val goalItemName: String?,
    val goalName: String?,
    val goalTypeId: Int,
    val id: String,
    val inflationRate: Double,
    val lifeExpectancy: Int?,
    val postRetirementReturn: String?,
    val retirementAge: Int?,
    val returnRate: Int,
    val updatedAt: String,
    val userId: String,
    val yearsLeft: Int?,
    val schemes: List<GoalSchemeDomain>
)

data class GoalSchemeDomain(
    val actualFolio: String,
    val balUnits: String,
    val currentVal: String,
    val folio: String,
    val nav: String,
    val schemeId: String,
    val schemeName: String
)
