package org.velvetinvesting.jantanivesh.app.features.fd.data.mapper

import org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto.*
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.*
import org.velvetinvesting.jantanivesh.app.core.utils.parseHtmlToReadableText

fun FixedDepositListDto.toDomain(): PaginatedData<FixedDepositDomain> {
    val paginationData = this.`data`.pagination
    return PaginatedData(
        items = `data`.fd_products.map { it.toDomain() },
        page = paginationData.page,
        pageSize = paginationData.limit,
        totalItems = paginationData.total,
        totalPages = paginationData.totalPages,
        hasNextPage = paginationData.page < paginationData.totalPages
    )
}

fun FdProductDto.toDomain(): FixedDepositDomain {
    val interestList = interest_rates.map { it.toDomain() }

    return FixedDepositDomain(
        id = id,
        bankName = issuer.full_name,
        bankLogoUrl = issuer.logo_url,
        riskLevel = tags.toRiskLevel(),
        baseInterest = interestList.maxOfOrNull { it.interestRate } ?: 0.0,
        minDeposit = min_deposit.toLongOrNull() ?: 0L,
        tenures = interestList,
        bankTag = tags.firstOrNull()?.text ?: "",
        tags = tags.map { it.text }
    )
}

fun InterestRateDto.toDomain(): FixedDepositTenureDomain {
    return FixedDepositTenureDomain(
        tenure = TenureRangeList.fromDays(tenure_days),
        tenureDays = tenure_days,
        interestRate = interest_rate.toDoubleOrNull() ?: 0.0,
        receiveMin = 0L,
        receiveMax = 0L
    )
}

fun List<TagDto>.toRiskLevel(): RiskLevel {
    val ratingText = this.joinToString { it.text }.uppercase()

    return when {
        "AAA" in ratingText || "A1+" in ratingText -> RiskLevel.LOW
        "AA" in ratingText || "A+" in ratingText -> RiskLevel.MODERATE
        else -> RiskLevel.HIGH
    }
}

fun FDDetailsDto.toDomain(): FDDetailsDomain {
    val payouts = `data`.interest_rates
        .distinctBy { it.payout_frequency }
        .map {
            PayoutType.fromId(it.payout_frequency)
        }

    val selectedPayout = PayoutType.defaultSelection(payouts)
    return FDDetailsDomain(
        id = `data`.id,
        invest = `data`.min_deposit.toLongOrNull() ?: 1000,
        selectedPayout = selectedPayout,
        applicable = `data`.interest_rates.firstOrNull()?.customer_type?.toReadableCustomerType() ?: "",

        // Header
        bankName = `data`.issuer.display_name,
        bankLogo = `data`.issuer.logo_url,
        rating = `data`.issuer.rating_text ?: "N/A",
        maxInterestRate = `data`.interest_rates.maxOfOrNull {
            it.interest_rate.toDoubleOrNull() ?: 0.0
        } ?: 0.0,
        riskLabel = if (`data`.issuer.rating_text != null) extractRiskLevel(`data`.issuer.rating_text) else RiskLevel.LOW,

        minDeposit = `data`.min_deposit.toLongOrNull() ?: 0L,

        payoutOptions = payouts,

        applicableFor = `data`.interest_rates
            .map { it.customer_type }
            .distinct()
            .map { it.toReadableCustomerType() },

        // Interest Table
        interestRates = `data`.interest_rates.map {
            FDTenureDomain(
                id = it.id,
                tenureLabel = it.tenure_label,
                tenureDays = it.tenure_days,
                interestRate = it.interest_rate.toDoubleOrNull() ?: 0.0,
                annualYield = it.annualized_yield.toDoubleOrNull() ?: 0.0,
                isDefault = it.is_default_selection,
                payoutFrequency = PayoutType.fromId(it.payout_frequency)
            )
        }.sortedBy { it.tenureDays },

        // Lock Section
        lockInDays = `data`.lock_in_period_days,
        prematurePenalty = `data`.premature_penalty_percent.toDouble(),

        insuranceAmount = "₹5L",

        // About
        about = `data`.issuer.about_description,

        // Key Features
        keyFeatures = `data`.usps.map {
            KeyFeatureDomain(
                title = it.title,
                description = it.description,
                iconUrl = it.icon
            )
        },

        // FAQ
        faqs = `data`.faqs.map {
            FDFaqDomain(
                question = it.title,
                answer = it.description.parseHtmlToReadableText()
            )
        },
        tags = `data`.tags.map { it.text }
    )
}

fun String.toReadableCustomerType(): String {
    return when (this.uppercase()) {
        "STANDARD" -> "Regular"
        "SENIOR_CITIZEN" -> "Senior Citizen"
        "WOMEN" -> "Women"
        else -> this.lowercase().replaceFirstChar { it.uppercase() }
    }
}

fun extractRiskLevel(ratingText: String): RiskLevel {
    return when {
        "AAA" in ratingText || "A1+" in ratingText -> RiskLevel.LOW
        "AA" in ratingText || "A+" in ratingText -> RiskLevel.MODERATE
        else -> RiskLevel.HIGH
    }
}
