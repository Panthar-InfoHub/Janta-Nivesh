package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

/** "13th", "1st", "22nd" — how a debit day is written on the day card and in the picker. */
fun Int.withOrdinalSuffix(): String {
    // 11th, 12th and 13th break the last-digit rule, so the teens are handled first.
    val suffix = when {
        this % 100 in 11..13 -> "th"
        this % 10 == 1 -> "st"
        this % 10 == 2 -> "nd"
        this % 10 == 3 -> "rd"
        else -> "th"
    }
    return "$this$suffix"
}

/** Indian grouping: 1,00,000 rather than 100,000 — the last three digits, then pairs. */
fun Int.withThousandSeparators(): String {
    val digits = toString()
    if (digits.length <= 3) return digits

    val head = digits.dropLast(3)
    val tail = digits.takeLast(3)

    val groupedHead = head.reversed()
        .chunked(2)
        .joinToString(",")
        .reversed()

    return "$groupedHead,$tail"
}

/** "13 Sept" — the short form the expected-NAV line uses. */
fun LocalDate.toShortDateLabel(): String = "$day ${month.shortLabel}"

private val Month.shortLabel: String
    get() = when (this) {
        Month.JANUARY -> "Jan"
        Month.FEBRUARY -> "Feb"
        Month.MARCH -> "Mar"
        Month.APRIL -> "Apr"
        Month.MAY -> "May"
        Month.JUNE -> "Jun"
        Month.JULY -> "Jul"
        Month.AUGUST -> "Aug"
        Month.SEPTEMBER -> "Sept"
        Month.OCTOBER -> "Oct"
        Month.NOVEMBER -> "Nov"
        Month.DECEMBER -> "Dec"
    }
