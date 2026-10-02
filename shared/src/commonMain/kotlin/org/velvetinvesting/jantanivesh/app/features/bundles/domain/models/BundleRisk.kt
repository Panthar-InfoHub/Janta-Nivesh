package org.velvetinvesting.jantanivesh.app.features.bundles.domain.models

/**
 * How risky a bundle is, and how a bundle of that risk is drawn.
 *
 * Adding a risk is two steps: an entry here, and a case in the meta-data mapper that recognises
 * the API's string for it.
 */
enum class BundleRisk(val label: String, val style: BundleRiskStyle) {
    LOW(
        label = "Low Risk",
        style = BundleRiskStyle(outline = 0xFFFDE68A, background = 0xFFFFFCF6, accent = 0xFF92400E)
    ),
    MODERATE(
        label = "Moderate Risk",
        style = BundleRiskStyle(outline = 0xFF0369A1, background = 0xFFF0F9FF, accent = 0xFF0369A1)
    ),
    HIGH(
        label = "High Risk",
        style = BundleRiskStyle(outline = 0xFFBE123C, background = 0xFFFFF1F2, accent = 0xFFBE123C)
    ),
    VERY_HIGH(
        label = "Very High Risk",
        style = BundleRiskStyle(outline = 0xFF7E22CE, background = 0xFFFAF5FF, accent = 0xFF7E22CE)
    ),
    UNKNOWN(
        label = "Unknown",
        style = BundleRiskStyle(outline = 0xffE2E8F0, background = 0xffF1F5F9, accent = 0xff334155)
    )
}

/**
 * A bundle card's palette, as ARGB colour values so the domain stays free of UI types.
 *
 * [accent] colours text drawn on the card's tint, such as the risk pill's label. It matches
 * [outline] except where the outline is too light to read as text.
 */
data class BundleRiskStyle(
    val outline: Long,
    val background: Long,
    val accent: Long
)
