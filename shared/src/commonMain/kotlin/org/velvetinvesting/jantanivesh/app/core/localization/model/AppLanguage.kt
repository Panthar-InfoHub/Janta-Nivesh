package org.velvetinvesting.jantanivesh.app.core.localization.model

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "हिंदी"),
    ;

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find {
                it.code == code
            } ?: ENGLISH
        }
    }
}