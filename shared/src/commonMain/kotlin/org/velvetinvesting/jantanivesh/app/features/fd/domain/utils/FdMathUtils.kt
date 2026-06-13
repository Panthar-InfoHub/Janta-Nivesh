package org.velvetinvesting.jantanivesh.app.features.fd.domain.utils

import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.PayoutType
import org.velvetinvesting.jantanivesh.app.core.platform.Log
import kotlin.math.pow
import kotlin.math.round

fun Double.trimTo(precision: Int): String {
    if (precision < 0) return this.toString()

    val factor = 10.0.pow(precision)
    val rounded = round(this * factor) / factor

    return rounded
        .toString()
        .removeSuffix(".0")
        .removeSuffix(".00")
        .removeSuffix(".000")
}

fun Double.trimDoubleTo(precision: Int): Double {
    if (precision < 0) return this

    val factor = 10.0.pow(precision)
    val rounded = round(this * factor) / factor

    return rounded
}

fun calculateMaturity(
    principal: Long,
    rate: Double,
    days: Int,
    frequency: PayoutType
): Double {

    val years = days / 360.0

    return when (frequency) {

        PayoutType.Cumulative -> {
            val n = 4

            val base = 1 + (rate / 100) / n
            val exponent = n * years

            val maturity = principal * base.pow(exponent)

            Log("FD_CALC", "CUMULATIVE maturity=$maturity")

            maturity.trimDoubleTo(2)
        }

        PayoutType.Monthly,
        PayoutType.Quarterly,
        PayoutType.HalfYearly,
        PayoutType.Yearly,
        is PayoutType.Custom -> {

            val totalInterest = principal * (rate / 100) * years

            val maturity = principal + totalInterest

            Log("FD_CALC", "NON_CUMULATIVE maturity=$maturity")

            maturity.trimDoubleTo(2)
        }
    }
}
