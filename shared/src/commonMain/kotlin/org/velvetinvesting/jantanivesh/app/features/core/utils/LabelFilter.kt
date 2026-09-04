package org.velvetinvesting.jantanivesh.app.features.core.utils

import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.MfFilterIds

sealed interface LabelFilter {
    val title: String
    val id: String
}

/**
 * The fund-list chips. Each id is a `tag` value accepted by `GET /mf/funds`, so selecting a chip
 * is the same filter the tray applies — the two cannot drift apart.
 */
sealed interface MutualFundLabel : LabelFilter {

    data object Popular : MutualFundLabel {
        override val title = "Popular"
        override val id = MfFilterIds.TAG_POPULAR
    }

    data object LargeCap : MutualFundLabel {
        override val title = "Large Cap"
        override val id = MfFilterIds.TAG_LARGE_CAP
    }

    data object MidCap : MutualFundLabel {
        override val title = "Mid Cap"
        override val id = MfFilterIds.TAG_MID_CAP
    }

    data object SmallCap : MutualFundLabel {
        override val title = "Small Cap"
        override val id = MfFilterIds.TAG_SMALL_CAP
    }

    data object FlexiCap : MutualFundLabel {
        override val title = "Flexi Cap"
        override val id = MfFilterIds.TAG_FLEXI_CAP
    }

    data object MultiCap : MutualFundLabel {
        override val title = "Multi Cap"
        override val id = MfFilterIds.TAG_MULTI_CAP
    }

    data object Debt : MutualFundLabel {
        override val title = "Debt"
        override val id = MfFilterIds.TAG_DEBT
    }

    data object Others : MutualFundLabel {
        override val title = "Others"
        override val id = MfFilterIds.TAG_OTHERS
    }

    /** Stands in for a tray selection that no single chip represents. */
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
