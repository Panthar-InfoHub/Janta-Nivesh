package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models

import org.velvetinvesting.jantanivesh.app.features.kyc.uistate.OccupationType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.FatcaOccupationType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.SourceOfWealth

object OccupationSourceOfWealthMapper {

    fun getSourceOfWealth(
        occupation: OccupationType?
    ): SourceOfWealth? {
        return when (occupation) {
            OccupationType.BUSINESS -> SourceOfWealth.BUSINESS_INCOME
            OccupationType.SERVICES -> SourceOfWealth.SALARY
            OccupationType.PROFESSIONAL -> SourceOfWealth.SALARY
            OccupationType.AGRICULTURE -> SourceOfWealth.BUSINESS_INCOME
            OccupationType.RETIRED -> SourceOfWealth.OTHER
            OccupationType.HOUSEWIFE -> SourceOfWealth.OTHER
            OccupationType.STUDENT -> SourceOfWealth.OTHER
            OccupationType.OTHERS -> SourceOfWealth.OTHER
            null -> null
        }
    }

    fun getSourceOfWealthCode(
        occupationCode: String?
    ): String? {
        return getSourceOfWealth(
            OccupationType.fromCode(occupationCode)
        )?.code
    }

    fun getFatcaOccupationTypeCode(
        sourceOfWealthCode: String?
    ): String {
        return when (sourceOfWealthCode) {
            SourceOfWealth.SALARY.code ->
                FatcaOccupationType.SERVICE.code

            SourceOfWealth.BUSINESS_INCOME.code ->
                FatcaOccupationType.BUSINESS.code

            SourceOfWealth.GIFT.code,
            SourceOfWealth.ANCESTRAL_PROPERTY.code,
            SourceOfWealth.RENTAL_INCOME.code,
            SourceOfWealth.PRIZE_MONEY.code,
            SourceOfWealth.ROYALTY.code,
            SourceOfWealth.OTHER.code ->
                FatcaOccupationType.OTHERS.code

            else ->
                FatcaOccupationType.NOT_CATEGORIZED.code
        }
    }
}