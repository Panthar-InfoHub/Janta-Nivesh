package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.features.login.domain.usecases.LoginWithNumberUseCase
import org.velvetinvesting.jantanivesh.app.features.login.domain.usecases.VerifyOTPUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.OnboardUserUseCase

val useCaseModule = module {
    factory { LoginWithNumberUseCase(get()) }
    factory { VerifyOTPUseCase(get()) }
    factory { OnboardUserUseCase(get()) }
}
