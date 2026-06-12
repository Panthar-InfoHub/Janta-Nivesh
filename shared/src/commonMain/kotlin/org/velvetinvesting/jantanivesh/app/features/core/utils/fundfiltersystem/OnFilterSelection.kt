package org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem

fun onFilterOptionSelected(
    currentFilter: InvestmentFilter,
    groupId: String,
    optionId: String
): InvestmentFilter {

    val updatedGroups = currentFilter.groups.map { group ->

        if (group.id != groupId) return@map group

        val isAlreadySelected = group.options.any {
            it.id == optionId && it.isSelected
        }

        val updatedOptions = when (group.selectionType) {

            SelectionType.SINGLE -> {
                group.options.map { option ->
                    when {
                        option.id == optionId && isAlreadySelected ->
                            option.copy(isSelected = false)

                        option.id == optionId ->
                            option.copy(isSelected = true)

                        else ->
                            option.copy(isSelected = false)
                    }
                }
            }

            SelectionType.MULTIPLE -> {
                group.options.map { option ->
                    if (option.id == optionId)
                        option.copy(isSelected = !option.isSelected)
                    else
                        option
                }
            }
        }

        group.copy(options = updatedOptions)
    }

    return currentFilter.copy(groups = updatedGroups)
}