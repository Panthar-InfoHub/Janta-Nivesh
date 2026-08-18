package org.velvetinvesting.jantanivesh.app.features.kycnew.domain.model

/**
 * Picklists for the onboarding forms. Each entry pairs the wire value the API expects with the
 * label shown in the dropdown — the UI never handles the raw key, and the request never handles
 * the label.
 */
enum class Occupation(val apiValue: String, val displayName: String) {
    BUSINESS("business", "Business"),
    PROFESSIONAL("professional", "Professional"),
    SERVICE("service", "Service"),
    PRIVATE_SECTOR_SERVICE("private_sector_service", "Private Sector Service"),
    PUBLIC_SECTOR_SERVICE("public_sector_service", "Public Sector Service"),
    GOVERNMENT_SERVICE("government_service", "Government Service"),
    DOCTOR("doctor", "Doctor"),
    AGRICULTURE("agriculture", "Agriculture"),
    FOREX_DEALER("forex_dealer", "Forex Dealer"),
    STUDENT("student", "Student"),
    HOUSE_WIFE("house_wife", "Housewife"),
    RETIRED("retired", "Retired"),
    OTHERS("others", "Others")
}

enum class SourceOfFund(val apiValue: String, val displayName: String) {
    SALARY("salary", "Salary"),
    BUSINESS("business", "Business"),
    GIFT("gift", "Gift"),
    ANCESTRAL_PROPERTY("ancestral_property", "Ancestral Property"),
    RENTAL_INCOME("rental_income", "Rental Income"),
    PRIZE_MONEY("prize_money", "Prize Money"),
    ROYALTY("royalty", "Royalty"),
    OTHERS("others", "Others")
}

enum class IncomeSlab(val apiValue: String, val displayName: String) {
    UPTO_1_LAKH("upto_1lakh", "Up to ₹1 Lakh"),
    ABOVE_1_UPTO_5_LAKH("above_1lakh_upto_5lakh", "₹1 Lakh - ₹5 Lakh"),
    ABOVE_5_UPTO_10_LAKH("above_5lakh_upto_10lakh", "₹5 Lakh - ₹10 Lakh"),
    ABOVE_10_UPTO_25_LAKH("above_10lakh_upto_25lakh", "₹10 Lakh - ₹25 Lakh"),
    ABOVE_25_LAKH_UPTO_1_CR("above_25lakh_upto_1cr", "₹25 Lakh - ₹1 Crore"),
    ABOVE_1_CR("above_1cr", "Above ₹1 Crore")
}

enum class NomineeRelation(val apiValue: String, val displayName: String) {
    FATHER("father", "Father"),
    MOTHER("mother", "Mother"),
    SPOUSE("spouse", "Spouse"),
    SON("son", "Son"),
    DAUGHTER("daughter", "Daughter"),
    BROTHER("brother", "Brother"),
    SISTER("sister", "Sister"),
    GRAND_FATHER("grand_father", "Grandfather"),
    GRAND_MOTHER("grand_mother", "Grandmother"),
    GRAND_SON("grand_son", "Grandson"),
    GRAND_DAUGHTER("grand_daughter", "Granddaughter"),
    FATHER_IN_LAW("father_in_law", "Father-in-law"),
    MOTHER_IN_LAW("mother_in_law", "Mother-in-law"),
    SON_IN_LAW("son_in_law", "Son-in-law"),
    DAUGHTER_IN_LAW("daughter_in_law", "Daughter-in-law"),
    BROTHER_IN_LAW("brother_in_law", "Brother-in-law"),
    SISTER_IN_LAW("sister_in_law", "Sister-in-law"),
    UNCLE("uncle", "Uncle"),
    AUNT("aunt", "Aunt"),
    NEPHEW("nephew", "Nephew"),
    NIECE("niece", "Niece"),
    COURT_APPOINTED_LEGAL_GUARDIAN(
        "court_appointed_legal_guardian",
        "Court Appointed Legal Guardian"
    ),
    OTHERS("others", "Others")
}

/**
 * Identity document used to reference the nominee. Only [PAN] is live today — the rest are
 * documented as upcoming, so the server may reject them until it catches up.
 *
 * [displayName] is the untranslated fallback; the UI shows the localized label instead (see
 * `nomineeDocumentLabel` in the nominee screen), so both stay in step in every language.
 */
enum class NomineeDocumentType(val apiValue: String, val displayName: String) {
    PAN("pan", "PAN"),
    AADHAAR("aadhaar", "Aadhar (Last 4 digits)"),
    DRIVING_LICENCE("driving_licence", "Driving Licence"),
    PASSPORT("passport", "OCI / Passport")
}
