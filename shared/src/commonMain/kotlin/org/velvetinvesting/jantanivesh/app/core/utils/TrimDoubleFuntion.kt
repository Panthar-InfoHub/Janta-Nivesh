package org.velvetinvesting.jantanivesh.app.core.utils

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
