package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums

enum class StateCode(
    val code: String,
    val displayName: String
) {

    ANDAMAN_NICOBAR(
        code = "AN",
        displayName = "Andaman & Nicobar"
    ),

    ANDHRA_PRADESH(
        code = "AP",
        displayName = "Andhra Pradesh"
    ),

    ARUNACHAL_PRADESH(
        code = "AR",
        displayName = "Arunachal Pradesh"
    ),

    ASSAM(
        code = "AS",
        displayName = "Assam"
    ),

    BIHAR(
        code = "BH",
        displayName = "Bihar"
    ),

    CHHATTISGARH(
        code = "CG",
        displayName = "Chhattisgarh"
    ),

    GOA(
        code = "GO",
        displayName = "Goa"
    ),

    GUJARAT(
        code = "GU",
        displayName = "Gujarat"
    ),

    HARYANA(
        code = "HA",
        displayName = "Haryana"
    ),

    HIMACHAL_PRADESH(
        code = "HP",
        displayName = "Himachal Pradesh"
    ),

    JAMMU_KASHMIR(
        code = "JM",
        displayName = "Jammu & Kashmir"
    ),

    JHARKHAND(
        code = "JK",
        displayName = "Jharkhand"
    ),

    KARNATAKA(
        code = "KA",
        displayName = "Karnataka"
    ),

    KERALA(
        code = "KE",
        displayName = "Kerala"
    ),

    MADHYA_PRADESH(
        code = "MP",
        displayName = "Madhya Pradesh"
    ),

    MAHARASHTRA(
        code = "MA",
        displayName = "Maharashtra"
    ),

    MANIPUR(
        code = "MN",
        displayName = "Manipur"
    ),

    MEGHALAYA(
        code = "ME",
        displayName = "Meghalaya"
    ),

    MIZORAM(
        code = "MI",
        displayName = "Mizoram"
    ),

    NAGALAND(
        code = "NA",
        displayName = "Nagaland"
    ),

    NEW_DELHI(
        code = "ND",
        displayName = "New Delhi"
    ),

    ODISHA(
        code = "OR",
        displayName = "Odisha"
    ),

    PUNJAB(
        code = "PU",
        displayName = "Punjab"
    ),

    RAJASTHAN(
        code = "RA",
        displayName = "Rajasthan"
    ),

    SIKKIM(
        code = "SI",
        displayName = "Sikkim"
    ),

    TAMIL_NADU(
        code = "TN",
        displayName = "Tamil Nadu"
    ),

    TELANGANA(
        code = "TG",
        displayName = "Telangana"
    ),

    TRIPURA(
        code = "TR",
        displayName = "Tripura"
    ),

    UTTAR_PRADESH(
        code = "UP",
        displayName = "Uttar Pradesh"
    ),

    UTTARAKHAND(
        code = "UL",
        displayName = "Uttarakhand"
    ),

    WEST_BENGAL(
        code = "WB",
        displayName = "West Bengal"
    );

    companion object {

        fun fromCode(code: String): StateCode? {
            return entries.find { it.code == code }
        }

        fun getDisplayName(code: String): String {
            return fromCode(code)?.displayName.orEmpty()
        }
    }
}