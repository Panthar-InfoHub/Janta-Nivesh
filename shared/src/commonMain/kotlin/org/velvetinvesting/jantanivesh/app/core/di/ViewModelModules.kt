package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberViewModel
import org.koin.core.module.dsl.viewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.OnboardingViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCFormScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCImageUploaderScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KycContractViewModel

val viewModelModule = module {

    viewModel { ChooseLanguageViewModel(get()) }
    viewModel { LoginWithPhoneNumberViewModel(get()) }
    viewModel { EnterOtpViewModel(get()) }

    viewModel { OnboardingViewModel(get()) }
    viewModel { KYCScreenViewModel(get(), get()) }
    viewModel { KYCFormScreenViewModel(get(), get()) }
    viewModel { KYCImageUploaderScreenViewModel(get(), get(), get()) }
    viewModel { KycContractViewModel(get(), get(), get()) }
}
