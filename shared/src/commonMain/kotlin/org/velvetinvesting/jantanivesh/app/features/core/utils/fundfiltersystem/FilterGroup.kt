package org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem

data class FilterGroup(
    val id: String,
    val title: String,
    val selectionType: SelectionType,
    val options: List<FilterOption>
)