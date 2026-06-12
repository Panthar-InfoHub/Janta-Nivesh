package org.velvetinvesting.jantanivesh.app.features.core.utils

sealed interface LabelFilter {
    val title: String
    val id: String
}

sealed interface MutualFundLabel : LabelFilter {


    data object IndexOnly : MutualFundLabel {
        override val title = "Index"
        override val id = "index"
    }

    data object FlexiCap : MutualFundLabel {
        override val title = "Flexi Cap"
        override val id = "flexi_cap"
    }

    data object LargeCap : MutualFundLabel {
        override val title = "Large Cap"
        override val id = "large_cap"
    }

    data object MidCap : MutualFundLabel {
        override val title = "Mid Cap"
        override val id = "mid_cap"
    }

    data object SmallCap : MutualFundLabel {
        override val title = "Small Cap"
        override val id = "small_cap"
    }

    data object LargeMidCap : MutualFundLabel {
        override val title = "Large & Mid Cap"
        override val id = "large_Mid_cap"
    }

    data object GlobalOthers : MutualFundLabel {
        override val title = "Global / Others"
        override val id = "global_others"
    }

    data class CustomLabel(
        override val title: String,
        override val id: String = "custom"
    ) : MutualFundLabel
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
    ) : LabelFilter
}
