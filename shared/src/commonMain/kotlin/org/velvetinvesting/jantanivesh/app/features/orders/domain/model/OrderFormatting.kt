package org.velvetinvesting.jantanivesh.app.features.orders.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.roundToLong
import kotlin.time.Instant
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas

/**
 * Rupees, grouped Indian-style. The paise are dropped when there are none, because most orders
 * are placed in whole rupees and "₹5,000.00" reads as noise next to "₹5,000".
 */
fun Double.asMoney(): String {
    val whole = formatWithCommas(toLong())
    val paise = ((abs(this) - abs(toLong().toDouble())) * 100).roundToLong()
    return if (paise == 0L) whole else "$whole.${paise.toString().padStart(2, '0')}"
}

/** Units, to three places — the precision the gateway itself reports them at. */
fun Double.asUnits(): String = roundedTo(3)

/** NAV, to four places, which is how AMCs publish it. */
fun Double.asNav(): String = roundedTo(4)

private fun Double.roundedTo(places: Int): String {
    var factor = 1L
    repeat(places) { factor *= 10 }
    val scaled = (this * factor).roundToLong()
    val whole = scaled / factor
    val fraction = abs(scaled % factor)
    if (fraction == 0L) return whole.toString()
    val text = fraction.toString().padStart(places, '0').trimEnd('0')
    return "$whole.$text"
}

private val displayDate = LocalDate.Format {
    day(padding = Padding.ZERO)
    char(' ')
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    year()
}

/**
 * "2026-09-05T12:58:41.627Z" -> "05 Sep 2026". Blank rather than a placeholder when the stamp is
 * absent, so the caller decides what an unreached state looks like.
 */
fun String?.toOrderDate(): String {
    if (this.isNullOrBlank()) return ""
    return runCatching {
        Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault()).date.format(displayDate)
    }.recoverCatching {
        LocalDate.parse(this).format(displayDate)
    }.getOrDefault("")
}

/** "05 Sep 2026, 06:28 PM" — the timeline shows the time of day, the rest of the screen does not. */
fun String?.toOrderDateTime(): String {
    if (this.isNullOrBlank()) return ""
    return runCatching {
        val dateTime = Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())
        val hour12 = when (val h = dateTime.hour % 12) {
            0 -> 12
            else -> h
        }
        val meridiem = if (dateTime.hour < 12) "AM" else "PM"
        val minute = dateTime.minute.toString().padStart(2, '0')
        "${dateTime.date.format(displayDate)}, ${hour12.toString().padStart(2, '0')}:$minute $meridiem"
    }.getOrDefault(toOrderDate())
}
