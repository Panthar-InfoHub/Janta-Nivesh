package org.velvetinvesting.jantanivesh.app.features.onboarding.data.model

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.PrefilledBankDetails
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.ReversePennyDropLinks
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.ReversePennyDropStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.UpiApp

/** `GET /onboarding/reverse-penny/prefill/` — bank details a reverse penny drop has already read. */
@Serializable
data class ReversePennyPrefillResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: ReversePennyPrefillDataDto? = null
)

@Serializable
data class ReversePennyPrefillDataDto(
    val has_prefilled: Boolean = false,
    val account_number: String? = null,
    val ifsc_code: String? = null,
    val account_holder_name: String? = null,
    val account_type: String? = null,
    val payer_vpa: String? = null,
    val bank_name: String? = null,
    val bank_reference_number: String? = null,
    val source: String? = null
)

/**
 * Null when there is nothing to prefill — either the server said so outright or the call came
 * back unsuccessful in its body. Both mean the same to the caller: run the reverse penny drop.
 */
fun ReversePennyPrefillResponseDto.toDomain(): PrefilledBankDetails? {
    val details = data?.takeIf { success && it.has_prefilled } ?: return null
    return PrefilledBankDetails(
        accountNumber = details.account_number.orEmpty(),
        ifscCode = details.ifsc_code.orEmpty(),
        accountHolderName = details.account_holder_name.orEmpty(),
        accountType = details.account_type.orEmpty(),
        bankName = details.bank_name.orEmpty()
    )
}

/** `POST /onboarding/reverse-penny/initiate` — the ₹1 payment links, one per UPI app. */
@Serializable
data class ReversePennyInitiateResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: ReversePennyInitiateDataDto? = null
)

@Serializable
data class ReversePennyInitiateDataDto(
    val reference_id: String? = null,
    val decentro_txn_id: String? = null,
    val api_status: String? = null,
    val response_code: String? = null,
    val message: String? = null,
    val data: ReversePennyLinksDto? = null,
    val response_key: String? = null
)

@Serializable
data class ReversePennyLinksDto(
    val validation_link: String? = null,
    val gpay_uri: String? = null,
    val phonepe_uri: String? = null,
    val paytm_uri: String? = null,
    val cred_uri: String? = null,
    val bhim_uri: String? = null,
    val curie_uri: String? = null
)

/** Null when the response carries no usable link at all, so there is nothing to pay with. */
fun ReversePennyInitiateResponseDto.toDomain(): ReversePennyDropLinks? {
    val links = data?.data ?: return null
    val validationLink = links.validation_link?.takeIf { it.isNotBlank() }

    val appLinks = buildMap {
        links.gpay_uri.putIfPresent(UpiApp.GOOGLE_PAY, this)
        links.phonepe_uri.putIfPresent(UpiApp.PHONEPE, this)
        links.paytm_uri.putIfPresent(UpiApp.PAYTM, this)
        links.cred_uri.putIfPresent(UpiApp.CRED, this)
        links.bhim_uri.putIfPresent(UpiApp.BHIM, this)
        // curie_uri is not offered: it has no app intent of its own, and the QR page covers
        // "any other app".
    }

    if (validationLink == null && appLinks.isEmpty()) return null
    return ReversePennyDropLinks(
        validationLink = validationLink,
        appLinks = appLinks
    )
}

private fun String?.putIfPresent(app: UpiApp, into: MutableMap<UpiApp, String>) {
    if (!isNullOrBlank()) into[app] = this
}

/** `GET /onboarding/reverse-penny/status/` — whether the ₹1 payment has come through. */
@Serializable
data class ReversePennyStatusResponseDto(
    val success: Boolean = false,
    val message: String? = null,
    val data: ReversePennyStatusDataDto? = null
)

@Serializable
data class ReversePennyStatusDataDto(
    val request_decentro_txn_id: String? = null,
    val transaction_status: String? = null,
    val transaction_status_description: String? = null
)

fun ReversePennyStatusResponseDto.toDomain() = ReversePennyDropStatus(
    status = data?.transaction_status,
    description = data?.transaction_status_description
)
