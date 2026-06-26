package org.velvetinvesting.jantanivesh.app.core.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


object DateTimeUtils {

    /**
     * Get the current year as an integer
     * @return Current year (e.g., 2026)
     */
    fun getCurrentYear(): Int {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    }

    fun getYear(epochMillis: Long):Int{
        return Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.currentSystemDefault()).year
    }
    /**
     * Get current time in epoch milliseconds
     * @return Current time as epoch millis
     */
    fun getCurrentEpochMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    /**
     * Get current time in epoch seconds
     * @return Current time as epoch seconds
     */
    fun getCurrentEpochSeconds(): Long {
        return Clock.System.now().epochSeconds
    }

    /**
     * Convert epoch milliseconds to LocalDateTime
     * @param epochMillis The epoch milliseconds to convert
     * @param timeZone The timezone (defaults to system default)
     * @return LocalDateTime representation
     */
    fun epochMillisToLocalDateTime(
        epochMillis: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): LocalDateTime {
        return Instant.fromEpochMilliseconds(epochMillis)
            .toLocalDateTime(timeZone)
    }

    /**
     * Convert LocalDateTime to epoch milliseconds
     * @param dateTime The LocalDateTime to convert
     * @param timeZone The timezone (defaults to system default)
     * @return Epoch milliseconds
     */
    fun localDateTimeToEpochMillis(
        dateTime: LocalDateTime,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Long {
        return dateTime.toInstant(timeZone).toEpochMilliseconds()
    }

    /**
     * Get the start of today in epoch milliseconds
     * @param timeZone The timezone (defaults to system default)
     * @return Epoch millis for start of today (00:00:00)
     */
    fun getStartOfTodayEpochMillis(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        val startOfDay = today.atStartOfDayIn(timeZone)
        return startOfDay.toEpochMilliseconds()
    }

    /**
     * Get the end of today in epoch milliseconds
     * @param timeZone The timezone (defaults to system default)
     * @return Epoch millis for end of today (23:59:59.999)
     */
    fun getEndOfTodayEpochMillis(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        val endOfDay = today.plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)
            .minus(1, DateTimeUnit.MILLISECOND)
        return endOfDay.toEpochMilliseconds()
    }

    /**
     * Add days to an epoch millis timestamp
     * @param epochMillis The base timestamp
     * @param days Number of days to add (can be negative)
     * @return New epoch millis after adding days
     */
    fun addDaysToEpochMillis(epochMillis: Long, days: Int): Long {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        return instant.plus(days.days).toEpochMilliseconds()
    }

    /**
     * Add hours to an epoch millis timestamp
     * @param epochMillis The base timestamp
     * @param hours Number of hours to add (can be negative)
     * @return New epoch millis after adding hours
     */
    fun addHoursToEpochMillis(epochMillis: Long, hours: Int): Long {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        return instant.plus(hours.hours).toEpochMilliseconds()
    }

    /**
     * Calculate difference between two epoch millis timestamps in days
     * @param startEpochMillis The earlier timestamp
     * @param endEpochMillis The later timestamp
     * @return Number of days between timestamps
     */
    fun getDaysBetween(startEpochMillis: Long, endEpochMillis: Long): Long {
        val start = Instant.fromEpochMilliseconds(startEpochMillis)
        val end = Instant.fromEpochMilliseconds(endEpochMillis)
        val duration = end - start
        return duration.inWholeDays
    }

    /**
     * Calculate difference between two epoch millis timestamps in hours
     * @param startEpochMillis The earlier timestamp
     * @param endEpochMillis The later timestamp
     * @return Number of hours between timestamps
     */
    fun getHoursBetween(startEpochMillis: Long, endEpochMillis: Long): Long {
        val start = Instant.fromEpochMilliseconds(startEpochMillis)
        val end = Instant.fromEpochMilliseconds(endEpochMillis)
        val duration = end - start
        return duration.inWholeHours
    }

    /**
     * Get year from epoch millis
     * @param epochMillis The timestamp
     * @param timeZone The timezone (defaults to system default)
     * @return Year as integer
     */
    fun getYearFromEpochMillis(
        epochMillis: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Int {
        return epochMillisToLocalDateTime(epochMillis, timeZone).year
    }

    /**
     * Check if a given epoch millis is today
     * @param epochMillis The timestamp to check
     * @param timeZone The timezone (defaults to system default)
     * @return True if the timestamp is today
     */
    fun isToday(epochMillis: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        val date = epochMillisToLocalDateTime(epochMillis, timeZone).date
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        return date == today
    }

    /**
     * Get current LocalDateTime
     * @param timeZone The timezone (defaults to system default)
     * @return Current LocalDateTime
     */
    fun now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
        return Clock.System.now().toLocalDateTime(timeZone)
    }

    /**
     * Get current LocalDate
     * @param timeZone The timezone (defaults to system default)
     * @return Current LocalDate
     */
    fun today(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
        return Clock.System.now().toLocalDateTime(timeZone).date
    }

    fun epochMillisToSlashDate(epochMillis: Long?): String {
        if (epochMillis==null) return ""
        val instant = Instant.fromEpochMilliseconds(epochMillis)

        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val day = dateTime.date.day.toString().padStart(2, '0')
        val month = dateTime.date.month.number.toString().padStart(2, '0')
        val year = dateTime.date.year

        return "$day/$month/$year"
    }
    fun epochMillisToSlashDateReversed(epochMillis: Long?): String {
        if (epochMillis==null) return ""
        val instant = Instant.fromEpochMilliseconds(epochMillis)

        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val day = dateTime.date.day.toString().padStart(2, '0')
        val month = dateTime.date.month.number.toString().padStart(2, '0')
        val year = dateTime.date.year

        return "$year/$month/$day"
    }

    fun slashDateToIsoUtc(date: String?): String {
        if (date.isNullOrBlank()) return ""

        return try {
            val parts = date.split("/")

            if (parts.size != 3) return ""

            val day = parts[0].toInt()
            val month = parts[1].toInt()
            val year = parts[2].toInt()

            val localDate = LocalDate(year = year,
                month = month,
                day = day
            )

            localDate
                .atStartOfDayIn(TimeZone.UTC)
                .toString()

        } catch (_: Exception) {
            ""
        }
    }

    fun slashDateToEpochMillis(date: String): Long? {
        return try {
            val parts = date.split("/")

            if (parts.size != 3) return null

            val day = parts[0].toInt()
            val month = parts[1].toInt()
            val year = parts[2].toInt()

            val localDate = LocalDate(year = year,
                month = month,
                day = day
            )

            localDate
                .atStartOfDayIn(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()

        } catch (_: Exception) {
            null
        }
    }

    fun epochToYYYYMMdd(epochMillis: Long?): String {
        if (epochMillis == null) return ""
        val instant = Instant.fromEpochMilliseconds(epochMillis)

        val localDate = instant
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date

        return localDate.toString()
    }

    fun epochMillisToIsoUtc(epochMillis: Long?): String? {
        if (epochMillis == null) return null
        return Instant
            .fromEpochMilliseconds(epochMillis)
            .toString()
    }

    fun isoUtcToSlashDate(isoUtc: String?): String {
        if (isoUtc.isNullOrBlank()) return ""

        return try {
            val instant = Instant.parse(isoUtc)

            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

            val day = dateTime.date.day.toString().padStart(2, '0')
            val month = dateTime.date.month.number.toString().padStart(2, '0')
            val year = dateTime.date.year

            "$day/$month/$year"
        } catch (_: Exception) {
            ""
        }
    }
    fun dobToEpochMillis(dob: String): Long {
        return Instant.parse(dob).toEpochMilliseconds()
    }

    fun formatDate(input: String): String {
        try{
            val normalized = input
                .lowercase()
                .replaceFirstChar { it.uppercase() }
                .replace(Regex("""\s([a-z]{3})\s""")) {
                    " ${it.groupValues[1].replaceFirstChar { c -> c.uppercase() }} "
                }

            val parser = LocalDate.Format {
                this@Format.day(padding = Padding.ZERO)
                char(' ')
                monthName(MonthNames.ENGLISH_ABBREVIATED)
                char(' ')
                year()
            }
            val formatter = LocalDate.Format {
                day(padding = Padding.ZERO)
                char('/')
                monthNumber(Padding.ZERO)
                char('/')
                year()
            }
            return parser.parse(normalized).format(formatter)

        }catch (e: Exception){
            return input
        }
    }
}


