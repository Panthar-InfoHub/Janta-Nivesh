package org.velvetinvesting.jantanivesh.app.core.utils

import kotlin.math.abs
import kotlin.math.round


fun formatMoneyWithUnits(amount: Long?): String {
    if (amount == null || amount == 0L) return "0"

    val absAmount = abs(amount)

    val (value, suffix) = when {
        absAmount >= 10_000_000 -> amount / 10_000_000.0 to "Cr"
        absAmount >= 100_000 -> amount / 100_000.0 to "L"
        absAmount >= 1_000 -> amount / 1_000.0 to "K"
        else -> return formatWithCommas(amount)
    }

    val rounded = round(value * 100) / 100

    val text = rounded.toString()
        .removeSuffix(".0")
        .removeSuffix(".00")

    return "$text$suffix"
}


fun formatMoneyAfterL(amount: Long?): String {
    if (amount == null) return "0"

    val absAmount = abs(amount)

    if (absAmount <= 99_999) {
        return formatWithCommas(amount)
    }

    val (value, suffix) = when {
        absAmount >= 10_000_000 -> amount / 10_000_000.0 to "Cr"
        absAmount >= 100_000 -> amount / 100_000.0 to "L"
        else -> return formatWithCommas(amount)
    }

    val rounded = round(value * 100) / 100

    val text = rounded.toString()
        .removeSuffix(".0")
        .removeSuffix(".00")

    return "$text$suffix"
}


fun formatWithCommas(amount: Long): String {
    val isNegative = amount < 0
    val number = abs(amount).toString()

    if (number.length <= 3) return amount.toString()

    val lastThree = number.takeLast(3)
    val remaining = number.dropLast(3)

    val formattedRemaining = remaining
        .reversed()
        .chunked(2)
        .joinToString(",")
        .reversed()

    val result = "$formattedRemaining,$lastThree"

    return if (isNegative) "-$result" else result
}
