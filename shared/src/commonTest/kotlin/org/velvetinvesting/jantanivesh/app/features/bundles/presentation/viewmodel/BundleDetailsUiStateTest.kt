package org.velvetinvesting.jantanivesh.app.features.bundles.presentation.viewmodel

import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.AssetAllocationDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleCategoryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleMetaDataDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.FundDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.FundMetricsDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.PortfolioSlotDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.TransactionRulesDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.deriveTransactionRules
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Switching to a way of investing that a selected fund doesn't offer: the minimum reads 0, the
 * fund is flagged, and the bundle can't be bought until it is changed.
 */
class BundleDetailsUiStateTest {

    @Test
    fun everyFundOffersTheMode_minimumIsWorkedOutAndInvestingIsAllowed() {
        val state = stateWith(PurchaseMode.MONTHLY, investmentAmount = 1000L)

        assertTrue(state.isPurchaseModeSupported)
        assertTrue(state.unsupportedSlots.isEmpty())
        assertEquals(250L, state.minAmount)
        // Monthly needs a debit day before investing.
        assertFalse(state.canInvest)
        assertTrue(state.copy(selectedSipDay = 5).canInvest)
    }

    @Test
    fun aFundWithoutTheMode_minimumIsZeroAndInvestingIsBlocked() {
        val state = stateWith(PurchaseMode.DAILY, investmentAmount = 1000L)

        assertFalse(state.isPurchaseModeSupported)
        assertEquals(listOf("No Daily Fund"), state.unsupportedSlots.map { it.selectedFund?.name })
        assertEquals(0L, state.minAmount)
        assertFalse(state.canInvest)
        assertEquals(listOf("No Daily Fund"), state.unsupportedFundNames(state.bundle!!.categories.single()))
    }

    @Test
    fun exploredFundsThatCantBeBoughtThisWay_areUnselectable() {
        val state = stateWith(PurchaseMode.DAILY, investmentAmount = 0L).copy(
            exploredFunds = mapOf("listed-1" to noDaily, "listed-2" to withDaily),
            exploreUnavailableIds = setOf("listed-3")
        )

        assertEquals(setOf("listed-1", "listed-3"), state.exploreUnselectableFundIds())
        assertEquals(setOf("listed-3"), state.copy(purchaseMode = PurchaseMode.MONTHLY).exploreUnselectableFundIds())
    }

    private val withDaily = fund("with-daily", "With Daily Fund", daily = 20)
    private val noDaily = fund("no-daily", "No Daily Fund", daily = null)

    private fun stateWith(mode: PurchaseMode, investmentAmount: Long): BundleDetailsUiState {
        val bundle = BundleDetailsDomain(
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
                    funds = listOf(withDaily, noDaily),
                    slots = listOf(
                        PortfolioSlotDomain("a", 60.0, 1, withDaily, withDaily),
                        PortfolioSlotDomain("b", 40.0, 2, noDaily, noDaily)
                    )
                )
            )
        )
        return BundleDetailsUiState(
            bundle = bundle,
            transactionRules = bundle.deriveTransactionRules(),
            purchaseMode = mode,
            investmentAmount = investmentAmount
        )
    }

    private fun fund(id: String, name: String, daily: Long?) = FundDomain(
        id = id,
        name = name,
        isin = "",
        imageUrl = "",
        latestNav = "",
        latestNavDate = "",
        metrics = FundMetricsDomain(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        transactionRules = TransactionRulesDomain(
            id = "",
            mfProductId = id,
            minSipAmount = 100,
            minLumpSumAmount = 500,
            minInvestmentAmount = 0,
            minLumpsumAddOnAmount = 0,
            minRedemptionQty = 0,
            minRedemptionAmount = 0,
            minDailySipAmount = daily,
            minWeeklySipAmount = 0,
            minFortnightlySipAmount = 0,
            minMonthlySipAmount = 100,
            minQuarterlySipAmount = 0,
            minSemiAnnualSipAmount = 0,
            minAnnualSipAmount = 0,
            sipAllowedDates = emptyList(),
            sipFrequencies = emptyList(),
            createdAt = "",
            updatedAt = ""
        )
    )
}
