package org.velvetinvesting.jantanivesh.app.features.onboarding.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.PennyDropStatus

/**
 * `GET /onboarding/pan-verification/status`, read for the penny drop verdict. The endpoint reports
 * the whole readiness record; only [PennyDropStatusDataDto.bank_accounts] matters here, and the
 * rest is left off the DTO rather than modelled unused.
 */
@Serializable
data class PennyDropStatusResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: PennyDropStatusDataDto = PennyDropStatusDataDto()
)

@Serializable
data class PennyDropStatusDataDto(
    val status: String? = null,
    val is_processing_complete: Boolean = false,
    val bank_accounts: List<BankAccountStatusDto> = emptyList(),
    val onboarding: OnboardingDto? = null
)

@Serializable
data class BankAccountStatusDto(
    val code: String? = null,
    val reason: String? = null,
    val status: String? = null,
    val value: BankAccountValueDto? = null
)

@Serializable
data class BankAccountValueDto(
    val account_number: String? = null,
    val ifsc_code: String? = null,
    val account_type: String? = null
)

/**
 * The verdict for one account. The server returns every account it holds, so the one just
 * submitted is picked out by number; an account it has not registered yet reads as still pending.
 */
fun PennyDropStatusResponseDto.toDomain(accountNumber: String): PennyDropStatus {
    val account = data.bank_accounts.firstOrNull {
        it.value?.account_number == accountNumber
    }
    return PennyDropStatus(
        status = account?.status,
        reason = account?.reason
    )
}
