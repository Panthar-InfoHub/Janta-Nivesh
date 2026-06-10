package org.velvetinvesting.jantanivesh.app.features.bottonNavigation.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.arrow_forward_short_icon
import jantanivesh.shared.generated.resources.monument_icon
import jantanivesh.shared.generated.resources.mutual_funds_icon
import jantanivesh.shared.generated.resources.piggybank_icon
import jantanivesh.shared.generated.resources.upward_trend_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.DarkNavy
import org.velvetinvesting.jantanivesh.app.core.theme.FdIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.GradientEnd
import org.velvetinvesting.jantanivesh.app.core.theme.GrayBackGround
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LightGrayBorder
import org.velvetinvesting.jantanivesh.app.core.theme.MutualFundIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.PercentageGreenBg
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SecondaryPrimary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.SlateGray
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.domain.models.FixedTopPicksUiModel
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.domain.models.MutualFundTopPicksUiModel
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.ui.viewmodels.ExploreFundsEvent
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.ui.viewmodels.ExploreFundsUiState
import org.velvetinvesting.jantanivesh.app.features.bottonNavigation.ui.viewmodels.ExploreFundsViewModel
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow

@Preview(heightDp = 1500)
@Composable
fun ExploreFundsScreenPreview() {
    JantaNiveshTheme {
        Scaffold { paddingValues ->
            ExploreFundsScreen(
                uiState = ExploreFundsUiState(
                    mutualFundList = listOf(
                        MutualFundTopPicksUiModel(
                            icon = "",
                            name = "SBI Gold Fund",
                            metadata = "Equity, Sectoral/Thematic, High Risk",
                            returnYears = 3,
                            percentage = 18.5,
                            id = "1"
                        )
                    ),
                    fixedDepositList = listOf(
                        FixedTopPicksUiModel(
                            icon = "",
                            name = "SBI Bank",
                            metadata = "LOW RISK",
                            percentage = 7.25,
                            id = "2"
                        )
                    )
                ),
                handleEvent = {},
                modifier = Modifier.padding(paddingValues).padding(Spacing.dp16)
            )
        }
    }
}

