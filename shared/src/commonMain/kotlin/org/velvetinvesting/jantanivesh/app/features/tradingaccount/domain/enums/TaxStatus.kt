package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums

enum class TaxStatus(
    val code: String,
    val displayName: String,
    val nomineeAllowed: Boolean,
    val isMinor: Boolean,
    val isCorporate: Boolean,
    val isJointAllowed: Boolean,
    val isResident: Boolean
) {

    INDIVIDUAL(
        code = "01",
        displayName = "INDIVIDUAL",
        nomineeAllowed = true,
        isMinor = false,
        isCorporate = false,
        isJointAllowed = true,
        isResident = true
    ),

    ON_BEHALF_OF_MINOR(
        code = "02",
        displayName = "ON BEHALF OF MINOR",
        nomineeAllowed = false,
        isMinor = true,
        isCorporate = false,
        isJointAllowed = false,
        isResident = true
    ),

    HUF(
        code = "03",
        displayName = "HUF",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    COMPANY(
        code = "04",
        displayName = "COMPANY",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    AOP(
        code = "05",
        displayName = "AOP",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    PARTNERSHIP_FIRM(
        code = "06",
        displayName = "PARTNERSHIP FIRM",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    BODY_CORPORATE(
        code = "07",
        displayName = "BODY CORPORATE",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    TRUST(
        code = "08",
        displayName = "TRUST",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    SOCIETY(
        code = "09",
        displayName = "SOCIETY",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    OTHERS(
        code = "10",
        displayName = "OTHERS",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    NRI_OTHERS(
        code = "11",
        displayName = "NRI-OTHERS",
        nomineeAllowed = true,
        isMinor = false,
        isCorporate = false,
        isJointAllowed = true,
        isResident = false
    ),

    SOLE_PROPRIETORSHIP(
        code = "13",
        displayName = "SOLE PROPRIETORSHIP",
        nomineeAllowed = true,
        isMinor = false,
        isCorporate = false,
        isJointAllowed = false,
        isResident = true
    ),

    NRI_REPATRIABLE_NRE(
        code = "21",
        displayName = "NRI - REPATRIABLE (NRE)",
        nomineeAllowed = true,
        isMinor = false,
        isCorporate = false,
        isJointAllowed = true,
        isResident = false
    ),

    NRI_REPATRIABLE_NRO(
        code = "24",
        displayName = "NRI - REPATRIABLE (NRO)",
        nomineeAllowed = true,
        isMinor = false,
        isCorporate = false,
        isJointAllowed = true,
        isResident = false
    ),

    NRI_CHILD(
        code = "26",
        displayName = "NRI CHILD",
        nomineeAllowed = false,
        isMinor = true,
        isCorporate = false,
        isJointAllowed = false,
        isResident = false
    ),

    NRI_HUF_NRE(
        code = "29",
        displayName = "NRI - HUF (NRE)",
        nomineeAllowed = true,
        isMinor = false,
        isCorporate = false,
        isJointAllowed = true,
        isResident = false
    ),

    LLP(
        code = "47",
        displayName = "LLP",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    PUBLIC_LIMITED_COMPANY(
        code = "51",
        displayName = "PUBLIC LIMITED COMPANY",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    PRIVATE_LIMITED_COMPANY(
        code = "52",
        displayName = "PRIVATE LIMITED COMPANY",
        nomineeAllowed = false,
        isMinor = false,
        isCorporate = true,
        isJointAllowed = false,
        isResident = true
    ),

    BODY_OF_INDIVIDUALS(
        code = "59",
        displayName = "BODY OF INDIVIDUALS",
        nomineeAllowed = true,
        isMinor = false,
        isCorporate = false,
        isJointAllowed = true,
        isResident = true
    ),

    PERSON_OF_INDIAN_ORIGIN(
        code = "70",
        displayName = "PERSON OF INDIAN ORIGIN",
        nomineeAllowed = true,
        isMinor = false,
        isCorporate = false,
        isJointAllowed = true,
        isResident = false
    );

    companion object {
        fun fromCode(code: String): TaxStatus? {
            return entries.find { it.code == code }
        }
    }
}