package org.velvetinvesting.jantanivesh.app.core.localization.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.velvetinvesting.jantanivesh.app.core.localization.model.AppLanguage

class LanguageRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
): LanguageRepository {

    private val languageKey = stringPreferencesKey("language")

    override fun currentLanguageFlow(): Flow<AppLanguage> {
        return dataStore.data.map {
            it[languageKey]?.let { languageCode ->
                AppLanguage.fromCode(languageCode)
            }?: AppLanguage.HINDI
        }
    }

    override suspend fun getLanguage(): AppLanguage {
        return dataStore.data.first()[languageKey]
            ?.let {
                AppLanguage.fromCode(it)
            }?: AppLanguage.ENGLISH
    }

     override suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit {
            it[languageKey] = language.code
        }
    }
}