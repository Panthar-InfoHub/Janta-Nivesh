package org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MandateOption
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchase
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.MfPurchaseConfirmation
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.repository.PlansRepo

class GetPurchasePlansUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(): NetworkResponse<List<PurchasePlan>, ErrorDomain> {
        return plansRepo.getPurchasePlans()
    }
}

class GetPurchasePlanUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(planId: String): NetworkResponse<PurchasePlan, ErrorDomain> {
        return plansRepo.getPurchasePlan(planId)
    }
}

class RequestPurchasePlanOtpUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(planId: String): NetworkResponse<Unit, ErrorDomain> {
        return plansRepo.requestPurchasePlanOtp(planId)
    }
}

class VerifyPurchasePlanOtpUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(
        planId: String,
        otp: String
    ): NetworkResponse<PurchasePlan, ErrorDomain> {
        return plansRepo.verifyPurchasePlanOtp(planId, otp)
    }
}

class GetSchemePlanUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(isin: String): NetworkResponse<SchemePlan, ErrorDomain> {
        return plansRepo.getSchemePlan(isin)
    }
}

/**
 * Starts a SIP on a product id. Pass [installmentDay] for a monthly SIP and leave it null for a
 * daily one — the repository picks the matching request body from that.
 */
class CreateSipPlanUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(
        mfProductId: String,
        amount: Int,
        frequency: String,
        installmentDay: Int?,
        folioNumber: String = NEW_FOLIO
    ): NetworkResponse<PurchasePlan, ErrorDomain> {
        return plansRepo.createSipPlan(
            mfProductId = mfProductId,
            amount = amount,
            frequency = frequency,
            installmentDay = installmentDay,
            folioNumber = folioNumber
        )
    }

    companion object {
        /** A blank folio tells the gateway this is a fresh purchase. */
        const val NEW_FOLIO = ""
    }
}

class CreateMfPurchaseUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(
        mfProductId: String,
        amount: Int,
        folioNumber: String = CreateSipPlanUseCase.NEW_FOLIO
    ): NetworkResponse<MfPurchase, ErrorDomain> {
        return plansRepo.createMfPurchase(
            mfProductId = mfProductId,
            amount = amount,
            folioNumber = folioNumber
        )
    }
}

class GetMfPurchaseUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(purchaseId: String): NetworkResponse<MfPurchase, ErrorDomain> {
        return plansRepo.getMfPurchase(purchaseId)
    }
}

class RequestMfPurchaseOtpUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(purchaseId: String): NetworkResponse<Unit, ErrorDomain> {
        return plansRepo.requestMfPurchaseOtp(purchaseId)
    }
}

class VerifyMfPurchaseOtpUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(
        purchaseId: String,
        otp: String
    ): NetworkResponse<MfPurchaseConfirmation, ErrorDomain> {
        return plansRepo.verifyMfPurchaseOtp(purchaseId, otp)
    }
}

class GetMandatesUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(): NetworkResponse<List<MandateOption>, ErrorDomain> {
        return plansRepo.getMandates()
    }
}
