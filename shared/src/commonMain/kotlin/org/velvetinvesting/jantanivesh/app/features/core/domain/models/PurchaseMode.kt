package org.velvetinvesting.jantanivesh.app.features.core.domain.models

/**
 * The three ways a fund or a bundle can be bought. [frequency] is what the SIP endpoint expects; it is
 * unused for [ONE_TIME], which goes to the lumpsum endpoint instead.
 */
enum class PurchaseMode(val label: String, val frequency: String) {
    DAILY("Daily", "daily"),
    MONTHLY("Monthly", "monthly"),
    ONE_TIME("One-time", "");

    val isSip: Boolean
        get() = this != ONE_TIME

    /** Only a monthly SIP debits on a fixed day of the month. */
    val needsInstallmentDay: Boolean
        get() = this == MONTHLY

    companion object {
        /** Recovers a mode from a navigation argument; an unknown name falls back to monthly. */
        fun fromName(name: String?): PurchaseMode =
            entries.firstOrNull { it.name == name } ?: MONTHLY
    }
}
