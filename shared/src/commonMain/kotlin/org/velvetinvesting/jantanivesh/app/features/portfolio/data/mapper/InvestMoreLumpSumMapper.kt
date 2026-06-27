package org.velvetinvesting.jantanivesh.app.features.portfolio.data.mapper

import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.LumpSumAdd
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.models.InvestMoreDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.models.InvestMoreItemDto

fun List<LumpSumAdd>.toInvestMoreDto(): InvestMoreDto {
    return InvestMoreDto(
        type = "LUMPSUM",
        items = this.map {
            InvestMoreItemDto(
                scheme_id = it.prod_Id,
                amount = it.amount,
                folio = it.folio
            )
        }
    )
}
