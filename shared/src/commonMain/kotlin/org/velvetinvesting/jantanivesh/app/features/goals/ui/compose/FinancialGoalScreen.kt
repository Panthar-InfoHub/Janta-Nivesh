package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.flag_icon
import jantanivesh.shared.generated.resources.goal_category
import jantanivesh.shared.generated.resources.goal_expected_inflation_rate
import jantanivesh.shared.generated.resources.goal_name_label
import jantanivesh.shared.generated.resources.goal_present_cost
import jantanivesh.shared.generated.resources.goal_projected_impact
import jantanivesh.shared.generated.resources.goal_save_goal
import jantanivesh.shared.generated.resources.goal_target_amount
import jantanivesh.shared.generated.resources.goal_target_year
import jantanivesh.shared.generated.resources.icon_callender
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileSecondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.IndianCurrencyVisualTransformation
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.YearPicker
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalCalculationDomain
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalOption
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.AddGoalEvent
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.AddGoalUiState
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.GoalFormState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.rememberDatePickerInteractionSource
import kotlin.math.abs
import kotlin.math.round

/**
 * Creating a goal. Which fields appear, which tenures and costs are suggested, and which rates
 * are prefilled all come from `GET /user-goal/config` for the selected type — the screen itself
 * knows only how to lay them out.
 *
 * Every label the design shows in two languages is written as `"English/ (" + translation + ")"`,
 * the translation coming from `strings.xml`; the rest of the copy stays English-only.
 */
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
        modifier = Modifier.clearFocusOnTap()
    ) { data ->
        val form = data.form
        val currentYear = remember { DateTimeUtils.getCurrentYear() }
        var showYearPicker by remember { mutableStateOf(false) }

        Box(modifier = modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                BackHeader(
                    title = "Financial Goal",
                    onBack = { handleEvent(AddGoalEvent.OnBackClicked) },
                    modifier = Modifier.padding(horizontal = Spacing.dp16)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(horizontal = Spacing.dp16),
                    contentPadding = PaddingValues(top= Spacing.dp16)
                ) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                            DropDownSelector(
                                title = "Category/ (" +
                                        stringResource(Res.string.goal_category) + ")",
                                value = form.selectedOption?.title ?: "",
                                onValueChange = { handleEvent(AddGoalEvent.OnOptionSelected(it)) },
                                placeholder = "Select Category",
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.flag_icon),
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(Spacing.dp20)
                                    )
                                },
                                list = data.options,
                                textConvertor = { it.title }
                            )
                        }
                    }

                    item {
                        GoalFormSection(
                            form = form,
                            handleEvent = handleEvent,
                            onTargetYearClicked = { showYearPicker = true }
                        )
                    }

                    if (data.projecting || data.projection != null ||
                        data.projectionError != null
                    ) {
                        item {
                            ProjectedImpactSection(
                                projection = data.projection,
                                projecting = data.projecting,
                                error = data.projectionError
                            )
                        }
                    }
                }

                NextButtonFooter(
                    value = "Save Goal/ (" + stringResource(Res.string.goal_save_goal) + ")",
                    onClick = { handleEvent(AddGoalEvent.OnSaveGoalClicked) },
                    loading = loading,
                    enabled = data.isValid
                )
            }

            // The picker scrims the whole screen, so it sits over the form rather than in it.
            if (showYearPicker) {
                val option = form.selectedOption
                YearPicker(
                    selectedYear = form.years.toIntOrNull()?.let { currentYear + it },
                    startOffsetYears = option?.minYears ?: 1,
                    yearsAhead = option?.maxYears ?: 30,
                    onYearSelected = { year ->
                        val years = (year - currentYear).coerceAtLeast(1)
                        handleEvent(AddGoalEvent.UpdateForm { copy(years = years.toString()) })
                    },
                    onDismiss = { showYearPicker = false }
                )
            }
        }
    }
}

