package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.ExploreFundsViewModel
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.HomeScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdViewModel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FdDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.SetInvestmentDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.AddGoalViewModel
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectedImpactViewModel
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectionImpactViewModel
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.YourGoalsViewModel
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.InsuranceViewModel
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.RequestCallbackViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.AllBundlesViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.BundleResultViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.CartScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.CategoryMutualFundViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.MutualFundDetailsScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.MutualFundSearchResultViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddNomineeViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.BasicDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EmailIdViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EmailOtpViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.KycSplashViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.PanVerificationViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ReviewProfileViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.SetupAutopayViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.UploadSignatureViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyBankAccountViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyWithDigilockerViewModel
import org.velvetinvesting.jantanivesh.app.features.search.ui.viewmodels.SearchOverlayViewModel
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.FundPurchaseViewModel
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.RedeemViewModel
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.ExistingFundsLumpSumViewModel
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.FDPortFolioDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.FolioFundsMFViewModel
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.MFPortfolioDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.PortfolioScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.NotificationViewModel
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileLanguageViewModel
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileSettingViewModel
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileViewModel
import org.velvetinvesting.jantanivesh.app.features.splashscreen.ui.viewmodels.SplashScreenViewModel

val viewModelModule = module {

    viewModel { ChooseLanguageViewModel(get()) }
    viewModel { LoginWithPhoneNumberViewModel(get()) }
    viewModel { EnterOtpViewModel(get(),get()) }

    viewModel { SplashScreenViewModel() }

    viewModel {(id:String)-> FdDetailsViewModel(id ,get()) }
    viewModel { ExploreFdViewModel(get()) }
    viewModel { (id:String)-> SetInvestmentDetailsViewModel(id, get(), get()) }


    // KycNew ViewModels
    viewModel { BasicDetailsViewModel(get(), get()) }
    viewModel { PanVerificationViewModel(get(), get(), get()) }
    viewModel { EmailIdViewModel(get(), get()) }
    viewModel { (email: String) -> EmailOtpViewModel(email, get(), get(), get()) }
    viewModel { KycSplashViewModel() }
    viewModel { VerifyBankAccountViewModel(get()) }
    viewModel { VerifyWithDigilockerViewModel(get(), get()) }
    viewModel { UploadSignatureViewModel(get()) }
    viewModel { ReviewProfileViewModel(get(), get(), get(), get()) }
    viewModel { AddNomineeViewModel(get()) }
    viewModel { SetupAutopayViewModel(get(), get()) }

    viewModel { SearchOverlayViewModel(get(), get(), get(), get()) }

    // Plans ViewModels
    viewModel { (mfProductId: String, isin: String, fundName: String, fundSubtitle: String) ->
        FundPurchaseViewModel(
            mfProductId = mfProductId,
            isin = isin,
            fundName = fundName,
            fundSubtitle = fundSubtitle,
            getSchemePlan = get(),
            getMandates = get(),
            createSipPlan = get(),
            getPurchasePlan = get(),
            requestPurchasePlanOtp = get(),
            verifyPurchasePlanOtp = get(),
            createMfPurchase = get(),
            getMfPurchase = get(),
            requestMfPurchaseOtp = get(),
            verifyMfPurchaseOtp = get()
        )
    }
    viewModel { RedeemViewModel(get()) }

    viewModel { AllBundlesViewModel(get()) }
    viewModel { (bundleKey: String) -> BundleResultViewModel(bundleKey, get(), get(), get()) }
    viewModel { CartScreenViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { CategoryMutualFundViewModel(get()) }
    viewModel { (id: String) -> MutualFundDetailsScreenViewModel(id, get(), get(), get(), get(), get(),get()) }
    viewModel { (search: String?, fundCategory: String?) ->
        MutualFundSearchResultViewModel(search, fundCategory, get())
    }

    viewModel { HomeScreenViewModel(get()) }
    viewModel { ExploreFundsViewModel(get(),get()) }
    viewModel { ProfileLanguageViewModel(get()) }
    viewModel { ProfileSettingViewModel() }
    viewModel { ProfileViewModel(get()) }
    viewModel { InsuranceViewModel() }
    viewModel { RequestCallbackViewModel() }

    viewModel { YourGoalsViewModel(get()) }
    viewModel { AddGoalViewModel(get(), get()) }
    viewModel { (id: String) -> ProjectedImpactViewModel(id, get(), get()) }
    viewModel { (id: String) -> ProjectionImpactViewModel(get(), get(), get(), id) }

    // Portfolio ViewModels
    viewModel { (folioId: String) -> FolioFundsMFViewModel(folioId, get()) }
    viewModel { PortfolioScreenViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { (fdId: String) -> FDPortFolioDetailsViewModel(get(), get(), fdId) }
    viewModel { MFPortfolioDetailsViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { ExistingFundsLumpSumViewModel(get(), get()) }
    viewModel { NotificationViewModel(get(),get()) }
}
