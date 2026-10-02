package org.velvetinvesting.jantanivesh.app.features.bundles.presentation.compose

import kotlin.test.Test
import kotlin.test.assertEquals

/** A share of the amount entered, as shown beside each asset class and category. */
class ShareAmountTextTest {

    @Test
    fun wholeRupees_haveNoDecimal() {
        assertEquals("₹4,750", shareAmountText(5000, 95.0))
        assertEquals("₹1,000", shareAmountText(5000, 20.0))
        assertEquals("₹0", shareAmountText(0, 30.0))
    }

    @Test
    fun aFraction_isRoundedToOneDecimal() {
        // ₹4,750 × 25% = ₹1,187.5
        assertEquals("₹1,187.5", shareAmountText(4750, 25.0))
        // ₹1,000 × 33.33% = ₹333.3
        assertEquals("₹333.3", shareAmountText(1000, 33.33))
        // ₹999 × 12.5% = ₹124.875 → ₹124.9
        assertEquals("₹124.9", shareAmountText(999, 12.5))
    }

    @Test
    fun aFractionThatRoundsToWholeRupees_dropsTheDecimal() {
        // ₹3 × 33.33% = ₹0.9999 → ₹1.0, shown as ₹1
        assertEquals("₹1", shareAmountText(3, 33.33))
    }
}
