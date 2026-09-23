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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import jantanivesh.shared.generated.resources.confirm_selection
import jantanivesh.shared.generated.resources.delete_box
import jantanivesh.shared.generated.resources.icon_arrow_right
import jantanivesh.shared.generated.resources.plus_icon
import jantanivesh.shared.generated.resources.upward_trend_arrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.SlateGray
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.FundIcon
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.VelvetLoader
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalHoldingDomain
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.MapSchemeEffect
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.MapSchemeEvent
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.MapSchemeUiData
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.MapSchemeUiState
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.SelectableSchemeUiModel

private val CardBorder = Color(0xffE9EDF2)

@Composable
fun MapSchemesScreen(
    uiState: MapSchemeUiState,
    effectFlow: Flow<MapSchemeEffect>,
    onEvent: (MapSchemeEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    UiStateContainer(
        uiState = uiState.goalDetailsState,
        onRetry = { onEvent(MapSchemeEvent.RetryGoalDetails) },
    ) { goalData ->
        Scaffold(
            containerColor = Color.White
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                BackHeader(
                    title = "Map SIP",
                    showBack = true,
                    onBack = onBack,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.dp16)
                )
                if (goalData.holdings.isEmpty()) {
                    MapSchemesEmptyContent(
                        modifier = Modifier.weight(1f),
                        onClick = { onEvent(MapSchemeEvent.OpenBottomSheet) }
                    )
                } else {
                    MappedHoldingsContent(
                        goalData = goalData,
                        removingHoldingIds = uiState.removingHoldingIds,
                        onRemoveHolding = { onEvent(MapSchemeEvent.RemoveHolding(it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
                MapSchemesFooter(
                    text = if (goalData.holdings.isEmpty()) {
                        "Map Schemes to Goal"
                    } else {
                        "More fund for Map"
                    },
                    onClick = { onEvent(MapSchemeEvent.OpenBottomSheet) }
                )
            }

            MapSchemesBottomSheetContent(
                portfolioState = uiState.portfolioDataState,
                mapping = uiState.mapping,
                effectFlow = effectFlow,
                onEvent = onEvent
            )

        }
    }
}

@Composable
private fun MapSchemesFooter(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = Spacing.dp24, vertical = Spacing.dp16)
    ) {
        AppButton(
            text = text,
            onClick = onClick,
            trailingIcon = Res.drawable.icon_arrow_right,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun MappedHoldingsContent(
    goalData: MapSchemeUiData,
    removingHoldingIds: Set<String>,
    onRemoveHolding: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = Spacing.dp16, vertical = Spacing.dp8),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        item {
            TotalCurrentValueCard(
                totalValue = goalData.totalCurrentValue,
                mappedCount = goalData.holdings.size
            )
        }
        items(
            items = goalData.holdings,
            key = { holding -> holding.holdingId }
        ) { holding ->
            MappedHoldingCard(
                holding = holding,
                removing = holding.holdingId in removingHoldingIds,
                onRemove = { onRemoveHolding(holding.holdingId) }
            )
        }
    }
}

@Composable
fun TotalCurrentValueCard(
    totalValue: Double,
    mappedCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .border(1.dp, CardBorder, RoundedCornerShape(Spacing.dp16))
            .background(Color.White)
            .padding(Spacing.dp20),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Current Value",
                    style = titlesStyle,
                    color = titleColor
                )
                Text(
                    text = "$mappedCount Mapped",
                    style = tinyLabel,
                    color = appGreen,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(appGreen.copy(alpha = 0.12f))
                        .padding(horizontal = Spacing.dp8, vertical = Spacing.dp4)
                )
            }
            Text(
                text = "₹${formatWithCommas(totalValue.toLong())}".withInterRupee(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }

        Icon(
            painter = painterResource(Res.drawable.upward_trend_arrow),
            contentDescription = null,
            tint = appGreen,
            modifier = Modifier
                .size(Spacing.dp44)
                .background(appGreen.copy(alpha = 0.12f), RoundedCornerShape(Spacing.dp12))
                .padding(Spacing.dp12)
        )
    }
}

@Composable
fun MappedHoldingCard(
    holding: GoalHoldingDomain,
    removing: Boolean,
    onRemove: () -> Unit,
    showDelete: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .border(1.dp, CardBorder, RoundedCornerShape(Spacing.dp16))
            .background(Color.White)
            .padding(Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            verticalAlignment = Alignment.Top
        ) {
            // The fund house's own logo, with its initial standing in until it loads.
            FundIcon(
                iconUrl = holding.imageUrl,
                name = holding.fundName,
                size = Spacing.dp36,
                cornerRadius = Spacing.dp8
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp2)
            ) {
                Text(
                    text = holding.fundName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Folio: ${holding.folioNumber}",
                    style = tinyLabel.copy(fontWeight = FontWeight.Normal),
                    color = SlateGray
                )
            }

            // Removing swaps the button for a loader, so the same row cannot be deleted twice.
            if (showDelete){
                if (removing) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = Primary,
                        modifier = Modifier.size(Spacing.dp16)
                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.delete_box),
                        contentDescription = "Remove ${holding.fundName}",
                        tint = SlateGray,
                        modifier = Modifier
                            .size(Spacing.dp16)
                            .clickable(onClick = onRemove)
                    )
                }
            }
        }

        HorizontalDivider(color = CardBorder)

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            HoldingStat(
                label = "UNITS",
                value = holding.units.trimTo(2),
                modifier = Modifier
            )
            HoldingStat(
                label = "NAV",
                value = "₹${holding.nav.trimTo(2)}",
                modifier = Modifier,
            )
            HoldingStat(
                label = "CURRENT VALUE",
                value = "₹${formatWithCommas(holding.currentValue.toLong())}",
                valueColor = appGreen,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun HoldingStat(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Primary,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp2),
        horizontalAlignment = horizontalAlignment
    ) {
        Text(text = label, style = tinyLabel.copy(fontWeight = FontWeight.Normal, fontSize = 10.sp), color = SlateGray)
        Text(
            text = value.withInterRupee(),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = valueColor
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSchemesBottomSheetContent(
    portfolioState: UiState<List<SelectableSchemeUiModel>>,
    mapping: Boolean,
    effectFlow: Flow<MapSchemeEffect>,
    onEvent: (MapSchemeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var showSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                MapSchemeEffect.OpenBottomSheet -> showSheet = true
                MapSchemeEffect.CloseBottomSheet -> showSheet = false
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(MapSchemeEvent.CloseBottomSheet) },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            MapSchemesSheetBody(
                portfolioState = portfolioState,
                mapping = mapping,
                onEvent = onEvent,
                modifier = modifier
            )
        }
    }
}

