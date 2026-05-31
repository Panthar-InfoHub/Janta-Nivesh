package org.velvetinvesting.jantanivesh.app.features.fd.domain.model

data class FDDetailsDomain(
    val id: String,
    val invest: Long = 1000,
    val selectedPayout: PayoutType?,
    val applicable: String = "",
    val bankName: String,
    val bankLogo: String,
    val rating: String,
    val maxInterestRate: Double,
    val riskLabel: RiskLevel,
    val minDeposit: Long,
    val payoutOptions: List<PayoutType>,
    val applicableFor: List<String>,
    val interestRates: List<FDTenureDomain>,
    val lockInDays: Int,
    val prematurePenalty: Double,
    val insuranceAmount: String,
    val about: String,
    val keyFeatures: List<KeyFeatureDomain>,
    val faqs: List<FDFaqDomain>
)

data class KeyFeatureDomain(
    val title: String,
    val description: String,
    val iconUrl: String? = null
)

data class FDTenureDomain(
    val id: String,
    val tenureLabel: String,
    val tenureDays: Int,
    val interestRate: Double,
    val annualYield: Double,
    val isDefault: Boolean,
    val payoutFrequency: PayoutType,
)

data class FDFaqDomain(
    val question: String,
    val answer: String
)

sealed interface PayoutType {
    val id: String
    val displayName: String

    data object Cumulative : PayoutType {
        override val id = "CUMULATIVE"
        override val displayName = "Maturity"
    }

    data object Monthly : PayoutType {
        override val id = "MONTHLY"
        override val displayName = "Monthly"
    }

    data object Quarterly : PayoutType {
        override val id = "QUARTERLY"
        override val displayName = "Quarterly"
    }

    data object HalfYearly : PayoutType {
        override val id = "HALF_YEARLY"
        override val displayName = "Half Yearly"
    }

    data object Yearly : PayoutType {
        override val id = "YEARLY"
        override val displayName = "Yearly"
    }

    data class Custom(
        override val id: String
    ) : PayoutType {
        override val displayName: String =
            id.lowercase().replace("_", " ")
                .replaceFirstChar { it.uppercase() }
    }

    companion object {
        fun fromId(id: String): PayoutType {
            return when (id.uppercase()) {
                Cumulative.id -> Cumulative
                Monthly.id -> Monthly
                Quarterly.id -> Quarterly
                HalfYearly.id -> HalfYearly
                Yearly.id -> Yearly
                else -> Custom(id)
            }
        }

        fun defaultSelection(payouts: List<PayoutType>): PayoutType? {
            return payouts.firstOrNull { it is Cumulative }
                ?: payouts.firstOrNull()
        }
    }
}