@Composable
fun GoalFormSection(
    form: GoalFormState,
    handleEvent: (AddGoalEvent) -> Unit,
    onTargetYearClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val option = form.selectedOption ?: return
    val type = option.type ?: return
    val currentYear = remember { DateTimeUtils.getCurrentYear() }

    // The form holds a tenure; the design shows the year it lands on.
    val targetYearText = form.years.toIntOrNull()?.let { (currentYear + it).toString() }.orEmpty()

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
        modifier = modifier
    ) {
        if (type.needsChildDetails) {
            TitledAppTextField(
                title = "Child Name",
                value = form.childName,
                onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(childName = it) }) },
                placeholder = "Child Name",
                mandatory = true
            )
            TitledAppTextField(
                title = "Child Age",
                value = form.childAge,
                onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(childAge = it) }) },
                placeholder = "Age",
                keyboardType = KeyboardType.Number,
                mandatory = true
            )
        } else {
            TitledAppTextField(
                title = "Goal Name/ (" + stringResource(Res.string.goal_name_label) + ")",
                value = form.goalName,
                onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(goalName = it) }) },
                placeholder = goalNamePlaceholder(type),
                mandatory = true
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.Bottom) {
            TitledAppTextField(
                title = "Target Year/ (" + stringResource(Res.string.goal_target_year) + ")",
                value = targetYearText,
                // The year is chosen from the wheel picker, never typed.
                onValueChange = { },
                readOnly = true,
                interactionSource = rememberDatePickerInteractionSource(onTargetYearClicked),
                placeholder = "${currentYear + option.minYears}",
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.icon_callender),
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(Spacing.dp20)
                    )
                },
                mandatory = true,
                modifier = Modifier.weight(1f)
            )

            TitledAppTextField(
                title = if (type.usesTargetAmount) {
                    "Target Amount/ (" + stringResource(Res.string.goal_target_amount) + ")"
                } else {
                    "Present Cost/ (" + stringResource(Res.string.goal_present_cost) + ")"
                },
                value = form.amount,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }
                    handleEvent(AddGoalEvent.UpdateForm { copy(amount = digits) })
                },
                placeholder = "0",
                keyboardType = KeyboardType.Number,
                visualTransformation = IndianCurrencyVisualTransformation(),
                prefix = {
                    Text(
                        text = "₹".withInterRupee(),
                        style = MaterialTheme.typography.labelSmall,
                        color = GreyText
                    )
                },
                mandatory = true,
                modifier = Modifier.weight(1f)
            )
        }

        // Inflation is absent for a fixed corpus — there is no present-day cost to inflate.
        if (option.inflationRate != null) {
            TitledAppTextField(
                title = "Expected Inflation Rate/ (" +
                        stringResource(Res.string.goal_expected_inflation_rate) + ")",
                value = form.inflation,
                onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(inflation = it) }) },
                placeholder = "Inflation %",
                keyboardType = KeyboardType.Number,
                suffix = {
                    Text(
                        text = "%",
                        style = MaterialTheme.typography.labelSmall,
                        color = GreyText
                    )
                }
            )
        }
    }
}

/**
 * The four figures the projection is read for, as the design lays them out: cost against future
 * value on the top row, horizon against the SIP that closes it underneath.
 */
