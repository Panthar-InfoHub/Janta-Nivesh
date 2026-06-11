package org.velvetinvesting.jantanivesh.app.core.utils.math

import kotlin.math.roundToInt
import kotlin.math.roundToLong

fun Number.simpleInterestEarned(annualRate: Number, days: Int): Long {
    val principal = this.toDouble()
    val rate = annualRate.toDouble() / 100.0

    // Convert days into a fraction of a year
    val timeInYears = days.toDouble() / 365.0

    return (principal * rate * timeInYears).roundToLong()
}