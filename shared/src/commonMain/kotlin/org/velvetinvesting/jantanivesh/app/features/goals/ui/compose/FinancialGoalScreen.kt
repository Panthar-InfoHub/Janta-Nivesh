package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
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

data class FinancialGoalScreenState(
    val categories: List<GoalType> = listOf(
        GoalType.ChildEducation,
        GoalType.ChildMarriage,
        GoalType.Retirement,
        GoalType.WealthBuilding
    ),
    val selectedCategory: GoalType? = GoalType.ChildEducation,
    val goalName: String = "",
    val targetYear: String = "",
    val targetAmount: String = "",
    val expectedInflationRate: String = "",
    val todayCost: Long = 0,
    val timeHorizon: Int = 0,
    val futureValue: Long = 0,
    val requiredSip: Long = 0,
)

@Preview(showBackground = true)
@Composable
fun FinancialGoalScreenPreview() {
    JantaNiveshTheme {
        FinancialGoalScreen(FinancialGoalScreenState())
    }
}

@Composable
fun FinancialGoalScreen(state: FinancialGoalScreenState, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(horizontal = Spacing.dp16)) {
        BackHeader(title = "Financial Goal", onBack = { TODO() }, modifier = Modifier.statusBarsPadding())
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.dp24), modifier = Modifier.weight(1f).padding(vertical = Spacing.dp12)) {
            item {
                DropDownSelector(
                    title = "Category/ ", value = "", onValueChange = { it },
                    placeholder = state.selectedCategory?.displayName ?: "UNAVAILABLE",
                    list = state.categories,
                    textConvertor = { it.displayName },
                )
            }
            item {
                TitledAppTextField(
                    title = "Goal Name/ ",
                    value = state.goalName,
                    onValueChange = { it },
                    placeholder = "New Car"
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    TitledAppTextField(
                        title = "Target Year/ ",
                        value = state.targetYear,
                        onValueChange = { it },
                        placeholder = "2027",
                        modifier = Modifier.weight(1f)
                    )
                    TitledAppTextField(
                        title = "Target Amount/ ",
                        value = state.targetAmount,
                        onValueChange = { it },
                        placeholder = "15,00,000",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                TitledAppTextField(
                    title = "Expected Inflation Rate/ ",
                    value = state.expectedInflationRate,
                    onValueChange = { it },
                    placeholder = "6.0",
                    trailingIcon = {
                        Text(
                            "%",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                )
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
            onClick = { TODO() },
            modifier = Modifier.genericDropShadow().fillMaxWidth().navigationBarsPadding(),
            style = AppButtonDefaults.style(shape = RoundedCornerShape(Spacing.dp16))
        )
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
                    label = "FUTURE VALUE (${timeHorizon})",
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