@Composable
private fun ProjectedImpactSection(
    projection: GoalCalculationDomain?,
    projecting: Boolean,
    error: String?,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Projected Impact (" + stringResource(Res.string.goal_projected_impact) + ")",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            if (projecting) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = Primary,
                    modifier = Modifier.size(Spacing.dp16)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
            modifier = Modifier
                .fillMaxWidth()
                .genericDropShadow(shape = RoundedCornerShape(Spacing.dp16))
                .background(White, RoundedCornerShape(Spacing.dp16))
                .padding(Spacing.dp20)
        ) {
            when {
                projection != null -> {
                    val targetYear = DateTimeUtils.getCurrentYear() + projection.yearsRemaining
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
                        ImpactCell(
                            label = if (projection.targetAmount != null) {
                                "TARGET CORPUS"
                            } else {
                                "TODAY'S COST"
                            },
                            value = projection.currentCostOrTarget().asCompactRupees(),
                            modifier = Modifier.weight(1f)
                        )
                        ImpactCell(
                            label = "FUTURE VALUE ($targetYear)",
                            value = projection.futureTargetAmount.asCompactRupees(),
                            labelColor = ProfileSecondary,
                            valueColor = ProfileSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
                        ImpactCell(
                            label = "TIME HORIZON",
                            value = "${projection.yearsRemaining} Years",
                            modifier = Modifier.weight(1f)
                        )
                        ImpactCell(
                            label = "REQUIRED SIP",
                            value = "₹ ${formatWithCommas(projection.requiredMonthlySip.toLong())}",
                            valueSuffix = " /mo",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                error != null -> Text(
                    text = "Could not project this goal right now. You can still save it.",
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText
                )
            }
        }
    }
}

@Composable
private fun ImpactCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueSuffix: String = "",
    labelColor: Color = GreyText,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp6),
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = labelColor
        )
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value.withInterRupee(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            if (valueSuffix.isNotEmpty()) {
                Text(
                    text = valueSuffix,
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText
                )
            }
        }
    }
}

/** Whichever of the two the goal was sized from — a named corpus, or a present-day cost. */
private fun GoalCalculationDomain.currentCostOrTarget(): Double =
    targetAmount ?: currentCost ?: 0.0

/** "₹ 15.00 L", the design's own shortening — two decimals kept so 15.4L reads apart from 15L. */
private fun Double.asCompactRupees(): String {
    val absolute = abs(this)
    val (value, suffix) = when {
        absolute >= 10_000_000 -> this / 10_000_000 to " Cr"
        absolute >= 100_000 -> this / 100_000 to " L"
        absolute >= 1_000 -> this / 1_000 to " K"
        else -> return "₹ ${formatWithCommas(toLong())}"
    }
    return "₹ ${value.toTwoDecimals()}$suffix"
}

private fun Double.toTwoDecimals(): String {
    val scaled = round(abs(this) * 100).toLong()
    val sign = if (this < 0) "-" else ""
    return "$sign${scaled / 100}.${(scaled % 100).toString().padStart(2, '0')}"
}

private fun goalNamePlaceholder(type: GoalType): String = when (type) {
    GoalType.BuyHome -> "My Dream Home"
    GoalType.BuyVehicle -> "My New Car"
    GoalType.BuildSavings -> "Emergency Cushion"
    else -> "Goal Name"
}

@Preview(showBackground = true, locale = "hi")
@Composable
fun FinancialGoalScreenPreview() {
    val option = GoalOption(
        title = "Wealth Building",
        color = Primary,
        goalTypeId = GoalType.BuyVehicle.id,
        purpose = "Save for the car you want to drive home",
        inflationRate = 0.06,
        expectedReturnRate = 0.10,
        tenureSuggestions = listOf(3, 5, 10),
        costChips = listOf(500_000, 1_000_000, 1_500_000, 2_500_000)
    )
    JantaNiveshTheme {
        FinancialGoalScreen(
            state = UiState.Success(
                AddGoalUiState(
                    options = listOf(option),
                    form = GoalFormState(
                        selectedOption = option,
                        goalName = "New Car",
                        years = "3",
                        amount = "1500000",
                        inflation = "6.0",
                        expectedReturn = "10"
                    ),
                    projection = GoalCalculationDomain(
                        goalTypeId = GoalType.BuyVehicle.id,
                        yearsRemaining = 3,
                        currentCost = 1_500_000.0,
                        targetAmount = null,
                        currentSavings = 0.0,
                        inflationRateUsed = 0.06,
                        expectedReturnRateUsed = 0.10,
                        futureTargetAmount = 1_786_524.0,
                        fvCurrentSavings = 0.0,
                        netRequiredCorpus = 1_786_524.0,
                        requiredMonthlySip = 42_500.0,
                        requiredLumpsumToday = 1_342_000.0
                    ),
                    isValid = true
                )
            ),
            loading = false,
            handleEvent = {}
        )
    }
}