@Composable
fun MapSchemesSheetBody(
    portfolioState: UiState<List<SelectableSchemeUiModel>>,
    onEvent: (MapSchemeEvent) -> Unit,
    modifier: Modifier = Modifier,
    mapping: Boolean = false
) {
    when (portfolioState) {
        is UiState.Error -> {
            ErrorScreen(
                errorMessage = portfolioState.message,
                onRetryClick = { onEvent(MapSchemeEvent.RetryPortfolio) }
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
                    text = "Map SIP",
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
                            onToggle = {
                                onEvent(MapSchemeEvent.ToggleSelection(scheme.holdingId))
                            }
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
                    onClick = { onEvent(MapSchemeEvent.MapSelectedHoldings) },
                    text = "Confirm Selection/ " + stringResource(Res.string.confirm_selection),
                    enabled = data.any { it.isSelected },
                    loading = mapping,
                    modifier = Modifier.fillMaxWidth()
                )
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
                style = MaterialTheme.typography.bodySmall,
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

private val previewHoldings = listOf(
    GoalHoldingDomain(
        holdingId = "holding-1",
        fundName = "HDFC Top 100 Fund – Growth",
        folioNumber = "123456",
        units = 150.25,
        nav = 332.8,
        currentValue = 50_000.0,
        imageUrl = null
    ),
    GoalHoldingDomain(
        holdingId = "holding-2",
        fundName = "ICICI Pru Bluechip – Direct Gr.",
        folioNumber = "884210",
        units = 85.50,
        nav = 98.4,
        currentValue = 35_000.0,
        imageUrl = null
    ),
    GoalHoldingDomain(
        holdingId = "holding-3",
        fundName = "Parag Parikh Flexi Cap Fund",
        folioNumber = "441092",
        units = 42.10,
        nav = 712.5,
        currentValue = 30_000.0,
        imageUrl = null
    )
)

private val previewGoalData = MapSchemeUiData(
    goalId = "4506b743-16f1-4678-adee-932fe02a4031",
    goalName = "Aarav's Education",
    holdings = previewHoldings,
    totalCurrentValue = 115_000.0
)

private val previewSchemes = listOf(
    SelectableSchemeUiModel(
        holdingId = "holding-4",
        name = "SBI Bluechip Fund - Direct Growth",
        units = "150.5432",
        value = 68620.0,
        folio = "123456789"
    ),
    SelectableSchemeUiModel(
        holdingId = "holding-5",
        name = "HDFC Mid-Cap Opportunities Fund - Direct Growth",
        units = "82.1145",
        value = 124350.0,
        folio = "987654321"
    ),
    SelectableSchemeUiModel(
        holdingId = "holding-6",
        name = "Axis Small Cap Fund - Direct Growth",
        units = "45.7788",
        value = 32110.0,
        folio = "741852963"
    )
)

@Preview()
@Composable
fun MapSchemesScreenPreview() {
    JantaNiveshTheme {
        MapSchemesScreen(
            uiState = MapSchemeUiState(
                goalDetailsState = UiState.Success(previewGoalData)
            ),
            effectFlow = emptyFlow(),
            onEvent = {},
            onBack = {}
        )
    }
}

@Preview
@Composable
fun MapSchemesScreenRemovingPreview() {
    JantaNiveshTheme {
        MapSchemesScreen(
            uiState = MapSchemeUiState(
                goalDetailsState = UiState.Success(previewGoalData),
                removingHoldingIds = setOf("holding-2")
            ),
            effectFlow = emptyFlow(),
            onEvent = {},
            onBack = {}
        )
    }
}

@Preview
@Composable
fun MapSchemesScreenEmptyPreview() {
    JantaNiveshTheme {
        MapSchemesScreen(
            uiState = MapSchemeUiState(
                goalDetailsState = UiState.Success(
                    previewGoalData.copy(holdings = emptyList(), totalCurrentValue = 0.0)
                )
            ),
            effectFlow = emptyFlow(),
            onEvent = {},
            onBack = {}
        )
    }
}

@Preview
@Composable
fun MapSchemesSheetLoadedPreview() {
    JantaNiveshTheme {
        MapSchemesSheetBody(
            portfolioState = UiState.Success(previewSchemes),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun MapSchemesSheetWithSelectionPreview() {
    JantaNiveshTheme {
        MapSchemesSheetBody(
            portfolioState = UiState.Success(
                previewSchemes.mapIndexed { index, scheme ->
                    scheme.copy(isSelected = index == 0)
                }
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun MapSchemesSheetEmptyPreview() {
    JantaNiveshTheme {
        MapSchemesSheetBody(
            portfolioState = UiState.Success(emptyList()),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun MapSchemesScreenWithSheetOpenPreview() {
    JantaNiveshTheme {
        MapSchemesScreen(
            uiState = MapSchemeUiState(
                goalDetailsState = UiState.Success(previewGoalData),
                portfolioDataState = UiState.Success(previewSchemes)
            ),
            effectFlow = flowOf(MapSchemeEffect.OpenBottomSheet),
            onEvent = {},
            onBack = {}
        )
    }
}

/**
 * The same screen with the mapping sheet open. `ModalBottomSheet` does not render in a preview,
 * so the sheet body is drawn over the screen exactly as the sheet presents it.
 */
@Preview
@Composable
fun MapSchemesScreenSheetOpenPreview() {
    JantaNiveshTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            MapSchemesScreen(
                uiState = MapSchemeUiState(
                    goalDetailsState = UiState.Success(previewGoalData),
                    portfolioDataState = UiState.Success(previewSchemes)
                ),
                effectFlow = emptyFlow(),
                onEvent = {},
                onBack = {}
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.32f))
            )
            MapSchemesSheetBody(
                portfolioState = UiState.Success(
                    previewSchemes.mapIndexed { index, scheme ->
                        scheme.copy(isSelected = index == 0)
                    }
                ),
                onEvent = {},
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(topStart = Spacing.dp28, topEnd = Spacing.dp28))
                    .background(Color.White)
            )
        }
    }
}
