package org.velvetinvesting.jantanivesh.app.features.bundles.domain.models

import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.ReturnYearsRateDomain
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SchemePlan
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.SipThreshold
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** A fund picked on the explore screen, built from its list item and its scheme plan. */
class ExploredFundTest {

    @Test
    fun allThreeThresholds_mapToTheirMinimums() {
        val fund = listedFund.toBundleFund(plan())
        val rules = fund.transactionRules!!

        assertEquals(1000L, rules.minLumpSumAmount)
        assertEquals(500L, rules.minMonthlySipAmount)
        assertEquals(500L, rules.minSipAmount)
        assertEquals(100L, rules.minDailySipAmount)
        assertEquals(listOf(1, 5, 10), rules.sipAllowedDates)
        assertEquals(listOf("D", "OM"), rules.sipFrequencies)
        assertTrue(fund.sipAllowed == true)
    }

    @Test
    fun identityComesFromThePlan_returnsFromTheListItem() {
        val fund = listedFund.toBundleFund(plan())

        assertEquals("product-1", fund.id)
        assertEquals("product-1", fund.transactionRules!!.mfProductId)
        assertEquals("Parag Parikh Flexi Cap Fund - Direct Plan - Growth", fund.name)
        assertEquals("INF879O01027", fund.isin)
        assertEquals("PPFAS Mutual Fund", fund.amcName)
        assertEquals(15.2, fund.metrics.return1Y)
        assertEquals(21.7, fund.metrics.return3Y)
        assertEquals(19.2, fund.metrics.return5Y)
    }

    @Test
    fun aModeTheSchemeDoesNotOffer_hasNoMinimumAndIsUnsupported() {
        val fund = listedFund.toBundleFund(plan(dailySip = null))
        val rules = fund.transactionRules!!

        assertNull(rules.minDailySipAmount)
        assertFalse(fund.supports(PurchaseMode.DAILY))
        assertTrue(fund.supports(PurchaseMode.MONTHLY))
        assertTrue(fund.supports(PurchaseMode.ONE_TIME))
        assertEquals(listOf("OM"), rules.sipFrequencies)
        assertTrue(fund.sipAllowed == true)
    }

    @Test
    fun aSchemeWithNoSip_isNotSipAllowed() {
        val fund = listedFund.toBundleFund(plan(monthlySip = null, dailySip = null))

        val rules = fund.transactionRules!!
        assertFalse(fund.sipAllowed == true)
        assertEquals(emptyList(), rules.sipFrequencies)
        assertEquals(1000L, rules.minLumpSumAmount)
        assertNull(rules.minMonthlySipAmount)
        assertNull(rules.minDailySipAmount)
    }

    /**
     * In a 25% slot each minimum is scaled up to the bundle amount that still covers it, then
     * rounded up to the next ₹10: ₹100/day → ₹400, ₹500/month → ₹2,000, ₹1,000 once → ₹4,000.
     */
    @Test
    fun anExploredFundInASlot_drivesAllThreeBundleMinimums() {
        val fund = listedFund.toBundleFund(plan())
        val rules = bundleWith(fund, slotPercentage = 25.0).deriveTransactionRules()

        assertEquals(400, rules.minDailySipAmount)
        assertEquals(2000, rules.minMonthlySipAmount)
        assertEquals(2000, rules.minBundleSipAmount)
        assertEquals(4000, rules.minBundleLumpsumAmount)
        assertEquals(listOf(1, 5, 10), rules.sipAllowedDates)
        assertEquals(listOf("D", "OM"), rules.sipFrequencies)
    }

    private val listedFund = MutualFundDomain(
        id = "list-1",
        name = "PARAG PARIKH FLEXI CAP FUND - DIRECT PLAN - GROWTH",
        icon = "",
        category = "Flexi-cap Fund",
        riskText = "Very High",
        type = "Equity",
        isin = "INF879O01027",
        returnYearsRate = ReturnYearsRateDomain(
            month3 = 4.8, month6 = 9.6, year1 = 15.2, year3 = 21.7, year5 = 19.2
        )
    )

    private fun threshold(min: Int, dates: List<Int> = emptyList()) = SipThreshold(
        amountMin = min,
        amountMax = 10_000_000,
        amountMultiples = 1,
        installmentsMin = 1,
        dates = dates
    )

    private fun plan(
        monthlySip: SipThreshold? = threshold(500, dates = listOf(1, 5, 10)),
        dailySip: SipThreshold? = threshold(100),
        lumpsum: SipThreshold? = threshold(1000)
    ) = SchemePlan(
        id = "product-1",
        isin = "INF879O01027",
        schemeName = "Parag Parikh Flexi Cap Fund - Direct Plan - Growth",
        fundName = "PPFAS Mutual Fund",
        option = "Growth",
        monthlySip = monthlySip,
        dailySip = dailySip,
        lumpsum = lumpsum
    )

    private fun bundleWith(fund: FundDomain, slotPercentage: Double) = BundleDetailsDomain(
        name = "Test",
        description = "",
        assetAllocation = AssetAllocationDomain(equity = 100.0, debt = 0.0, hybrid = 0.0, commodity = 0.0),
        metaData = BundleMetaDataDomain(riskLevel = "", investmentTime = "", investmentGrowth = ""),
        categories = listOf(
            BundleCategoryDomain(
                id = "flexi",
                categoryName = "flexi_cap",
                displayName = "Flexi Cap",
                allocationPercentage = slotPercentage,
                funds = listOf(fund),
                slots = listOf(PortfolioSlotDomain("slot1", slotPercentage, 1, fund, fund))
            )
        )
    )
}
