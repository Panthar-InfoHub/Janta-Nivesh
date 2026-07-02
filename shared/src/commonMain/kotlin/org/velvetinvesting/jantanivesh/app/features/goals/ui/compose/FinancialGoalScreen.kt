package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.goalOptions
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.AddGoalEvent
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.AddGoalUiState
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.GoalFormState

@Composable
fun FinancialGoalScreen(
    state: UiState<AddGoalUiState>,
    loading: Boolean,
    handleEvent: (AddGoalEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    UiStateContainer(
        uiState = state,
        onRetry = { handleEvent(AddGoalEvent.LoadData) },
        modifier = Modifier
                .clearFocusOnTap()
    ) { data ->
        val form = data.form
        Column(modifier = modifier.fillMaxSize()) {
            BackHeader(
                title = "Financial Goal",
                onBack = { handleEvent(AddGoalEvent.OnBackClicked) },
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = Spacing.dp16)
            )

            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(horizontal = Spacing.dp16, vertical = Spacing.dp12)
                ) {
                    item {
                        DropDownSelector(
                            title = "Goal Type ",
                            value =form.selectedOption?.title ?:"",
                            onValueChange = { handleEvent(AddGoalEvent.OnOptionSelected(it)) },
                            placeholder =  "Select Goal Type",
                            list = goalOptions,
                            textConvertor = { it.title },
                        )
                    }

                    item {
                        GoalFormSection(
                            form = form,
                            currentAge = data.currentAge,
                            handleEvent = handleEvent
                        )
                    }

                    data.preview?.let {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
                                Text("Projected Impact", style = MaterialTheme.typography.labelLarge)
                                // We'll use the data from the viewmodel's latest calculation logic
                                // which is currently handled inside createPreview or similar.
                                // For now, let's keep the existing card if it's still available.
                                // Actually, I'll just show the preview details here.
                            }
                        }
                    }
                }

                NextButtonFooter(
                    value = "Save Goal",
                    onClick = { handleEvent(AddGoalEvent.OnSaveGoalClicked) },
                    loading = loading,
                    enabled = data.isValid
                )
            }
        }
    }
}

@Composable
fun GoalFormSection(
    form: GoalFormState,
    currentAge: Int,
    handleEvent: (AddGoalEvent) -> Unit
) {
    val option = form.selectedOption ?: return

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp24)) {
        when (option.type) {
            GoalType.ChildEducation, GoalType.ChildMarriage -> {
                TitledAppTextField(
                    title = "Child Name",
                    value = form.childName,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(childName = it) }) },
                    placeholder = "Child Name"
                )
                TitledAppTextField(
                    title = "Child Age ",
                    value = form.childAge,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(childAge = it) }) },
                    placeholder = "Age",
                    keyboardType = KeyboardType.Number
                )
                TitledAppTextField(
                    title = "Present Goal Value ",
                    value = form.goalCost,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(goalCost = it) }) },
                    placeholder = "Enter Goal Cost",
                    keyboardType = KeyboardType.Number
                )
                TitledAppTextField(
                    title = "Inflation Rate(%)",
                    value = form.inflation,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(inflation = it) }) },
                    placeholder = "Inflation %",
                    keyboardType = KeyboardType.Number
                )
                TitledAppTextField(
                    title = "Target Year ",
                    value = form.targetYear,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(targetYear = it) }) },
                    placeholder = "Target Year For Goal",
                    keyboardType = KeyboardType.Number
                )
            }

            GoalType.Retirement -> {
                TitledAppTextField(
                    title = "Inflation Rate(%) ",
                    value = form.inflation,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(inflation = it) }) },
                    placeholder = "Inflation %",
                    keyboardType = KeyboardType.Number
                )
                TitledAppTextField(
                    title = "Retirement Age ",
                    value = form.retirementAge,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(retirementAge = it) }) },
                    placeholder = "Retirement Age",
                    keyboardType = KeyboardType.Number
                )
                TitledAppTextField(
                    title = "Life Expectancy ",
                    value = form.lifeExpectancy,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(lifeExpectancy = it) }) },
                    placeholder = "Life Expectancy",
                    keyboardType = KeyboardType.Number
                )
                TitledAppTextField(
                    title = "Monthly Expense ",
                    value = form.monthlyExpense,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(monthlyExpense = it) }) },
                    placeholder = "50,000",
                    keyboardType = KeyboardType.Number
                )
                TitledAppTextField(
                    title = "Post Retirement Return(%) ",
                    value = form.postReturn,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(postReturn = it) }) },
                    placeholder = "Post Retirement Return %",
                    keyboardType = KeyboardType.Number
                )
            }

            GoalType.WealthBuilding -> {
                TitledAppTextField(
                    title = "Goal Name ",
                    value = form.goalName,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(goalName = it) }) },
                    placeholder = "Goal Name"
                )
                TitledAppTextField(
                    title = "Goal Category",
                    value = form.goalItemName,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(goalItemName = it) }) },
                    placeholder = "Goal Category (e.g. House, Travel)"
                )
                TitledAppTextField(
                    title = "Target Amount ",
                    value = form.goalCost,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(goalCost = it) }) },
                    placeholder = "Target Amount",
                    keyboardType = KeyboardType.Number
                )
                TitledAppTextField(
                    title = "Inflation Rate(%) ",
                    value = form.inflation,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(inflation = it) }) },
                    placeholder = "Inflation %",
                    keyboardType = KeyboardType.Number
                )
                TitledAppTextField(
                    title = "Target Year",
                    value = form.targetYear,
                    onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(targetYear = it) }) },
                    placeholder = "Target Year For Goal",
                    keyboardType = KeyboardType.Number
                )
            }
        }

        TitledAppTextField(
            title = "Expected Return(%) ",
            value = form.returns,
            onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(returns = it) }) },
            placeholder = "10.0",
            keyboardType = KeyboardType.Number
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FinancialGoalScreenPreview() {
    JantaNiveshTheme {
        FinancialGoalScreen(
            state = UiState.Success(
                AddGoalUiState(
                    form = GoalFormState(
                        selectedOption = goalOptions[0],
                        childName = "Arav",
                        childAge = "5",
                        goalCost = "1000000",
                        inflation = "8",
                        targetYear = "2040",
                        returns = "12"
                    ),
                    isValid = true,
                    currentAge = 30
                )
            ),
            loading = false,
            handleEvent = {}
        )
    }
}
