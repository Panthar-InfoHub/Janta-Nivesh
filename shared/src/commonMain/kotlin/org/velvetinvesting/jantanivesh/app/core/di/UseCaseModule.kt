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
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.usecases.OnboardUserUseCase

val useCaseModule = module {
    factory { LoginWithNumberUseCase(get()) }
    factory { VerifyOTPUseCase(get()) }
    factory { OnboardUserUseCase(get()) }
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
}
