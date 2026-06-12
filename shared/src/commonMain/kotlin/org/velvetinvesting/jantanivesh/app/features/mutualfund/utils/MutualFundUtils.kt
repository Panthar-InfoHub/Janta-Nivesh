package org.velvetinvesting.jantanivesh.app.features.mutualfund.utils

private val keepUppercase = setOf(
    "ETF", "ELSS", "SIP", "STP", "SWP",
    "NFO", "FOF", "LIC", "ITI", "UTI",
    "DSP", "HDFC", "HSBC", "JM", "SBI",
    "MF", "S&P", "BSE", "NSE", "ICICI"
)

fun String.toTitleCase(): String {
    return lowercase()
        .replace(Regex("(^|[\\s\\-_.\\/]+)([a-z])")) {
            it.groupValues[1] + it.groupValues[2].uppercase()
        }
        .split(" ")
        .joinToString(" ") { word ->
            val normalized = word.uppercase()
            if (normalized in keepUppercase) {
                normalized
            } else {
                word
            }
        }
}
