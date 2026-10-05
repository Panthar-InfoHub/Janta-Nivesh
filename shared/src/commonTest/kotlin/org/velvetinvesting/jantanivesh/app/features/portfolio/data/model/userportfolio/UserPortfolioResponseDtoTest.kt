package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.userportfolio

import kotlinx.serialization.json.Json
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.mapper.toDomain
import kotlin.test.Test
import kotlin.test.assertEquals

class UserPortfolioResponseDtoTest {

    // Mirrors the app's HttpClient config: NAVs arrive as quoted strings.
    private val json = Json { isLenient = true; ignoreUnknownKeys = true }

    @Test
    fun holdingWithNullNavAndReturnsParsesAndMapsToZeroes() {
        val dto = json.decodeFromString<UserPortfolioResponseDto>(
            """
            {"code":200,"message":"User portfolio fetched successfully","data":{
              "total_investments":{"current_value":87.92,"total_returns":-2.08,"return_percent":-2.31,
                "allocation":{"mutual_funds":{"value":87.92,"percent":100,"invested_amount":90,
                  "total_returns":-2.08,"return_percent":-2.31,"one_day_return":-0.09,
                  "one_day_return_percent":-0.1,"xirr":-49.98},
                  "fixed_deposits":{"value":0,"percent":0}}},
              "invested_amount_breakdown":{"invested_amount":90,"invested_items_count":2,
                "returns_amount":-2.08,"returns_percent":-2.31},
              "mf_summary":{"current_value":87.92,"invested_amount":90,"total_returns":-2.08,
                "return_percent":-2.31,"one_day_return":-0.09,"one_day_return_percent":-0.1,"xirr":-49.98},
              "mutual_funds":[
                {"id":"a","title":"Axis Gold Fund","is_sip":true,"category":"DEBT","sub_category":"Debt",
                 "nav_as_on":"2026-10-01T00:00:00.000Z","xirr":-49.98,"curr_nav":"42.4135","avg_nav":"43.29",
                 "return_percentage":"-2.0286","amount":90,"current_value":87.92,"return":-2.08,
                 "folio":"910247428186","folios":["910247428186","910247404274"],"bal_units":2.073,"img_url":"x"},
                {"id":"b","title":"Invesco India Smallcap Fund","is_sip":false,"category":"EQUITY",
                 "sub_category":"Small Cap","nav_as_on":"2026-10-01T00:00:00.000Z","xirr":null,
                 "curr_nav":"46.41","avg_nav":null,"return_percentage":null,"amount":0,"current_value":0,
                 "return":0,"folio":"31051557424","folios":["31051557424"],"bal_units":0,"img_url":"y"}
              ],
              "fixed_deposits":[]}}
            """
        )

        val portfolio = dto.toDomain()
        val (axis, invesco) = portfolio.mutualFunds

        assertEquals(42.4135, axis.currentNav)
        assertEquals(43.29, axis.avgNav)
        assertEquals(2, axis.folios.size)

        assertEquals(46.41, invesco.currentNav)
        assertEquals(0.0, invesco.avgNav)
        assertEquals(0.0, invesco.xirr)
        assertEquals("", invesco.returnPercentage)

        assertEquals(-49.98, portfolio.mutualFundSummary.xirr)
        assertEquals(0, portfolio.fixedDeposits.size)
    }
}
