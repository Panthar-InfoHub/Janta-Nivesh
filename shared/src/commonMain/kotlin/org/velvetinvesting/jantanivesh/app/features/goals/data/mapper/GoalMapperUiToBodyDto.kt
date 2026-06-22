package org.velvetinvesting.jantanivesh.app.features.goals.data.mapper

import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping.GoalMapBodyDto
import org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.goalmapping.MapData
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.SelectableSchemeUiModel


fun List<SelectableSchemeUiModel>.toBody(goalId: Int): GoalMapBodyDto {
    return GoalMapBodyDto(
        goal_id = goalId,
        map_data = this.map {
            MapData(
                folio = it.folio,
                scheme_id = it.schemeId.toString()
            )
        }
    )
}