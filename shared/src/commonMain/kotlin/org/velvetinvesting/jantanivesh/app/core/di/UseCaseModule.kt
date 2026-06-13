package org.velvetinvesting.jantanivesh.app.core.di

import org.koin.dsl.module
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.GetUserDataUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.FinalizeKycUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.GetContractPdfUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.GetDigiLockerDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.GetESignUrlUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.InitiateKycUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.LinkKycDocumentsUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.UploadKycFormDataUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.UploadKycImageUseCase
import org.velvetinvesting.jantanivesh.app.features.kyc.domain.usecases.UploadKycSignatureUseCase
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
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.OnboardUserUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetFDDetailsUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetFixedDepositsSearchResultUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.GetTopPickFDUseCase
import org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases.PurchaseFDUseCase
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases.GetTradingAccountPrefilledDataUseCase
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases.SubmitTradingAccountFormUseCase
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases.TradingAccountConfirmationUseCase
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.usecases.VerifyPANUseCase

val useCaseModule = module {
    factory { LoginWithNumberUseCase(get()) }
    factory { VerifyOTPUseCase(get()) }
    factory { OnboardUserUseCase(get()) }

    factory { GetFDDetailsUseCase(get()) }
    factory { GetFixedDepositsSearchResultUseCase(get()) }
    factory { PurchaseFDUseCase(get()) }
    factory { GetTopPickFDUseCase(get()) }
    factory { GetUserDataUseCase(get()) }

    // KYC UseCases
    factory { InitiateKycUseCase(get()) }
    factory { GetDigiLockerDetailsUseCase(get()) }
    factory { UploadKycFormDataUseCase(get()) }
    factory { UploadKycImageUseCase(get()) }
    factory { UploadKycSignatureUseCase(get()) }
    factory { LinkKycDocumentsUseCase(get()) }
    factory { GetContractPdfUseCase(get()) }
    factory { GetESignUrlUseCase(get()) }
    factory { FinalizeKycUseCase(get()) }

    //Trading Account UseCases
    factory { GetTradingAccountPrefilledDataUseCase(get()) }
    factory { VerifyPANUseCase(get()) }
    factory { SubmitTradingAccountFormUseCase(get()) }
    factory { TradingAccountConfirmationUseCase(get()) }

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
}
