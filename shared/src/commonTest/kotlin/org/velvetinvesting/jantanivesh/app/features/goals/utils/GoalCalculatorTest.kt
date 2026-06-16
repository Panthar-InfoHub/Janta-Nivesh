package org.velvetinvesting.jantanivesh.app.features.goals.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GoalCalculatorTest {

    @Test
    fun testCalculateFutureValue() {
        val presentValue = 1000L
        val inflationRate = 6
        val years = 10
        val expectedFutureValue = 1790.84 // 1000 * (1.06)^10
        val result = GoalCalculator.calculateFutureValue(presentValue, inflationRate, years)
        assertEquals(expectedFutureValue, result, 0.1)
    }

    @Test
    fun testCalculateSip() {
        val futureValue = 1000000.0
        val annualReturnRate = 12
        val years = 10
        val result = GoalCalculator.calculateSip(futureValue, annualReturnRate, years)
        // SIP = (FV * r) / ((1 + r)^n - 1) * (1 + r)
        // r = (1 + 0.12)^(1/12) - 1 = 0.00948879
        // n = 120
        // Expected SIP is roughly 4300-4400
        assertTrue(result > 4000 && result < 5000)
    }

    @Test
    fun testCalculateRetirementCorpus() {
        val monthlyExpense = 50000.0
        val inflation = 0.06
        val returns = 0.08
        val yearsToRetire = 20
        val yearsPostRetire = 25
        
        val result = GoalCalculator.calculateRetirementCorpus(
            monthlyExpense, inflation, returns, yearsToRetire, yearsPostRetire
        )
        assertTrue(result > 0)
    }
}
