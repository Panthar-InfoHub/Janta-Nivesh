package org.velvetinvesting.jantanivesh.app.core.deeplink

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExternalAppUrlTest {

    private val gpayMandate =
        "gpay://upi/mandate?pa=cybrillatechpl.bdsi@icici&pn=CYBRILLA&am=100.00&tn=Amount%20to%20be%20paid"

    @Test
    fun webUrlsStayInTheWebView() {
        listOf(
            "https://pay.example.com/checkout",
            "HTTP://example.com",
            "about:blank",
            "blob:https://example.com/1234",
            "data:text/html,hello",
            "javascript:void(0)",
            "/relative/path",
            "",
            null
        ).forEach { assertFalse(ExternalAppUrl.isExternalAppUrl(it), "$it") }
    }

    @Test
    fun appLinksLeaveTheWebView() {
        listOf(
            gpayMandate,
            "phonepe://pay?pa=x@ybl",
            "paytmmp://pay?pa=x@paytm",
            "upi://pay?pa=x@upi",
            "intent://upi/pay?pa=x#Intent;scheme=gpay;package=com.google.android.apps.nbu.paisa.user;end",
            "tel:18001234567"
        ).forEach { assertTrue(ExternalAppUrl.isExternalAppUrl(it), it) }
    }

    @Test
    fun notInstalledMessageNamesTheApp() {
        assertEquals(
            "Google Pay is not installed on this device. Please choose another payment option.",
            ExternalAppUrl.appNotInstalledMessage(gpayMandate)
        )
        assertEquals(
            "Google Pay is not installed on this device. Please choose another payment option.",
            ExternalAppUrl.appNotInstalledMessage(
                "intent://upi/pay#Intent;scheme=gpay;package=com.google.android.apps.nbu.paisa.user;end"
            )
        )
        assertEquals(
            "No UPI app found on this device. Please choose another payment option.",
            ExternalAppUrl.appNotInstalledMessage("upi://pay?pa=x")
        )
    }
}
