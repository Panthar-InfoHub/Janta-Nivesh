package org.velvetinvesting.jantanivesh.app.features.bundles.presentation.compose

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.invest
import jantanivesh.shared.generated.resources.ic_pointer_right
import jantanivesh.shared.generated.resources.ic_veritcal_tilted_arrow
import jantanivesh.shared.generated.resources.up_stock
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardSubtitle
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardTitle
import org.velvetinvesting.jantanivesh.app.core.theme.Gray45
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.IconSize
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LightBlue
import org.velvetinvesting.jantanivesh.app.core.theme.LightBlueBorder
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.presentation.viewmodel.AllBundlesEvent
import org.velvetinvesting.jantanivesh.app.features.bundles.presentation.viewmodel.AllBundlesUiState
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow

@Composable
fun AllBundlesScreen(
    state: AllBundlesUiState,
    onEvent: (AllBundlesEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackHeader(
                title = "Janta Bundles",
                onBack = { onEvent(AllBundlesEvent.OnBackClicked) },
                modifier = Modifier.padding(horizontal = Spacing.dp16)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            PurchaseModeSelector(
                selected = state.purchaseMode,
                onSelected = { onEvent(AllBundlesEvent.OnPurchaseModeSelected(it)) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when {
                    state.isLoading && state.bundles.isEmpty() -> LoaderScreen()

                    state.error != null && state.bundles.isEmpty() -> ErrorScreen(
                        errorMessage = state.error,
                        onRetryClick = { onEvent(AllBundlesEvent.Retry) }
                    )

                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        itemsIndexed(state.bundles, key = { _, bundle -> bundle.id }) { _, bundle ->
                            BundleCardAll(
                                bundle = bundle,
                                // NEW: the card's amount follows the selected way of investing.
                                purchaseMode = state.purchaseMode,
                                onClick = { onEvent(AllBundlesEvent.OnBundleClicked(bundle.id)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
                        }
                    }
                }
            }
        }
    }
}

/** Monthly first: it is the default, and the way most users start. */
private val selectorModes = listOf(PurchaseMode.MONTHLY, PurchaseMode.DAILY, PurchaseMode.ONE_TIME)

private fun PurchaseMode.selectorTitle(): String = when (this) {
    PurchaseMode.MONTHLY -> "Monthly SIP"
    PurchaseMode.DAILY -> "Daily SIP"
    PurchaseMode.ONE_TIME -> "One-time"
}

private fun PurchaseMode.selectorSubtitle(): String = when (this) {
    PurchaseMode.MONTHLY -> "Wealth builder"
    PurchaseMode.DAILY -> "From ₹10/day"
    PurchaseMode.ONE_TIME -> "Lumpsum"
}

/** Segmented control for how to invest; the selected mode sits on a raised white pill. */
@Composable
private fun PurchaseModeSelector(
    selected: PurchaseMode,
    onSelected: (PurchaseMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackShape = LocalShapes.current.roundedDp16
    val pillShape = LocalShapes.current.roundedDp12

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(trackShape)
            .background(LightBlue)
            .padding(Spacing.dp6),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp4)
    ) {
        selectorModes.forEach { mode ->
            val isSelected = mode == selected
            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (isSelected) {
                            Modifier
                                .genericDropShadow(pillShape)
                                .clip(pillShape)
                                .background(White)
                                .border(Spacing.dp1, LightBlueBorder.copy(alpha = 0.4f), pillShape)
                        } else {
                            Modifier.clip(pillShape)
                        }
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onSelected(mode) }
                    .padding(vertical = Spacing.dp12, horizontal = Spacing.dp6),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = mode.selectorTitle(),
                    style = tinyLabel.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected) Black else Gray45,
                    maxLines = 1
                )
                Text(
                    text = mode.selectorSubtitle(),
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreyText,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun BundleCardAll(
    bundle: BundleSummaryDomain,
    // NEW
    purchaseMode: PurchaseMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BundleCardContainer(
        bundle = bundle,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(bottom = Spacing.dp12)
        ) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp4),
            ) {
                Text(
                    text = bundle.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = BundleCardTitle
                )
                if (bundle.description.isNotBlank()) {
                    Text(
                        text = bundle.description,
                        style = tinyLabel.copy(fontWeight = FontWeight.Normal),
                        color = BundleCardSubtitle
                    )
                }
            }
            Row(
                modifier = Modifier
                    .clip(
                        LocalShapes.current.circle
                    )
                    .background(White)
                    .border(
                        width = 1.dp,
                        color = bundle.cardBorderColor,
                        shape = LocalShapes.current.circle
                    )
                    .padding(
                        horizontal = Spacing.dp12,
                        vertical = Spacing.dp4
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp2)
            ) {
                Text(
                    text = bundle.metaData.investmentGrowth,
                    style = tinyLabel,
                    color = appGreen
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.dp20)
                .height(1.dp)
                .background(White)
        )

        BundleInfoRowAll(
            onClick = onClick,
            modifier = Modifier.padding(top = Spacing.dp8)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // NEW: label, amount and suffix all follow the selected mode.
                val startAmount = bundle.metaData.startAmountFor(purchaseMode)
                Text(
                    text = purchaseMode.minAmountLabel(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp
                    ),
                    color = BundleCardSubtitle
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Black
                            )
                        ) {
                            // NEW: a dash when the API didn't send this mode's amount.
                            append(startAmount?.let { "₹$it" } ?: "—")
                        }
                        if (startAmount != null) append(purchaseMode.amountSuffix())
                    },
                    style = tinyLabel,
                    color = BundleCardSubtitle,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${bundle.fundCount} Mutual Funds",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.sp
                    ),
                    color = BundleCardSubtitle
                )
                if (bundle.assetClassLabel.isNotBlank()) {
                    Text(
                        text = bundle.assetClassLabel,
                        style = tinyLabel,
                        color = BundleCardSubtitle,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// NEW: the card's minimum label for each mode.
private fun PurchaseMode.minAmountLabel(): String = when (this) {
    PurchaseMode.MONTHLY -> "MIN. MONTHLY SIP"
    PurchaseMode.DAILY -> "MIN. DAILY SIP"
    PurchaseMode.ONE_TIME -> "MIN. ONE-TIME"
}

// NEW: what follows the amount; a one-time amount has no period.
private fun PurchaseMode.amountSuffix(): String = when (this) {
    PurchaseMode.MONTHLY -> "/mo"
    PurchaseMode.DAILY -> "/day"
    PurchaseMode.ONE_TIME -> ""
}

@Composable
private fun BundleInfoRowAll(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f)
        ) { content() }
        Spacer(modifier = Modifier.width(Spacing.dp12))
        Button(
            onClick = onClick,
            shape = LocalShapes.current.roundedDp12,
            colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = White),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Invest/" + stringResource(Res.string.invest),
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_pointer_right),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.padding(start = 4.dp).size(12.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AllBundlesScreenPreview() {
    JantaNiveshTheme {
        AllBundlesScreen(
            state = AllBundlesUiState(
                bundles = listOf(
                    previewBundleSummary,
                    previewBundleSummary.copy(
                        id = "2",
                        name = "Aggressive",
                        description = "Velvet Long Term Vision"
                    )
                ),
                cartAmount = 5000
            ),
            onEvent = {}
        )
    }
}
