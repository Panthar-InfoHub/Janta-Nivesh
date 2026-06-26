package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models

enum class PendingAction(
    val label: String
) {

    VKYC(
        label = "Complete VKYC"
    ),

    PAYMENT(
        label = "Complete Payment"
    ),

    COMPLETED(
        label = "Completed"
    );

    companion object {

        fun fromValue(value: String?): PendingAction {
            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true)
            } ?: COMPLETED
        }
    }

    fun isPending(): Boolean {
        return this == VKYC || this == PAYMENT
    }

    fun isCompleted(): Boolean {
        return this == COMPLETED
    }
}