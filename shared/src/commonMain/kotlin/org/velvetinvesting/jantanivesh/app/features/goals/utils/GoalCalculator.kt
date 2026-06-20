package org.velvetinvesting.jantanivesh.app.features.goals.utils

import kotlin.math.*

data class RetirementGoalResult(
    val monthlyExpense: Double,
    val retirementCorpus: Double,
    val retirementAge: Int,
    val sip: Double
)

object GoalCalculator {

    fun calculateFutureValue(
        presentValue: Long,
        inflationRate: Int,
        years: Int
    ): Double {
        val i = inflationRate / 100.0
        return presentValue * (1 + i).pow(years)
    }

    fun calculateSip(
        futureValue: Double,
        annualReturnRate: Int,
        years: Int
    ): Double {

        val months = years * 12
        if (months <= 0) return 0.0

        val r = (1 + annualReturnRate / 100.0).pow(1.0 / 12) - 1

        val fvFactor = (1 + r).pow(months) - 1
        if (fvFactor <= 0 || r <= 0) return 0.0

        return (futureValue * r) / (fvFactor * (1 + r))
    }


    fun calculateRetirementCorpus(
        currentMonthlyExpense: Double,
        inflationRate: Double,
        returnRate: Double,
        yearsToRetirement: Int,
        yearsPostRetirement: Int
    ): Double {

        if (yearsToRetirement <= 0 || yearsPostRetirement <= 0) return 0.0

        val annualExpenseNow = currentMonthlyExpense * 12

        val expenseAtRetirement =
            annualExpenseNow * (1 + inflationRate).pow(yearsToRetirement)

        val r = returnRate
        val g = inflationRate
        val n = yearsPostRetirement

        return if (abs(r - g) < 1e-9) {
            val pv = expenseAtRetirement * (n / (1 + r))
            pv * (1 + r)
        } else {
            val growthFactor = (1 + g) / (1 + r)

            val pv =
                expenseAtRetirement *
                        (1 - growthFactor.pow(n)) / (r - g)

            pv * (1 + r)
        }
    }

    fun calculateRetirementSip(
        retirementCorpus: Double,
        annualReturnRate: Double,
        yearsToRetirement: Int
    ): Double {

        val months = yearsToRetirement * 12
        if (months <= 0) return 0.0

        val r = (1 + annualReturnRate).pow(1.0 / 12) - 1

        val fvFactor = (1 + r).pow(months) - 1
        if (fvFactor <= 0 || r <= 0) return 0.0

        return (retirementCorpus * r) / (fvFactor * (1 + r))
    }


    fun calculateRetirementFromInputs(
        currentAge: Int,
        retirementAge: Int,
        lifeExpectancy: Int,
        monthlyExpense: Double,
        inflationRate: Double,
        postRetirementReturn: Double,
        preRetirementReturn: Double
    ): RetirementGoalResult {

        val yearsToRetirement = retirementAge - currentAge
        val yearsPostRetirement = lifeExpectancy - retirementAge

        if (yearsToRetirement <= 0 || yearsPostRetirement <= 0) {
            return RetirementGoalResult(0.0, 0.0, retirementAge, 0.0)
        }

        val corpus = calculateRetirementCorpus(
            currentMonthlyExpense = monthlyExpense,
            inflationRate = inflationRate,
            returnRate = postRetirementReturn,
            yearsToRetirement = yearsToRetirement,
            yearsPostRetirement = yearsPostRetirement
        )

        val sip = calculateRetirementSip(
            retirementCorpus = corpus,
            annualReturnRate = preRetirementReturn,
            yearsToRetirement = yearsToRetirement
        )

        return RetirementGoalResult(
            monthlyExpense = monthlyExpense,
            retirementCorpus = corpus,
            retirementAge = retirementAge,
            sip = sip
        )
    }
}
