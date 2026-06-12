package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.icon_cross
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.subHeading
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.utils.AppBackHandler
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ContinueBackButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.FilterGroup
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.InvestmentFilter
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.SelectionType
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.onFilterOptionSelected

@Composable
fun InvestmentFilterScreen(
    appliedFilter: InvestmentFilter,
    onClose: () -> Unit,
    onCancelClick: () -> Unit,
    onApplyClick: (InvestmentFilter) -> Unit,
    pv: PaddingValues
) {
    var selectedGroupIndex by remember { mutableStateOf(0) }

    var editingFilter by remember(appliedFilter) {
        mutableStateOf(appliedFilter)
    }

    val hasSelectedFilters = remember(editingFilter) {
        editingFilter.groups.any { group ->
            group.options.any { it.isSelected }
        }
    }

    AppBackHandler(
        onBack = onClose,
        enabled = true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
    ) {

        FilterTopBar(onClose)
        Row(
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
        ) {
            FilterLeftPanel(
                modifier=Modifier.fillMaxWidth(0.35f),
                groups = editingFilter.groups,
                selectedIndex = selectedGroupIndex,
                onGroupSelected = { selectedGroupIndex = it }
            )

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = titleColor
            )

            FilterRightPanel(
                modifier=Modifier.weight(1f),
                group = editingFilter.groups[selectedGroupIndex],
                onOptionClick = { groupId, optionId ->
                    editingFilter = onFilterOptionSelected(
                        editingFilter,
                        groupId,
                        optionId
                    )
                }
            )
        }

        FilterBottomBar(
            onCancelClick = onCancelClick,
            onApplyClick = { onApplyClick(editingFilter) },
            isApplyEnabled = hasSelectedFilters,
            pv = pv
        )
    }
}

@Composable
fun FilterBottomBar(
    onCancelClick: () -> Unit,
    onApplyClick: () -> Unit,
    isApplyEnabled: Boolean,
    pv: PaddingValues
) {
    ContinueBackButtonFooter(
        continueText = "View Results",
        backText = "Cancel",
        onContinue = onApplyClick,
        onBack = onCancelClick,
        enabled = isApplyEnabled,
        pv = pv,
    )
}

@Composable
fun FilterTopBar(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(Res.drawable.icon_cross),
            contentDescription = null,
            modifier = Modifier.clickable { onClose() }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Review Investment",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Primary,
            fontFamily = InterFontFamily
        )
    }
}
@Composable
fun FilterLeftPanel(
    groups: List<FilterGroup>,
    selectedIndex: Int,
    onGroupSelected: (Int) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
    ) {
        groups.forEachIndexed { index, group ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSelected) Secondary.copy(0.1f)
                        else Color.Transparent
                    )
                    .clickable { onGroupSelected(index) }
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = group.title,
                    style = subHeading,
                    color = Primary,
                    modifier=Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}
@Composable
fun FilterRightPanel(
    group: FilterGroup,
    onOptionClick: (String, String) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        group.options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ) {
                        onOptionClick(group.id, option.id)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = option.title,
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium
                )

                when(group.selectionType)
                {
                    SelectionType.SINGLE -> {
                        RadioButton(
                            selected = option.isSelected,
                            onClick = {
                                onOptionClick(group.id, option.id)
                            }
                        )
                    }
                    SelectionType.MULTIPLE -> {
                        Checkbox(
                            checked = option.isSelected,
                            onCheckedChange = {
                                onOptionClick(group.id, option.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
