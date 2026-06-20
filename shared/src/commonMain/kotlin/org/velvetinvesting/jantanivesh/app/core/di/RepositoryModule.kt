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
import org.velvetinvesting.jantanivesh.app.features.fd.data.repository.FixedDepositRepo
import org.velvetinvesting.jantanivesh.app.features.fd.domain.repository.FixedDepositRepository
import org.velvetinvesting.jantanivesh.app.features.kyc.data.repository.MFKYCRepoImpl
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.repository.MFKYCRepository
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.repository.MutualFundRepo
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.data.repository.TradingAccountRepoImpl
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.repository.TradingAccountRepo
import org.velvetinvesting.jantanivesh.app.features.goals.data.repository.GoalsRepositoryImpl
import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.GoalsRepository

val repositoryModule = module {
    single<LanguageRepository> { LanguageRepositoryImpl(get()) }
    single<AuthPrefs> { AuthPrefsImpl(get()) }
    single<HttpClient> { getHttpClient(get()) }
    single<UserAuth> { UserAuthenticationRepo(get(), get(), get()) }
    single<OnboardingRepo> { OnboardingRepoImpl(get(), get()) }
    single<MFKYCRepository> { MFKYCRepoImpl(get()) }
    single<UserDataRepo> { UserDataRepoImpl(get()) }
    single<TradingAccountRepo> { TradingAccountRepoImpl(get()) }
    single<FixedDepositRepository> { FixedDepositRepo(get()) }
    single<MutualFundRepository> { MutualFundRepo(get()) }
    single<GoalsRepository> { GoalsRepositoryImpl(get()) }
}
