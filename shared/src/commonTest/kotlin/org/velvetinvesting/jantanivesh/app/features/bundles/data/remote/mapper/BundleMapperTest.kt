package org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.mapper

import kotlinx.serialization.json.Json
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.AllBundlesDto
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.BundleDetailsDto
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleRisk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** The bundle endpoints, decoded the way the app's client decodes them and mapped to the domain. */
class BundleMapperTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun allBundles_mapsMetaDataAndSlots() {
        val bundles = json.decodeFromString<AllBundlesDto>(ALL_BUNDLES).toDomain()

        assertEquals(2, bundles.size)

        val bachat = bundles[0]
        assertEquals("Bachat", bachat.name)
        assertEquals("Chhoti bachat, mazboot shuruaat.", bachat.description)
        assertEquals(500L, bachat.metaData.startAmount)
        assertEquals("1-3 Years", bachat.metaData.investmentTime)
        assertEquals(BundleRisk.LOW, bachat.metaData.risk)
        assertEquals(2, bachat.fundCount)
        assertEquals("Debt", bachat.assetClassLabel)

        val future = bundles[1]
        assertEquals(1000L, future.metaData.startAmount)
        assertEquals("Very High", future.metaData.riskLevel)
        assertEquals(2, future.fundCount)
        assertEquals("Equity + Commodity + Hybrid", future.assetClassLabel)
    }

    @Test
    fun bundleDetails_preSelectedFundIsTheSlotDefault() {
        val bundle = json.decodeFromString<BundleDetailsDto>(BUNDLE_DETAILS).toDomain()

        assertEquals(500L, bundle.metaData.startAmount)

        val slots = bundle.categories.single().slots
        val first = slots[0]
        assertEquals("cmt83ak5p00gu83riqq0mx4u0", first.defaultFund?.id)
        assertEquals(first.defaultFund, first.selectedFund)
        assertEquals("15.8261", first.defaultFund?.latestNav)
        assertEquals(5.867, first.defaultFund?.metrics?.return1Y)
        assertEquals(100L, first.defaultFund?.transactionRules?.minMonthlySipAmount)
        assertEquals(100L, first.defaultFund?.transactionRules?.minLumpSumAmount)

        // No pre-selected fund: falls back to the category's list in rank order.
        assertEquals("cmt83eyhh030t83rixbjkk47h", slots[1].defaultFund?.id)
        assertNull(slots[1].defaultFund?.transactionRules)
        assertEquals("29.3879", slots[1].defaultFund?.latestNav)
    }

    /** The backend's current shape: three start amounts, a daily minimum, and `scheme_plan` strings. */
    @Test
    fun bundleDetails_mapsAllThreeMinimums() {
        val bundle = json.decodeFromString<BundleDetailsDto>(BUNDLE_DETAILS_WITH_MINIMUMS).toDomain()

        assertEquals(1000L, bundle.metaData.startAmount)
        assertEquals(100L, bundle.metaData.dailyStartAmount)
        assertEquals(1000L, bundle.metaData.monthlyStartAmount)

        val category = bundle.categories.single()
        val preSelected = category.slots.single().defaultFund!!
        assertEquals("cmt83dnfj02a083rig8cdtpjs", preSelected.id)
        assertEquals("105.54", preSelected.latestNav)
        assertEquals(100L, preSelected.transactionRules?.minLumpSumAmount)
        assertEquals(100L, preSelected.transactionRules?.minMonthlySipAmount)
        assertEquals(20L, preSelected.transactionRules?.minDailySipAmount)

        val listed = category.funds.single()
        assertEquals("451.8827", listed.latestNav)
        // A null return reads as 0.
        assertEquals(0.0, listed.metrics.return5Y)
        assertEquals(5000L, listed.transactionRules?.minLumpSumAmount)
        assertEquals(500L, listed.transactionRules?.minMonthlySipAmount)
        assertEquals(500L, listed.transactionRules?.minSipAmount)
        assertEquals(500L, listed.transactionRules?.minDailySipAmount)
    }

    private companion object {
        val ALL_BUNDLES = """
        {
            "success": true,
            "message": "Bundles fetched successfully",
            "data": {
                "bundles": [
                    {
                        "id": "cmuqo6scn000403riwydgqhpk",
                        "bundle_name": "Bachat",
                        "bundle_description": "Chhoti bachat, mazboot shuruaat.",
                        "equity_percentage": 0,
                        "commodity_percentage": 0,
                        "debt_percentage": 100,
                        "hybrid_percentage": 0,
                        "img_url": null,
                        "meta_data": {
                            "risk_level": "Low",
                            "start_amount": 500,
                            "investment_time": "1-3 Years",
                            "investment_growth": "6-8% p.a."
                        },
                        "categories": [
                            {
                                "id": "cmuqo6ser000503rika9g6cfx",
                                "bundle_id": "cmuqo6scn000403riwydgqhpk",
                                "category_name": "debt",
                                "display_name": "Debt (Ultra Short Duration)",
                                "total_percentage": 100,
                                "slots": [
                                    {
                                        "id": "cmuqo6sgu000603rikg8vfisj",
                                        "bundle_category_id": "cmuqo6ser000503rika9g6cfx",
                                        "allocation_percentage": 60,
                                        "default_rank": 1,
                                        "pre_selected_product_id": "cmt83ak5p00gu83riqq0mx4u0"
                                    },
                                    {
                                        "id": "cmuqo6sgu000703riwvvhkajn",
                                        "bundle_category_id": "cmuqo6ser000503rika9g6cfx",
                                        "allocation_percentage": 40,
                                        "default_rank": 2,
                                        "pre_selected_product_id": "cmt83b1zv00r883rillriqc73"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "id": "cmuqonq07000m03ri4s3zmttl",
                        "bundle_name": "Bachhon Ka Future",
                        "bundle_description": "Bade sapnon ke liye, lamba nivesh.",
                        "equity_percentage": 70,
                        "commodity_percentage": 10,
                        "debt_percentage": 0,
                        "hybrid_percentage": 20,
                        "img_url": null,
                        "meta_data": {
                            "risk_level": "Very High",
                            "start_amount": 1000,
                            "investment_time": "5-10+ Years",
                            "investment_growth": "15-18% p.a."
                        },
                        "categories": [
                            {
                                "id": "cmuqonq2d000n03rij7h7licz",
                                "bundle_id": "cmuqonq07000m03ri4s3zmttl",
                                "category_name": "large_mid_cap",
                                "display_name": "Large & Mid Cap",
                                "total_percentage": 30,
                                "slots": [
                                    {
                                        "id": "cmuqonq4g000o03riyeeatc9f",
                                        "bundle_category_id": "cmuqonq2d000n03rij7h7licz",
                                        "allocation_percentage": 30,
                                        "default_rank": 1,
                                        "pre_selected_product_id": "cmt83dnfj02a083rig8cdtpjs"
                                    }
                                ]
                            },
                            {
                                "id": "cmuqonqij000v03ris7x2p1es",
                                "bundle_id": "cmuqonq07000m03ri4s3zmttl",
                                "category_name": "others",
                                "display_name": "Gold",
                                "total_percentage": 10,
                                "slots": [
                                    {
                                        "id": "cmuqonqkj000w03ri1j2lovkh",
                                        "bundle_category_id": "cmuqonqij000v03ris7x2p1es",
                                        "allocation_percentage": 10,
                                        "default_rank": 1,
                                        "pre_selected_product_id": "cmt83ac4500ca83ringj8gixu"
                                    }
                                ]
                            }
                        ]
                    }
                ],
                "pagination": { "total": 4, "page": 1, "limit": 20, "totalPages": 1 }
            }
        }
        """.trimIndent()

        val BUNDLE_DETAILS = """
        {
            "success": true,
            "message": "Bundle fetched successfully",
            "data": {
                "bundle_name": "Bachat",
                "bundle_description": "Chhoti bachat, mazboot shuruaat.",
                "equity_percentage": 0,
                "commodity_percentage": 0,
                "debt_percentage": 100,
                "hybrid_percentage": 0,
                "meta_data": {
                    "risk_level": "Low",
                    "start_amount": 500,
                    "investment_time": "1-3 Years",
                    "investment_growth": "6-8% p.a."
                },
                "categories": [
                    {
                        "id": "cmuqo6ser000503rika9g6cfx",
                        "category_name": "debt",
                        "display_name": "Debt (Ultra Short Duration)",
                        "total_percentage": 100,
                        "slots": [
                            {
                                "id": "cmuqo6sgu000603rikg8vfisj",
                                "allocation_percentage": 60,
                                "default_rank": 1,
                                "pre_selected_product_id": "cmt83ak5p00gu83riqq0mx4u0",
                                "pre_selected_fund": {
                                    "id": "cmt83ak5p00gu83riqq0mx4u0",
                                    "name": "Axis Ultra Short Duration Fund - Regular Plan Growth",
                                    "isin": "INF846K01G23",
                                    "img_url": "https://example.com/axis.png",
                                    "latest_nav": 15.8261,
                                    "latest_nav_date": "2026-09-30T00:00:00.000Z",
                                    "returns": { "return_1y": 5.867, "return_3y": 6.558, "return_5y": 5.891 },
                                    "min_investment": { "lumpsum_min": 100, "sip_monthly_min": 100 }
                                }
                            },
                            {
                                "id": "cmuqo6sgu000703riwvvhkajn",
                                "allocation_percentage": 40,
                                "default_rank": 2
                            }
                        ],
                        "funds": [
                            {
                                "id": "cmt83e54e02k783riw3friofp",
                                "name": "Kotak Gold Fund Growth",
                                "latest_nav": "56.1912",
                                "metrics": { "return_1y": 27.096, "return_5y": null }
                            },
                            {
                                "id": "cmt83eyhh030t83rixbjkk47h",
                                "name": "Motilal Oswal Gold and Silver Passive Fund of Funds(Regular Plan)",
                                "isin": "INF247L01BN6",
                                "latest_nav": "29.3879",
                                "latest_nav_date": "2026-09-30T00:00:00.000Z",
                                "metrics": { "return_1y": 29.935, "return_3y": 36.67, "return_5y": null }
                            }
                        ]
                    }
                ]
            }
        }
        """.trimIndent()

        val BUNDLE_DETAILS_WITH_MINIMUMS = """
        {
            "success": true,
            "message": "Bundle fetched successfully",
            "data": {
                "bundle_name": "Bachhon Ka Future",
                "bundle_description": "Bade sapnon ke liye, lamba nivesh.",
                "equity_percentage": 70,
                "commodity_percentage": 10,
                "debt_percentage": 0,
                "hybrid_percentage": 20,
                "meta_data": {
                    "risk_level": "Very High",
                    "start_amount": 1000,
                    "investment_time": "5-10+ Years",
                    "investment_growth": "15-18% p.a.",
                    "daily_start_amount": 100,
                    "monthly_start_amount": 1000
                },
                "categories": [
                    {
                        "id": "cmuqybs61000g4iria04z0m54",
                        "category_name": "large_mid_cap",
                        "display_name": "Large & Mid Cap",
                        "total_percentage": 30,
                        "slots": [
                            {
                                "id": "cmuqybs7m000h4iriq5r597zr",
                                "allocation_percentage": 30,
                                "default_rank": 1,
                                "pre_selected_product_id": "cmt83dnfj02a083rig8cdtpjs",
                                "pre_selected_fund": {
                                    "id": "cmt83dnfj02a083rig8cdtpjs",
                                    "name": "Invesco India Large & Mid Cap Fund - Regular Plan - Growth",
                                    "isin": "INF205K01247",
                                    "img_url": "https://res.cloudinary.com/djufv2qnd/image/upload/v1779125756/Invesco_Mutual_Fund_lishm2.png",
                                    "latest_nav": 105.54,
                                    "latest_nav_date": "2026-09-30T00:00:00.000Z",
                                    "returns": { "return_1y": 6.413, "return_3y": 20.116, "return_5y": 14.97 },
                                    "min_investment": { "lumpsum_min": 100, "sip_monthly_min": 100, "sip_daily_min": 20 }
                                }
                            }
                        ],
                        "funds": [
                            {
                                "id": "cmt83cz1y01vm83ritftp93wm",
                                "name": "HSBC Midcap Fund - Regular Growth",
                                "isin": "INF917K01254",
                                "img_url": "https://res.cloudinary.com/djufv2qnd/image/upload/v1779125755/HSBC_Mutual_Fund_bk5sjw.png",
                                "latest_nav": "451.8827",
                                "latest_nav_date": "2026-09-30T00:00:00.000Z",
                                "metrics": {
                                    "return_1y": 16.642,
                                    "return_3y": 20.893,
                                    "return_5y": null,
                                    "return_6m": 24.096,
                                    "return_90d": -2.014,
                                    "return_30d": -5.671,
                                    "nav_change_pct": -0.042
                                },
                                "scheme_plan": {
                                    "lumpsum_amount_min": "5000",
                                    "sip_monthly_amount_min": "500",
                                    "sip_daily_amount_min": "500"
                                }
                            }
                        ]
                    }
                ]
            }
        }
        """.trimIndent()
    }
}