fun String.isoUtcToDisplayDate(): String {
    return try {
        val instant = Instant.parse(this)
        val localDate = instant.toLocalDateTime(TimeZone.UTC).date

        val day = localDate.day.toString().padStart(2, '0')
        val month = localDate.month.name
            .lowercase()
            .replaceFirstChar { it.uppercase() }
            .take(3)
        val year = localDate.year

        "$day $month $year"

    } catch (e: Exception) {
        this
    }
}

fun formatDateParts(date: LocalDate): Pair<String, String> {
    val year = date.year.toString()
    val dateFormatter = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED); char(','); char(' ')
        day(padding = Padding.ZERO)
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
    }

    val formattedDate = date.format(dateFormatter)
    return year to formattedDate
}

@OptIn(ExperimentalTime::class)
fun formatMillisDateParts(millis: Long?, timeZone: TimeZone = TimeZone.currentSystemDefault()): Pair<String, String>? {
    if (millis == null) return null

    val instant = Instant.fromEpochMilliseconds(millis)
    val localDate = instant.toLocalDateTime(timeZone).date

    val year = localDate.year.toString()
    val dateFormatter = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED); char(','); char(' ')
        day(padding = Padding.ZERO)
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
    }

    val formattedDate = localDate.format(dateFormatter)
    return year to formattedDate
}

fun String.toReadableDate(): String {
    val instant = Instant.parse(this)
    val date = instant.toLocalDateTime(TimeZone.UTC).date

    val day = date.day
    val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    val year = date.year

    return "${day.withSuffix()} $month $year"
}

fun Int.withSuffix(): String {
    return when {
        this % 100 in 11..13 -> "${this}th"
        this % 10 == 1 -> "${this}st"
        this % 10 == 2 -> "${this}nd"
        this % 10 == 3 -> "${this}rd"
        else -> "${this}th"
    }
}

fun String.toSlashDate(): String {

    val inputFormat = LocalDate.Format {

        this@Format.day(padding = Padding.ZERO)

        char('-')

        monthName(MonthNames.ENGLISH_ABBREVIATED)

        char('-')

        year()

    }

    val outputFormat = LocalDate.Format {

        this@Format.day(padding = Padding.ZERO)

        char('/')

        monthNumber()

        char('/')

        year()

    }

    return LocalDate.parse(this, inputFormat).format(outputFormat)

}



private val slashDateFormat = LocalDate.Format {

    this@Format.day(padding = Padding.ZERO)

    char('/')

    monthNumber()

    char('/')

    year()

}

fun String.incrementSlashDateYearYear(): String = runCatching {

    LocalDate

        .parse(this, slashDateFormat)

        .plus(1, DateTimeUnit.YEAR)

        .format(slashDateFormat)

}.getOrDefault(this)

fun String.decrementSlashDateYearYear(): String = runCatching {

    LocalDate

        .parse(this, slashDateFormat)

        .minus(1, DateTimeUnit.YEAR)

        .format(slashDateFormat)

}.getOrDefault(this)