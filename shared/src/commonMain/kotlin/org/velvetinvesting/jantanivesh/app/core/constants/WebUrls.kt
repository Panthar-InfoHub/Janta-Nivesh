package org.velvetinvesting.jantanivesh.app.core.constants

/**
 * The marketing-site pages the app opens in a browser.
 *
 * These are public pages on jantanivesh.com rather than API endpoints, so they are kept here
 * instead of in the networking config: nothing about them changes between environments, and the
 * navigation graph should not be the place a domain name is spelled out.
 */
object WebUrls {

    private const val BASE = "https://www.jantanivesh.com"

    /** Reached from Profile → Contact Us. */
    const val CONTACT = "$BASE/contact"

    /** Reached from Profile → Help & FAQ. */
    const val FAQS = "$BASE/faqs"

    /** Reached from Profile Settings → Delete Account; the request is filed on the web. */
    const val DATA_DELETION = "$BASE/data-deletion"
}
