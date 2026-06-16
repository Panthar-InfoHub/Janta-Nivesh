package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.FinancialGoalEvent
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.FinancialGoalUiState

@Preview(showBackground = true)
@Composable
fun FinancialGoalScreenPreview() {
    JantaNiveshTheme {
        FinancialGoalScreen(
            pv = PaddingValues(0.dp),
            state = FinancialGoalUiState(),
            handleEvent = {}
        )
    }
}

@Composable
fun FinancialGoalScreen(
    pv: PaddingValues,
    state: FinancialGoalUiState,
    handleEvent: (FinancialGoalEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(pv)) {
        BackHeader(
            title = "Financial Goal",
            onBack = { handleEvent(FinancialGoalEvent.OnBackClicked) },
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
                        title = "Category/ ", value = "", onValueChange = { handleEvent(FinancialGoalEvent.OnCategorySelected(it)) },
                        placeholder = state.selectedCategory?.displayName ?: "UNAVAILABLE",
                        list = state.categories,
                        textConvertor = { it.displayName },
                    )
                }
                item {
                    TitledAppTextField(
                        title = "Goal Name/ ",
                        value = state.goalName,
                        onValueChange = { handleEvent(FinancialGoalEvent.OnGoalNameChanged(it)) },
                        placeholder = "New Car"
                    )
                }

                if (state.selectedCategory == GoalType.ChildEducation || state.selectedCategory == GoalType.ChildMarriage) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                            TitledAppTextField(
                                title = "Child Name/ ",
                                value = state.childName,
                                onValueChange = { handleEvent(FinancialGoalEvent.OnChildNameChanged(it)) },
                                placeholder = "John",
                                modifier = Modifier.weight(1f)
                            )
                            TitledAppTextField(
                                title = "Child Age/ ",
                                value = state.childAge,
                                onValueChange = { handleEvent(FinancialGoalEvent.OnChildAgeChanged(it)) },
                                placeholder = "5",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                if (state.selectedCategory == GoalType.Retirement) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                            TitledAppTextField(
                                title = "Current Age/ ",
                                value = state.currentAge,
                                onValueChange = { handleEvent(FinancialGoalEvent.OnCurrentAgeChanged(it)) },
                                placeholder = "30",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                            TitledAppTextField(
                                title = "Retirement Age/ ",
                                value = state.retirementAge,
                                onValueChange = { handleEvent(FinancialGoalEvent.OnRetirementAgeChanged(it)) },
                                placeholder = "60",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                            TitledAppTextField(
                                title = "Life Expectancy/ ",
                                value = state.lifeExpectancy,
                                onValueChange = { handleEvent(FinancialGoalEvent.OnLifeExpectancyChanged(it)) },
                                placeholder = "85",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                            TitledAppTextField(
                                title = "Monthly Expense/ ",
                                value = state.currentMonthlyExpense,
                                onValueChange = { handleEvent(FinancialGoalEvent.OnCurrentMonthlyExpenseChanged(it)) },
                                placeholder = "50,000",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item {
                        TitledAppTextField(
                            title = "Post Retirement Return/ ",
                            value = state.postRetirementReturn,
                            onValueChange = { handleEvent(FinancialGoalEvent.OnPostRetirementReturnChanged(it)) },
                            placeholder = "8",
                            keyboardType = KeyboardType.Number,
                            trailingIcon = { Text("%") }
                        )
                    }
                } else {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                            TitledAppTextField(
                                title = "Target Year/ ",
                                value = state.targetYear,
                                onValueChange = { handleEvent(FinancialGoalEvent.OnTargetYearChanged(it)) },
                                placeholder = "2027",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                            TitledAppTextField(
                                title = "Target Amount/ ",
                                value = state.targetAmount,
                                onValueChange = { handleEvent(FinancialGoalEvent.OnTargetAmountChanged(it)) },
                                placeholder = "15,00,000",
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                        TitledAppTextField(
                            title = "Inflation Rate/ ",
                            value = state.expectedInflationRate,
                            onValueChange = { handleEvent(FinancialGoalEvent.OnInflationRateChanged(it)) },
                            placeholder = "6.0",
                            keyboardType = KeyboardType.Number,
                            trailingIcon = { Text("%") },
                            modifier = Modifier.weight(1f)
                        )
                        TitledAppTextField(
                            title = "Return Rate/ ",
                            value = state.expectedReturnRate,
                            onValueChange = { handleEvent(FinancialGoalEvent.OnReturnRateChanged(it)) },
                            placeholder = "12.0",
                            keyboardType = KeyboardType.Number,
                            trailingIcon = { Text("%") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
                        Text("Projected Impact", style = MaterialTheme.typography.labelLarge)
                        ProjectedImpactCard(
                            todayCost = "₹${state.todayCost}",
                            futureValue = "₹${state.futureValue}",
                            timeHorizon = "${state.timeHorizon} years",
                            requiredSip = "₹${state.requiredSip}"
                        )
                    }
                }
            }

            AppButton(
                text = "Save Goal",
                onClick = { handleEvent(FinancialGoalEvent.OnSaveGoalClicked) },
                modifier = Modifier
                    .genericDropShadow()
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp16)
                    .navigationBarsPadding(),
                style = AppButtonDefaults.style(shape = RoundedCornerShape(Spacing.dp16))
            )
        }
    }
}

@Composable
private fun ReturnDetailItem(
    label: String,
    value: String,
    valueSuffix: String = "",
    valueColor: Color = Black,
    labelColor: Color = GreyText,
    valueStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = labelColor
        )
        Text(
            text = value + valueSuffix,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = valueColor
        )
    }
}

@Composable
private fun ProjectedImpactCard(
    todayCost: String,
    futureValue: String,
    timeHorizon: String,
    requiredSip: String
) {
    Box(
        modifier = Modifier.genericDropShadow()
            .fillMaxWidth()
            .background(White, shape = RoundedCornerShape(Spacing.dp12))
            .padding(Spacing.dp24)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                ReturnDetailItem(
                    label = "TODAY'S COST",
                    value = todayCost,
                    valueStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                ReturnDetailItem(
                    label = "TIME HORIZON",
                    value = timeHorizon,
                    valueColor = Black,
                    valueStyle = MaterialTheme.typography.labelLarge,
                )

            }
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                ReturnDetailItem(
                    label = "FUTURE VALUE",
                    value = futureValue,
                    valueStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    valueColor = SelectedBoxBorder,
                    labelColor = SelectedBoxBorder,
                )
                ReturnDetailItem(
                    label = "REQUIRED SIP",
                    value = requiredSip,
                    valueColor = Black,
                    valueStyle = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}