package org.velvetinvesting.jantanivesh.app.core.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.core.localization.repository.LanguageRepository
import org.velvetinvesting.jantanivesh.app.core.localization.repository.LanguageRepositoryImpl
import org.velvetinvesting.jantanivesh.app.core.networking.getHttpClient
import org.velvetinvesting.jantanivesh.app.features.core.data.local.repository.AuthPrefsImpl
import org.velvetinvesting.jantanivesh.app.features.core.data.remote.repository.UserDataRepoImpl
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.UserDataRepo
import org.velvetinvesting.jantanivesh.app.features.login.data.repository.UserAuthenticationRepo
import org.velvetinvesting.jantanivesh.app.features.login.domain.repository.UserAuth
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.remote.repository.OnboardingRepoImpl
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.repository.OnboardingRepo
import org.velvetinvesting.jantanivesh.app.features.kyc.data.repository.MFKYCRepoImpl
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.repository.MFKYCRepository

val repositoryModule = module {
    single<LanguageRepository> { LanguageRepositoryImpl(get()) }
    single<AuthPrefs> { AuthPrefsImpl(get()) }
    single<HttpClient> { getHttpClient(get()) }
    single<UserAuth> { UserAuthenticationRepo(get(), get(), get()) }
    single<OnboardingRepo> { OnboardingRepoImpl(get(), get()) }
    single<MFKYCRepository> { MFKYCRepoImpl(get()) }
    single<UserDataRepo> { UserDataRepoImpl(get()) }
}
