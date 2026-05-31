package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberViewModel
import org.koin.core.module.dsl.viewModel
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.ui.viewmodels.ExploreFundsViewModel
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.ui.viewmodels.HomeScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.OnboardingViewModel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FixedDepositsViewModel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdViewModel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FdDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.SetInvestmentDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCFormScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCImageUploaderScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KycContractViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.AllBundlesViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.BundleResultViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.CartScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.CategoryMutualFundViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.MutualFundDetailsScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.MutualFundSearchResultViewModel
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountViewModel

val viewModelModule = module {

    viewModel { ChooseLanguageViewModel(get()) }
    viewModel { LoginWithPhoneNumberViewModel(get()) }
    viewModel { EnterOtpViewModel(get()) }

    viewModel { OnboardingViewModel(get()) }

    viewModel { FixedDepositsViewModel(get()) }
    viewModel { ExploreFdViewModel(get()) }
    viewModel { FdDetailsViewModel(get(), get()) }
    viewModel { SetInvestmentDetailsViewModel(get(), get()) }
    viewModel { KYCScreenViewModel(get(), get()) }
    viewModel { KYCFormScreenViewModel(get(), get(),get()) }
    viewModel { KYCImageUploaderScreenViewModel(get(), get(), get()) }
    viewModel { KycContractViewModel(get(), get(), get()) }
    viewModel { TradingAccountViewModel(get(), get(), get(), get(), get()) }

    viewModel { AllBundlesViewModel(get()) }
    viewModel { (bundleKey: String) -> BundleResultViewModel(bundleKey, get(), get(), get()) }
    viewModel { CartScreenViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { CategoryMutualFundViewModel(get()) }
    viewModel { (id: String) -> MutualFundDetailsScreenViewModel(id, get(), get(), get(), get(), get()) }
    viewModel { (search: String?, fundCategory: String?) ->
        MutualFundSearchResultViewModel(search, fundCategory, get())
    }

    viewModel { HomeScreenViewModel(get()) }
    viewModel { ExploreFundsViewModel(get()) }
}
