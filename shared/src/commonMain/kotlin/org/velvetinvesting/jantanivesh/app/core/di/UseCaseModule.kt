package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.GetUserDataUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetFDDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetFixedDepositsSearchResultUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetTopPickFDUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.PurchaseFDUseCase
import org.velvetinvesting.jantanivesh.app.features.login.domain.usecases.LoginWithNumberUseCase
import org.velvetinvesting.jantanivesh.app.features.login.domain.usecases.VerifyOTPUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.AddBundleToCartLumpsumUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.AddBundleToCartSipUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.AddToCartLumpsumUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.AddToCartSipUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.CheckSipPurchaseStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.ClearCartUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.DeleteCartItemUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetAllBundledFundsUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetBundleFundsUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetCategoryMutualFundsUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetCombinedCategoryMutualFundsUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetMutualFundDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetMutualFundGraphUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetMutualFundSearchResultUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetMutualFundTopPicksUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.GetUserCartUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.InitiateSipPurchaseUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.PurchaseLumpsumFundUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.PurchaseSipFundUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.RedeemFullFundUseCase
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases.RedeemPartialFundUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.ConfirmMandateUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.CreateMandateUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.GetKycFormStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.GetPANVerificationStatusUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.InitiateKycFormUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.InitiatePANVerificationUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.RequestEmailOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SkipPANVerificationUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SubmitBasicDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SubmitInvestorProfileUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SubmitNomineesUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.SubmitPennyDropUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.UploadKycFormSignatureUseCase
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.VerifyEmailOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.CreatePurchasePlanUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetPurchasePlanUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetPurchasePlansUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.GetSchemePlansUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.RequestPurchasePlanOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases.VerifyPurchasePlanOtpUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.CancelLumpSumOrderUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.CancelSipOrderUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.DownloadPdfByUrlUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportCapitalReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportPortfolioReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportSoaReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.ExportTaxReportUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetFDPortfolioByIdUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetFDRedirectUrlUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetFolioFundsUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetPendingOrdersUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.GetPortfolioUseCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases.InvestMoreLumpsumUseCase

val useCaseModule = module {
    factory { LoginWithNumberUseCase(get()) }
    factory { VerifyOTPUseCase(get()) }
    factory { GetFDDetailsUseCase(get()) }
    factory { GetFixedDepositsSearchResultUseCase(get()) }
    factory { PurchaseFDUseCase(get()) }
    factory { GetTopPickFDUseCase(get()) }
    factory { GetUserDataUseCase(get()) }

    // New Onboarding UseCases
    factory { SubmitBasicDetailsUseCase(get()) }
    factory { InitiatePANVerificationUseCase(get()) }
    factory { SkipPANVerificationUseCase(get()) }
    factory { GetPANVerificationStatusUseCase(get()) }
    factory { InitiateKycFormUseCase(get()) }
    factory { GetKycFormStatusUseCase(get()) }
    factory { UploadKycFormSignatureUseCase(get()) }
    factory { SubmitPennyDropUseCase(get()) }
    factory { RequestEmailOtpUseCase(get()) }
    factory { VerifyEmailOtpUseCase(get()) }
    factory { SubmitInvestorProfileUseCase(get()) }
    factory { SubmitNomineesUseCase(get()) }
    factory { CreateMandateUseCase(get()) }
    factory { ConfirmMandateUseCase(get()) }

    // Plans UseCases
    factory { GetSchemePlansUseCase(get()) }
    factory { GetPurchasePlansUseCase(get()) }
    factory { CreatePurchasePlanUseCase(get()) }
    factory { GetPurchasePlanUseCase(get()) }
    factory { RequestPurchasePlanOtpUseCase(get()) }
    factory { VerifyPurchasePlanOtpUseCase(get()) }

    // Mutual Fund UseCases
    factory { AddToCartSipUseCase(get()) }
    factory { AddToCartLumpsumUseCase(get()) }
    factory { AddBundleToCartSipUseCase(get()) }
    factory { AddBundleToCartLumpsumUseCase(get()) }
    factory { CheckSipPurchaseStatusUseCase(get()) }
    factory { ClearCartUseCase(get()) }
    factory { DeleteCartItemUseCase(get()) }
    factory { GetAllBundledFundsUseCase(get()) }
    factory { GetBundleFundsUseCase(get()) }
    factory { GetCategoryMutualFundsUseCase(get()) }
    factory { GetCombinedCategoryMutualFundsUseCase(get()) }
    factory { GetMutualFundDetailsUseCase(get()) }
    factory { GetMutualFundGraphUseCase(get()) }
    factory { GetMutualFundSearchResultUseCase(get()) }
    factory { GetMutualFundTopPicksUseCase(get()) }
    factory { GetUserCartUseCase(get()) }
    factory { InitiateSipPurchaseUseCase(get()) }
    factory { PurchaseLumpsumFundUseCase(get()) }
    factory { PurchaseSipFundUseCase(get()) }
    factory { RedeemFullFundUseCase(get()) }
    factory { RedeemPartialFundUseCase(get()) }

    // Goals UseCases
    factory { GetPortfolioUseCase(get()) }

    // Portfolio UseCases
    factory { GetPortfolioUseCase(get()) }
    factory { GetFolioFundsUseCase(get()) }
    factory { CancelSipOrderUseCase(get()) }
    factory { ExportSoaReportUseCase(get()) }
    factory { ExportTaxReportUseCase(get()) }
    factory { DownloadPdfByUrlUseCase(get()) }
    factory { GetFDRedirectUrlUseCase(get()) }
    factory { GetPendingOrdersUseCase(get()) }
    factory { InvestMoreLumpsumUseCase(get()) }
    factory { CancelLumpSumOrderUseCase(get()) }
    factory { GetFDPortfolioByIdUseCase(get()) }
    factory { ExportCapitalReportUseCase(get()) }
    factory { ExportPortfolioReportUseCase(get()) }
}
