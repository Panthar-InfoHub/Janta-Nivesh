package org.velvetinvesting.jantanivesh.app.features.plans.data.source

/**
 * The schemes offered on the plan screen. There is no "list schemes" endpoint yet, so the
 * catalogue is fixed here and each entry is fetched by ISIN — replacing this with a listing call
 * is a change inside the repository only.
 */
object OfferedSchemes {
    val ISINS = listOf(
        "INF209K01RU9",
        "INF084M01093",
        "INF209KB12D6"
    )
}
