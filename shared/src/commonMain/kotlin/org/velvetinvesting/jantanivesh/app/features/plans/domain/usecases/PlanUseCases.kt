package org.velvetinvesting.jantanivesh.app.features.plans.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.repository.PlansRepo

class GetSchemePlansUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(): NetworkResponse<List<SchemePlan>, ErrorDomain> {
        return plansRepo.getSchemePlans()
    }
}

class GetPurchasePlansUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(): NetworkResponse<List<PurchasePlan>, ErrorDomain> {
        return plansRepo.getPurchasePlans()
    }
}

class CreatePurchasePlanUseCase(
    private val plansRepo: PlansRepo
) {
    suspend operator fun invoke(
        scheme: String,
        amount: Int,
        installmentDay: Int,
        frequency: String = MONTHLY,
        folioNumber: String = NEW_FOLIO
    ): NetworkResponse<PurchasePlan, ErrorDomain> {
        return plansRepo.createPurchasePlan(
            scheme = scheme,
            amount = amount,
            frequency = frequency,
            installmentDay = installmentDay,
            folioNumber = folioNumber
        )
    }

    companion object {
        const val MONTHLY = "monthly"

        /** A blank folio tells the gateway this is a fresh purchase. */
        const val NEW_FOLIO = ""
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
