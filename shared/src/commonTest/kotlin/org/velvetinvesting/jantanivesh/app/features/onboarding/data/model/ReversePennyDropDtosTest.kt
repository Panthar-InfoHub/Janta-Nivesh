package org.velvetinvesting.jantanivesh.app.features.onboarding.data.model

import kotlinx.serialization.json.Json
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.ReversePennyDropStatus
import org.velvetinvesting.jantanivesh.app.features.onboarding.domain.model.UpiApp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ReversePennyDropDtosTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun prefillWithDetailsFillsEveryFieldAndBlanksTheNulls() {
        val dto = json.decodeFromString<ReversePennyPrefillResponseDto>(
            """
            {"success":true,"message":"Prefilled bank details fetched","data":{
              "has_prefilled":true,"account_number":"00000040255038106","ifsc_code":"SBIN0018946",
              "account_holder_name":"GAGAN DEEP  SINGH","account_type":"SAVINGS",
              "payer_vpa":"9026705339@ptyes","bank_name":null,
              "bank_reference_number":"315476060669","source":"REVERSE_PENNY"}}
            """
        )

        val details = dto.toDomain()!!
        assertEquals("00000040255038106", details.accountNumber)
        assertEquals("SBIN0018946", details.ifscCode)
        assertEquals("GAGAN DEEP  SINGH", details.accountHolderName)
        assertEquals("SAVINGS", details.accountType)
        assertEquals("", details.bankName)
    }

    @Test
    fun prefillWithoutDetailsOrUnsuccessfulReadsAsNothingToPrefill() {
        val notPrefilled = json.decodeFromString<ReversePennyPrefillResponseDto>(
            """{"success":true,"data":{"has_prefilled":false}}"""
        )
        val unsuccessful = json.decodeFromString<ReversePennyPrefillResponseDto>(
            """{"success":false,"data":{"has_prefilled":true,"account_number":"1"}}"""
        )

        assertNull(notPrefilled.toDomain())
        assertNull(unsuccessful.toDomain())
    }

    @Test
    fun initiateMapsEveryAppLink() {
        val dto = json.decodeFromString<ReversePennyInitiateResponseDto>(
            """
            {"success":true,"data":{"reference_id":"r","data":{
              "validation_link":"https://decpay.in/pay/link/rpd/x",
              "gpay_uri":"https://decpay.in/pay/link/rpd/x?app=gpay",
              "phonepe_uri":"https://decpay.in/pay/link/rpd/x?app=phonepe",
              "paytm_uri":"https://decpay.in/pay/link/rpd/x?app=paytm",
              "cred_uri":"https://decpay.in/pay/link/rpd/x?app=cred",
              "bhim_uri":"https://decpay.in/pay/link/rpd/x?app=bhim",
              "curie_uri":"https://decpay.in/pay/link/rpd/x?app=curie"}}}
            """
        )

        val links = dto.toDomain()!!
        assertEquals("https://decpay.in/pay/link/rpd/x", links.validationLink)
        assertEquals(UpiApp.entries.toSet(), links.appLinks.keys)
        assertEquals("https://decpay.in/pay/link/rpd/x?app=gpay", links.appLinks[UpiApp.GOOGLE_PAY])
        // Curie has no app of its own; the QR page stands in for it.
        assertFalse(links.appLinks.values.any { it.endsWith("app=curie") })
    }

    @Test
    fun initiateWithoutAnyLinkHasNothingToPayWith() {
        val dto = json.decodeFromString<ReversePennyInitiateResponseDto>(
            """{"success":true,"data":{"data":{"gpay_uri":""}}}"""
        )
        assertNull(dto.toDomain())
    }

    @Test
    fun statusClassification() {
        fun status(value: String?) = ReversePennyDropStatus(value, null)

        assertTrue(status("SUCCESS").isSuccess)
        assertTrue(status("PENDING").isPending)
        assertTrue(status(null).isPending)
        assertTrue(status("FAILED").isFailed)
        assertTrue(status("EXPIRED").isFailed)
        assertFalse(status("PENDING").isFailed)
    }
}