@Composable
fun ExploreFundsScreen(
    uiState: ExploreFundsUiState,
    handleEvent: (ExploreFundsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.dp24), modifier = modifier) {
        item {
            Text(
                "Want to invest", style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp24)) {
                IconButtonCard(
                    onClick = { handleEvent(ExploreFundsEvent.OnMutualFundsCategoryClick) },
                    title = "Mutual Funds",
                    icon = Res.drawable.mutual_funds_icon,
                    iconBackground = FdIconBg,
                    iconColor = Primary,
                    modifier = Modifier.weight(1f)
                )
                IconButtonCard(
                    onClick = { handleEvent(ExploreFundsEvent.OnFixedDepositCategoryClick) },
                    title = "Fixed Deposit",
                    icon = Res.drawable.monument_icon,
                    iconBackground = MutualFundIconBg,
                    iconColor = SecondaryPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Row(modifier = Modifier.padding(vertical = Spacing.dp24)) {
                Text(
                    "Top Picks ", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Mutual Funds", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = SelectedBoxBorder
                )
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                items(uiState.mutualFundList) { fund ->
                    TopPicksMfCard( // TODO adjust Sizing of composable
                        fund = fund,
                        onInvestClick = { handleEvent(ExploreFundsEvent.OnMutualFundInvestClick(fund.id)) }
                    )
                }
            }
        }
        item {
            Row(modifier = Modifier.padding(vertical = Spacing.dp24)) {
                Text(
                    "Top Picks ", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Fixed Deposit", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = SelectedBoxBorder
                )
            }
        }
        items(uiState.fixedDepositList) { fd ->
            TopPicksFixedDepositCard(
                fund = fd,
                onClick = { handleEvent(ExploreFundsEvent.OnFixedDepositClick(fd.id)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            StartWealthCard()
        }
    }
}

@Composable
private fun IconButtonCard(
    onClick: () -> Unit,
    title: String,
    icon: DrawableResource,
    iconColor: Color,
    iconBackground: Color,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.genericDropShadow(RoundedCornerShape(Spacing.dp24))
            .clip(
                RoundedCornerShape(Spacing.dp24)
            )
            .background(color = White)
            .clickable(onClick = onClick)
            .padding(vertical = Spacing.dp24)
    ) {
        Icon(
            painterResource(icon),
            contentDescription = title,
            modifier = Modifier.size(Spacing.dp53).clip(RoundedCornerShape(Spacing.dp16))
                .background(iconBackground)
                .padding(Spacing.dp14),
            tint = iconColor
        )
        Text(
            title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Black
        )
    }
}

@Composable
fun TopPicksMfCard(
    fund: MutualFundTopPicksUiModel,
    onInvestClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
        modifier = modifier.fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(Spacing.dp24))
            .clip(RoundedCornerShape(Spacing.dp24))
            .border(width = Spacing.dp1, color = LightGrayBorder).background(White)
            .padding(horizontal = Spacing.dp16, vertical = Spacing.dp24)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.size(44.dp), model = fund.icon, contentDescription = null,

                loading = {
                    MutualFundIcon(
                        backgroundColor = GrayBackGround,
                        textColor = Primary,
                        schemeName = fund.name, size = 44.dp
                    )
                },

                error = {
                    MutualFundIcon(
                        backgroundColor = GrayBackGround,
                        textColor = Primary,
                        schemeName = fund.name, size = 44.dp
                    )
                },

                success = {
                    SubcomposeAsyncImageContent()
                }
            )
            if (fund.metadata.contains("High Risk")) {
                Text(
                    "HIGH RISK",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clip(RoundedCornerShape(Spacing.dp8))
                        .background(color = MaterialTheme.colorScheme.errorContainer)
                        .padding(horizontal = Spacing.dp8, vertical = Spacing.dp4)
                )
            } else if (fund.metadata.contains("Low Risk")) {
                Text(
                    "LOW RISK",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
            Text(
                fund.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                fund.metadata,
                style = MaterialTheme.typography.labelSmall,
                color = GreyText
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().border(
                width = Spacing.dp1,
                color = LightGrayBorder,
                shape = RoundedCornerShape(Spacing.dp12)
            ).padding(Spacing.dp16)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                Text(
                    "${fund.returnYears}Y RETURNS",
                    style = MaterialTheme.typography.labelSmall,
                    color = GreyText
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        "${fund.percentage}%",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = SelectedBoxBorder
                    )
                    Text(
                        " p.a.",
                        style = MaterialTheme.typography.labelSmall,
                        color = GreyText.copy(alpha = 0.5f)
                    )
                }
            }
            Icon(
                painter = painterResource(Res.drawable.upward_trend_icon),
                contentDescription = null,
                modifier = Modifier.height(Spacing.dp64),
                tint = SecondaryPrimary
            )
        }
        TextButton(
            onClick = onInvestClick,
            shape = RoundedCornerShape(Spacing.dp8),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = White
            ),
            modifier = Modifier.fillMaxWidth().padding(Spacing.dp16)
        ) {
            Text(
                "Invest Now →",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Composable
fun TopPicksFixedDepositCard(
    fund: FixedTopPicksUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
        modifier = modifier.genericDropShadow().clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .clickable(onClick = onClick)
            .padding(Spacing.dp20)
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.size(44.dp),
            model = fund.icon,
            contentDescription = null,

            loading = {
                MutualFundIcon(
                    letterNum = 2,
                    backgroundColor = GrayBackGround,
                    textColor = Primary,
                    schemeName = fund.name,
                    size = 40.dp,
                    cornerRadius = Spacing.dp48
                )
            },

            error = {
                MutualFundIcon(
                    letterNum = 2,
                    backgroundColor = GrayBackGround,
                    textColor = Primary,
                    schemeName = fund.name, size = 40.dp,
                    cornerRadius = Spacing.dp48
                )
            },

            success = {
                SubcomposeAsyncImageContent()
            }
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp4),
            modifier = Modifier.weight(0.5f)
        ) {
            Text(fund.name, style = MaterialTheme.typography.labelLarge)
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp4),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.background(
                        color = PercentageGreenBg,
                        RoundedCornerShape(Spacing.dp6)
                    ).padding(horizontal = Spacing.dp8, vertical = Spacing.dp4)
                ) {
                    Text(
                        "${fund.percentage}%",
                        style = MaterialTheme.typography.labelLarge,
                        color = SelectedBoxBorder
                    )
                    Text(
                        " p.a.",
                        style = MaterialTheme.typography.titleSmall,
                        color = DarkNavy
                    )
                }
                if (fund.metadata.contains("High Risk", ignoreCase = true)) {
                    Text(
                        "HIGH RISK",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = GreyText
                    )
                } else if (fund.metadata.contains("Low Risk", ignoreCase = true)) {
                    Text(
                        "LOW RISK",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = GreyText
                    )
                }
            }
        }
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(Res.drawable.arrow_forward_short_icon),
                contentDescription = null,
                modifier = Modifier.weight(0.5f),
                tint = SlateGray
            )
        }
    }
}

@Composable
fun StartWealthCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Primary, GradientEnd),
                    start = Offset.Zero,
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = RoundedCornerShape(32.dp)
            )
            .padding(Spacing.dp32),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
        ) {
            Text(
                "Start your wealth journey today",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = White
            )
            Text(
                "Secure, transparent, and built for your future.",
                style = MaterialTheme.typography.titleSmall.copy(
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = White.copy(alpha = 0.8f)
            )
        }
        Icon(
            painter = painterResource(Res.drawable.piggybank_icon),
            contentDescription = null,
            modifier = Modifier.size(100.dp).padding(start = Spacing.dp32),
            tint = White.copy(alpha = 0.15f)
        )
    }
}

@Composable
fun MutualFundIcon(
    letterNum: Int = 1,
    schemeName: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    cornerRadius: Dp = 12.dp,
    backgroundColor: Color = Primary,
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = schemeName
                .take(letterNum).capitalize(Locale.current),
            style = MaterialTheme.typography.headlineSmall,
            color = textColor
        )
    }
}