package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums

enum class KycType(
    val code: String,
    val displayName: String
) {

    CKYC_COMPLIANT(
        code = "C",
        displayName = "CKYC Compliant (C)"
    ),

    KRA_COMPLIANT(
        code = "K",
        displayName = "KRA Compliant (K)"
    ),

    BIOMETRIC_KYC(
        code = "B",
        displayName = "Biometric KYC (B)"
    ),

    AADHAAR_EKYC(
        code = "E",
        displayName = "Aadhaar eKYC (E)"
    ),

    PAN(
        code = "P",
        displayName = "PAN (P)"
    );

    companion object {

        fun fromCode(code: String): KycType? {
            return entries.find { it.code == code }
        }

        fun getDisplayName(code: String): String {
            return fromCode(code)?.displayName.orEmpty()
        }
    }
}