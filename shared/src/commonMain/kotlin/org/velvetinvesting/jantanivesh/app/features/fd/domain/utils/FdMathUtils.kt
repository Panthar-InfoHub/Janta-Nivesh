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

    val years = days / 365.0

    val maturity = principal *
            (1 + rate / 100)
                .pow(years)

    Log(
        "FD_CALC",
        "principal=$principal rate=$rate years=$years maturity=$maturity"
    )

    return maturity.trimDoubleTo(2)
}
