package org.velvetinvesting.jantanivesh.app.features.fd.domain.model

enum class SelectionType {
    SINGLE,
    MULTIPLE
}

data class FilterOption(
    val id: String,
    val title: String,
    val isSelected: Boolean = false
)

data class FilterGroup(
    val id: String,
    val title: String,
    val selectionType: SelectionType,
    val options: List<FilterOption>
)

data class InvestmentFilter(
    val groups: List<FilterGroup>
)

sealed interface LabelFilter {
    val title: String
    val id: String
}

sealed interface FDLabel : LabelFilter {

    data object PublicBank : FDLabel {
        override val title = "Public Bank"
        override val id = "public_bank"
    }

    data object PrivateBank : FDLabel {
        override val title = "Private Bank"
        override val id = "private_bank"
    }

    data object NBFC : FDLabel {
        override val title = "NBFC"
        override val id = "nbfc"
    }

    data object SmallStart : FDLabel {
        override val title = "Small Start"
        override val id = "small_start"
    }

    data object WomenSpecial : FDLabel {
        override val title = "Women Special"
        override val id = "women_special"
    }

    data class CustomLabel(
        override val title: String,
        override val id: String
    ) : FDLabel
}
