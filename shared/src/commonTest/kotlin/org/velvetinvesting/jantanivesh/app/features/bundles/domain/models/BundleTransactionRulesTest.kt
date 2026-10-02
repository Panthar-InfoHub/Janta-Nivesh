package org.velvetinvesting.jantanivesh.app.features.bundles.domain.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode

/** The bundle minimums, worked out again from whichever funds are selected. */
class BundleTransactionRulesTest {

    /**
     * Each minimum is the highest of (fund minimum ÷ slot share), rounded up to ₹10. Figures are
     * lumpsum / monthly / daily.
     * Slot A (60%): ₹500 / ₹100 / ₹20 → ₹840 / ₹170 / ₹40.
     * Slot B (40%): ₹500 / ₹100 / ₹20 → ₹1,250 / ₹250 / ₹50.
     */
    @Test
    fun preSelectedFunds_setTheStartingMinimums() {
        val rules = bundle(slotA = fundA, slotB = fundB).deriveTransactionRules()

        assertEquals(1250, rules.minBundleLumpsumAmount)
        assertEquals(250, rules.minMonthlySipAmount)
        assertEquals(50, rules.minDailySipAmount)
    }

    /** Swapping slot A's fund for one with higher minimums raises every bundle minimum. */
    @Test
    fun swappingAFund_recalculatesAllThreeMinimums() {
        val pricier = fund("pricier", lumpsum = 5000, monthly = 1000, daily = 100)
        val rules = bundle(slotA = pricier, slotB = fundB).deriveTransactionRules()

        // ₹5,000 ÷ 60% = ₹8,333.4 → ₹8,340; ₹1,000 ÷ 60% → ₹1,670; ₹100 ÷ 60% → ₹170.
        assertEquals(8340, rules.minBundleLumpsumAmount)
        assertEquals(1670, rules.minMonthlySipAmount)
        assertEquals(170, rules.minDailySipAmount)
    }

    /** A cheaper fund in place of the one setting the minimum lowers it to the next highest slot. */
    @Test
    fun swappingTheDecidingFund_forACheaperOne_lowersTheMinimum() {
        val cheaper = fund("cheaper", lumpsum = 100, monthly = 50, daily = 10)
        val rules = bundle(slotA = fundA, slotB = cheaper).deriveTransactionRules()

        // Slot B now needs ₹250 / ₹130 / ₹30, so slot A's ₹840 / ₹170 / ₹40 decide.
        assertEquals(840, rules.minBundleLumpsumAmount)
        assertEquals(170, rules.minMonthlySipAmount)
        assertEquals(40, rules.minDailySipAmount)
    }

    /**
     * One fund without a daily minimum leaves the bundle with no daily minimum at all — not one
     * worked out from the other funds — while the other two ways still have theirs.
     */
    @Test
    fun aFundWithoutADailyMinimum_leavesTheBundleWithoutOne() {
        val noDaily = fund("no-daily", lumpsum = 500, monthly = 100, daily = null)
        val rules = bundle(slotA = noDaily, slotB = fundB).deriveTransactionRules()

        assertNull(rules.minDailySipAmount)
        assertNull(rules.minAmountFor(PurchaseMode.DAILY))
        assertEquals(250, rules.minAmountFor(PurchaseMode.MONTHLY))
        assertEquals(1250, rules.minAmountFor(PurchaseMode.ONE_TIME))
    }

    /** A fund that came with no limits at all can't be bought any way. */
    @Test
    fun aFundWithNoRules_leavesTheBundleWithoutAnyMinimum() {
        val noRules = fundA.copy(id = "no-rules", transactionRules = null)
        val rules = bundle(slotA = noRules, slotB = fundB).deriveTransactionRules()

        assertNull(rules.minAmountFor(PurchaseMode.DAILY))
        assertNull(rules.minAmountFor(PurchaseMode.MONTHLY))
        assertNull(rules.minAmountFor(PurchaseMode.ONE_TIME))
    }

    private val fundA = fund("a", lumpsum = 500, monthly = 100, daily = 20)
    private val fundB = fund("b", lumpsum = 500, monthly = 100, daily = 20)

    private fun fund(id: String, lumpsum: Long?, monthly: Long?, daily: Long?) = FundDomain(
        id = id,
        name = id,
        isin = "",
        imageUrl = "",
        latestNav = "",
        latestNavDate = "",
        metrics = FundMetricsDomain(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        transactionRules = TransactionRulesDomain(
            id = "",
            mfProductId = id,
            minSipAmount = monthly,
            minLumpSumAmount = lumpsum,
            minInvestmentAmount = 0,
            minLumpsumAddOnAmount = 0,
            minRedemptionQty = 0,
            minRedemptionAmount = 0,
            minDailySipAmount = daily,
            minWeeklySipAmount = 0,
            minFortnightlySipAmount = 0,
            minMonthlySipAmount = monthly,
            minQuarterlySipAmount = 0,
            minSemiAnnualSipAmount = 0,
            minAnnualSipAmount = 0,
            sipAllowedDates = emptyList(),
            sipFrequencies = emptyList(),
            createdAt = "",
            updatedAt = ""
        )
    )

    private fun bundle(slotA: FundDomain, slotB: FundDomain) = BundleDetailsDomain(
        name = "Test",
        description = "",
        assetAllocation = AssetAllocationDomain(equity = 100.0, debt = 0.0, hybrid = 0.0, commodity = 0.0),
        metaData = BundleMetaDataDomain(riskLevel = "", investmentTime = "", investmentGrowth = ""),
        categories = listOf(
            BundleCategoryDomain(
                id = "cat",
                categoryName = "flexi_cap",
                displayName = "Flexi Cap",
                allocationPercentage = 100.0,
                funds = listOf(fundA, fundB),
                slots = listOf(
                    PortfolioSlotDomain("a", 60.0, 1, fundA, slotA),
                    PortfolioSlotDomain("b", 40.0, 2, fundB, slotB)
                )
            )
        )
    )
}
