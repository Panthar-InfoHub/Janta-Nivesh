package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import kotlinx.coroutines.delay
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorType
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MfRedemption
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.RedemptionState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

/** How a redemption was sized. The endpoint accepts one or the other, never both. */
sealed interface RedemptionRequest {
    data class ByAmount(val amount: Double) : RedemptionRequest
    data class ByUnits(val units: Double) : RedemptionRequest
}

/**
 * Step 1: place the redemption. The [MfRedemption.id] it returns is the gateway's `fp_id`, which
 * every later step in the flow is keyed on.
 */
class CreateMfRedemptionUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(
        holdingId: String,
        request: RedemptionRequest
    ): NetworkResponse<MfRedemption, ErrorDomain> = when (request) {
        is RedemptionRequest.ByAmount ->
            repository.createRedemptionByAmount(holdingId, request.amount)

        is RedemptionRequest.ByUnits ->
            repository.createRedemptionByUnits(holdingId, request.units)
    }
}

/**
 * Waits for a redemption to reach a given state.
 *
 * Used twice in the flow: before the OTP, waiting for `PENDING` — a fresh redemption comes back
 * `UNDER_REVIEW` and the OTP endpoints reject it until it settles — and again after the code is
 * verified, waiting for `SUBMITTED`, which is the gateway confirming it reached the AMC.
 *
 * A transient read failure does not end the wait: the redemption itself is already placed, and
 * one bad response says nothing about its state, so errors are carried and only surfaced if
 * every attempt is used up.
 */
class AwaitMfRedemptionUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(
        redemptionId: String,
        awaiting: RedemptionState
    ): NetworkResponse<MfRedemption, ErrorDomain> {

        var lastError: ErrorDomain? = null

        repeat(MAX_ATTEMPTS) { attempt ->
            if (attempt > 0) delay(POLL_INTERVAL_MILLIS)

            when (val result = repository.getRedemption(redemptionId)) {
                is NetworkResponse.Error -> lastError = result.error

                is NetworkResponse.Success -> when {
                    result.data.state == awaiting -> return result

                    result.data.state == RedemptionState.FAILED -> return NetworkResponse.Error(
                        ErrorDomain(
                            code = -1,
                            message = result.data.failureReason
                                ?: "The redemption could not be placed. Please try again.",
                            type = ErrorType.SERVER
                        )
                    )

                    // Still moving through the gateway — keep waiting.
                    else -> Unit
                }
            }
        }

        return NetworkResponse.Error(
            lastError ?: ErrorDomain(
                code = -1,
                message = "The redemption is taking longer than usual. " +
                        "Check your portfolio in a few minutes.",
                type = ErrorType.UNKNOWN
            )
        )
    }

    private companion object {
        const val MAX_ATTEMPTS = 5
        const val POLL_INTERVAL_MILLIS = 10_000L
    }
}

/** Step 3: send the confirmation code. Also backs "resend" on the OTP screen. */
class RequestMfRedemptionOtpUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(redemptionId: String): NetworkResponse<Unit, ErrorDomain> =
        repository.requestRedemptionOtp(redemptionId)
}

/** Step 4: authorise it. Success is the end of the flow — the money is on its way back. */
class VerifyMfRedemptionOtpUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(
        redemptionId: String,
        otp: String
    ): NetworkResponse<Unit, ErrorDomain> = repository.verifyRedemptionOtp(redemptionId, otp)
}
