package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.LoginWithPhoneNumberViewModel
import org.koin.core.module.dsl.viewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ChooseLanguageViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterNameFromPanViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterOtpViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBViewModel

val viewModelModule = module {
    viewModel { LoginWithPhoneNumberViewModel() }
    viewModel { EnterYourDOBViewModel() }
    viewModel { AddYourEmailViewModel() }
    viewModel { ChooseLanguageViewModel() }
    viewModel { EnterNameFromPanViewModel() }
    viewModel { EnterOtpViewModel() }
}