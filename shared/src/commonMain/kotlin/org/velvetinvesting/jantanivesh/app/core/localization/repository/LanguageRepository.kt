package org.velvetinvesting.jantanivesh.app.core.localization.repository

import kotlinx.coroutines.flow.Flow
import org.velvetinvesting.jantanivesh.app.core.localization.model.AppLanguage

interface LanguageRepository {
    fun currentLanguageFlow(): Flow<AppLanguage>
    suspend fun getLanguage(): AppLanguage
    suspend fun setLanguage(language: AppLanguage)
}