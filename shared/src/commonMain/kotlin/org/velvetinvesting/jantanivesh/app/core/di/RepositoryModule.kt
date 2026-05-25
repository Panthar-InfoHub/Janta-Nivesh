package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.core.localization.repository.LanguageRepository
import org.velvetinvesting.jantanivesh.app.core.localization.repository.LanguageRepositoryImpl

val repositoryModule = module {
    single<LanguageRepository> { LanguageRepositoryImpl(get()) }
}
