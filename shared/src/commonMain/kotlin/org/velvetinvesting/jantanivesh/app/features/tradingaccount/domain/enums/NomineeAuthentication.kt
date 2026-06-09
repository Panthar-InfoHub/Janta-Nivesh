package org.sharad.velvetinvestment.utils.tradingaccount

enum class NominationAuthentication(
    val code: String,
    val displayName: String,
    val allowedForNominationOpt: String

) {

    WET_SIGNATURE(
        code = "W",
        displayName = "Wet Signature",
        allowedForNominationOpt = "Y"
    ),

    E_SIGN(
        code = "E",
        displayName = "E-Sign",
        allowedForNominationOpt = "Y"
    ),

    OTP_AUTHENTICATION(
        code = "O",
        displayName = "OTP Authentication",
        allowedForNominationOpt = "Y"
    ),

    OTP_DECLARATION(
        code = "O",
        displayName = "OTP With Declaration",
        allowedForNominationOpt = "N"

    ),

    VIDEO_RECORDING(
        code = "V",
        displayName = "Video Recording",
        allowedForNominationOpt = "N"
    );

    companion object {
        fun fromCode(code: String?): NominationAuthentication? {
            return entries.firstOrNull { it.code == code }
        }
        fun getAllowedOptions(nominationOpt: String): List<NominationAuthentication> {
            return entries.filter {
                it.allowedForNominationOpt == nominationOpt
            }
        }
    }

}