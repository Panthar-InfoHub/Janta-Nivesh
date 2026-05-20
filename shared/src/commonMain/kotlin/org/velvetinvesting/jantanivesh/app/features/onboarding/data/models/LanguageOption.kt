package org.velvetinvesting.jantanivesh.app.features.onboarding.data.models

data class LanguageOption(
    val id: String,          // Unique identifier (e.g., "hi", "mr", "gu")
    val nativeName: String,  // e.g., "हिन्दी", "मराठी"
    val englishName: String  // e.g., "Hindi", "Marathi"
)