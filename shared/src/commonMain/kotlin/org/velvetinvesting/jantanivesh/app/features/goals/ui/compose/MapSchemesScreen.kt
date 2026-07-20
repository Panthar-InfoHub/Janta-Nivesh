    package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.plus_icon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectionImpactEffect
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectionImpactEvent
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectionImpactUiData
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectionImpactUiState
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.subHeading
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.VelvetLoader
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalSchemeDomain
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.SelectableSchemeUiModel

@Preview
@Composable
fun MapSchemesScreenPreview() {
    JantaNiveshTheme {
        val mockData = ProjectionImpactUiData(
            goalItemName = "Buy a House",
            todaysCost = 5000000L,
            futureValue = 7500000.0,
            targetYear = 2030,
            monthlySip = 25000.0,
            feasibilityScore = 0.8f,
            currentSaved = 1000000L,
            targetAmount = 7500000L,
            increasedBy = 2500000.0,
            requiredMonthly = 25000.0,
            schemes = listOf(
                GoalSchemeDomain(
                    schemeName = "SBI Bluechip Fund",
                    folio = "123456789",
                    balUnits = "150.5",
                    nav = "45.6",
                    currentVal = "6862",
                    actualFolio = "Preview",
                    schemeId = "Preview"
                )
            ),
            goalId = 1,
            goalName = "Buy a House",
            goalTypeId = 1
        )
        MapSchemesScreen(
            uiState = ProjectionImpactUiState(
                goalDetailsState = UiState.Success(mockData)
            ),
            effectFlow = emptyFlow(),
            onEvent = {},
            onBack = {}
        )
    }
}

@Composable
fun MapSchemesScreen(
    uiState: ProjectionImpactUiState,
    effectFlow: Flow<ProjectionImpactEffect>,
    onEvent: (ProjectionImpactEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    UiStateContainer(
        uiState = uiState.goalDetailsState,
        onRetry = { onEvent(ProjectionImpactEvent.RetryGoalDetails) },
    ) { goalDataResponse ->
        Scaffold(
            topBar = {
                BackHeader(
                    title = "Map Schemes",
                    showBack = true,
                    onBack = onBack
                )
            },
            bottomBar = {
                NextButtonFooter(
                    onClick = { onEvent(ProjectionImpactEvent.OpenBottomSheet) },
                    pv = PaddingValues(0.dp),
                    value = if (goalDataResponse.schemes.isEmpty()) "Map Schemes to Goal" else "More funds for maps"
                )
            },
            containerColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (goalDataResponse.schemes.isEmpty()) {
                    MapSchemesEmptyContent(
                        modifier = Modifier.weight(1f),
                        onClick = { onEvent(ProjectionImpactEvent.OpenBottomSheet) }
                    )
                } else {
                    MapSchemesFilledContent(
                        mappedSchemes = goalDataResponse.schemes,
                        onRemoveScheme = { onEvent(ProjectionImpactEvent.UnMapGoal(goalDataResponse.goalId)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            MapSchemesBottomSheetContent(
                portfolioState = uiState.portfolioDataState,
                effectFlow = effectFlow,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun TotalCurrentValueBar(totalValue: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(Spacing.dp8)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Current Value",
                style = titlesStyle,
                color = titleColor
            )
            Text(
                text = "₹${formatWithCommas(totalValue.toLong())}".withInterRupee(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = appGreen
            )
        }
    }
}

@Composable
fun MapSchemesEmptyContent(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xffEFF4FF), CircleShape)
                .border(1.dp, Color(0xffCBDBF5).copy(0.3f), CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color(0xffCBDBF5).copy(0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.plus_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp).size(32.dp),
                    tint = Primary
                )
            }
        }
        Column(
            modifier = Modifier.padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "No schemes mapped yet",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Primary
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Link your investments to track progress towards this goal",
                style = titlesStyle,
                color = titleColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MapSchemesFilledContent(
    mappedSchemes: List<GoalSchemeDomain>,
    onRemoveScheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TotalCurrentValueBar(
                totalValue = mappedSchemes.sumOf { it.currentVal.toDoubleOrNull() ?: 0.0 }
            )
        }
        items(mappedSchemes) { scheme ->
            MappedSchemeCard(scheme = scheme)
        }
        item {
            Text(
                text = "Remove Mapping",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onRemoveScheme),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                color = appRed
            )
        }
    }
}

@Composable
fun MappedSchemeCard(scheme: GoalSchemeDomain) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(Spacing.dp12))
            .border(1.dp, Color.LightGray.copy(alpha = 0.3f))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = scheme.schemeName,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "FOLIO", style = tinyLabel)
                    Text(
                        text = scheme.folio,
                        style = subHeading,
                        color = Primary
                    )
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = "UNITS", style = tinyLabel)
                    Text(
                        text = scheme.balUnits,
                        style = subHeading,
                        color = Primary
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "NAV", style = tinyLabel)
                    Text(
                        text = "₹${scheme.nav}".withInterRupee(),
                        style = subHeading,
                        color = Primary
                    )
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = "CURRENT VALUE", style = tinyLabel)
                    Text(
                        text = "₹${formatWithCommas(scheme.currentVal.toDoubleOrNull()?.toLong() ?: 0L)}".withInterRupee(),
                        style = subHeading,
                        color = appGreen
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSchemesBottomSheetContent(
    portfolioState: UiState<List<SelectableSchemeUiModel>>,
    effectFlow: Flow<ProjectionImpactEffect>,
    onEvent: (ProjectionImpactEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var showSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                ProjectionImpactEffect.OpenBottomSheet -> showSheet = true
                ProjectionImpactEffect.CloseBottomSheet -> showSheet = false
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(ProjectionImpactEvent.CloseBottomSheet) },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            when (portfolioState) {
                is UiState.Error -> {
                    ErrorScreen(
                        errorMessage = portfolioState.message,
                        onRetryClick = { onEvent(ProjectionImpactEvent.RetryPortfolio) }
                    )
                }
                UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        VelvetLoader()
                    }
                }
                is UiState.Success -> {
                    val data = portfolioState.data
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(Spacing.dp15))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Map Schemes",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Choose the fund where you have invested to set a goal",
                            style = titlesStyle,
                            color = titleColor,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 24.dp)
                        )

                        LazyColumn(
                            modifier = Modifier.weight(1f, fill = false),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(data) { scheme ->
                                SelectableSchemeItem(
                                    scheme = scheme,
                                    onToggle = { onEvent(ProjectionImpactEvent.ToggleSelection(scheme.schemeId)) }
                                )
                            }
                        }

                        if (data.isEmpty()) {
                            Text(
                                text = "Purchase Funds to map them with goals",
                                style = titlesStyle,
                                color = titleColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        AppButton(
                            onClick = { onEvent(ProjectionImpactEvent.MapGoal) },
                            text = "Confirm Selection",
                            enabled = data.any { it.isSelected }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectableSchemeItem(
    scheme: SelectableSchemeUiModel,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(Spacing.dp12)
            )
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(if (scheme.isSelected) Color(0xffEFF6FF) else Color.White)
            .clickable(
                onClick = onToggle,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = scheme.name,
                style = titlesStyle.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = ("Units: ${scheme.units} | Value: ₹${formatWithCommas(scheme.value.toLong())}").withInterRupee(),
                style = MaterialTheme.typography.displaySmall,
                color = titleColor
            )
        }
        Checkbox(
            checked = scheme.isSelected,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = Secondary,
                uncheckedColor = Color.LightGray.copy(alpha = 0.5f)
            )
        )
    }
}