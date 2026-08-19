package org.velvetinvesting.jantanivesh.app.features.onboarding.ui

/**
 * Input sanitisers and validators shared by the onboarding screens.
 *
 * Sanitisers run on every keystroke and only ever *drop* characters, so a field can never hold a
 * value the API would reject on shape alone. Validators decide whether the screen's primary
 * button is enabled.
 */
object OnboardingInput {

    const val PAN_LENGTH = 10
    const val PINCODE_LENGTH = 6
    const val PHONE_LENGTH = 10

    /** The nominee Aadhaar field takes only the last four digits, never the full number. */
    const val AADHAAR_SUFFIX_LENGTH = 4

    /** Keeps a typed annual income well inside `Long`, however many zeroes get entered. */
    const val ANNUAL_INCOME_MAX_DIGITS = 12

    private val PAN_REGEX = Regex("^[A-Z]{5}[0-9]{4}[A-Z]$")
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val ISO_DATE_REGEX = Regex("^\\d{4}-\\d{2}-\\d{2}$")

    /** Uppercase alphanumerics only, capped at PAN's fixed length. */
    fun sanitizePan(input: String): String =
        input.filter { it.isLetterOrDigit() }.uppercase().take(PAN_LENGTH)

    /** Same shape as a PAN but without the length cap, for the other identity documents. */
    fun sanitizeDocumentNumber(input: String): String =
        input.filter { it.isLetterOrDigit() }.uppercase().take(20)

    /** Letters, spaces and the punctuation that shows up in Indian names. */
    fun sanitizeName(input: String): String =
        input.filter { it.isLetter() || it == ' ' || it == '.' || it == '\'' || it == '-' }
            .trimStart()
            .take(60)

    fun sanitizeDigits(input: String, maxLength: Int): String =
        input.filter { it.isDigit() }.take(maxLength)

    /** Percentages are 0-100, so anything that would exceed 100 is rejected outright. */
    fun sanitizePercentage(input: String): String {
        val digits = input.filter { it.isDigit() }.take(3)
        val value = digits.toIntOrNull() ?: return ""
        return if (value > 100) digits.dropLast(1) else digits
    }

    /** Emails never contain whitespace, and the server treats them case-insensitively. */
    fun sanitizeEmail(input: String): String =
        input.filterNot { it.isWhitespace() }.lowercase().take(80)

    /** Free text that still has to survive a JSON round trip — just trims leading blanks. */
    fun sanitizeText(input: String, maxLength: Int = 120): String =
        input.trimStart().take(maxLength)

    fun isValidPan(value: String): Boolean = PAN_REGEX.matches(value)

    fun isValidEmail(value: String): Boolean = EMAIL_REGEX.matches(value)

    fun isValidPincode(value: String): Boolean =
        value.length == PINCODE_LENGTH && value.all { it.isDigit() }

    /** Indian mobile numbers are 10 digits starting with 6-9. */
    fun isValidPhone(value: String): Boolean =
        value.length == PHONE_LENGTH && value.all { it.isDigit() } && value.first() in '6'..'9'

    fun isValidIsoDate(value: String): Boolean = ISO_DATE_REGEX.matches(value)

    fun isFilled(value: String): Boolean = value.isNotBlank()
}