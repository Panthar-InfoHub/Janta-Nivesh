package org.velvetinvesting.jantanivesh.app.core.utils.math

import kotlin.math.roundToInt

fun Int.toYearsFormatKmp(): String {
    val years = this / 365.0

    // Round to exactly one decimal place
    val roundedYears = (years * 10).roundToInt() / 10.0

    // If the number is a whole integer (e.g., 2.0), cast it to Int to drop the .0
    return if (roundedYears % 1.0 == 0.0) {
        "${roundedYears.toInt()}Y"
    } else {
        "${roundedYears}Y"
    }
}
fun Int.toMonthsFormatKmp(): String {
    val months = this / 30.0
    val roundedMonths = (months * 10).roundToInt() / 10.0
    return if (roundedMonths >= 1) {
        "${roundedMonths.toInt()}"
    } else {
        "$roundedMonths"
    }
}