package org.velvetinvesting.jantanivesh.app.core.localization.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val englishName: String
) {
    HINDI(
        code = "hi",
        displayName = "हिन्दी",
        englishName = "Hindi"
    ),
    MARATHI(
        code = "mr",
        displayName = "मराठी",
        englishName = "Marathi"
    ),
    GUJARATI(
        code = "gu",
        displayName = "ગુજરાતી",
        englishName = "Gujarati"
    ),
    TAMIL(
        code = "ta",
        displayName = "தமிழ்",
        englishName = "Tamil"
    ),
    TELUGU(
        code = "te",
        displayName = "తెలుగు",
        englishName = "Telugu"
    ),
    BENGALI(
        code = "bn",
        displayName = "বাংলা",
        englishName = "Bengali"
    );

    companion object {

        val DEFAULT = HINDI

        fun fromCode(code: String): AppLanguage {
            return entries.find {
                it.code == code
            } ?: DEFAULT
        }
    }
}