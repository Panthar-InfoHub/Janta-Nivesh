package org.velvetinvesting.jantanivesh.app.features.search.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.back_arrow
import jantanivesh.shared.generated.resources.ic_clock
import jantanivesh.shared.generated.resources.profile_bank
import jantanivesh.shared.generated.resources.search_icon
import jantanivesh.shared.generated.resources.upward_trend_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.MutualFundIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.PrimaryContainer
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.utils.AppBackHandler
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.search.domain.model.RecentSearch
import org.velvetinvesting.jantanivesh.app.features.search.ui.viewmodels.SearchOverlayEvent
import org.velvetinvesting.jantanivesh.app.features.search.ui.viewmodels.SearchOverlayUiState

/**
 * The full-screen search surface.
 *
 * It is drawn as an overlay on top of whichever screen opened it rather than as its own
 * destination: the screen underneath keeps its scroll position and its loaded data, so closing
 * the search is instant and nothing is re-fetched.
 *
 * The field takes focus as soon as it appears — the user tapped a search bar to get here, so
 * making them tap a second one would be a wasted step.
 */
@Composable
fun SearchOverlay(
    state: SearchOverlayUiState,
    handleEvent: (SearchOverlayEvent) -> Unit,
    onDismiss: () -> Unit,
    onStartSipClick: () -> Unit,
    onBookFdClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppBackHandler(enabled = true, onBack = onDismiss)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .imePadding()
    ) {
        SearchField(
            query = state.query,
            onQueryChange = { handleEvent(SearchOverlayEvent.OnQueryChange(it)) },
            onSubmit = { handleEvent(SearchOverlayEvent.OnSubmit) },
            onDismiss = onDismiss
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.dp20),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
            if (state.showRecents) {
                RecentSearchesCard(
                    searches = state.recentSearches,
                    onRecentClick = { handleEvent(SearchOverlayEvent.OnRecentClick(it)) },
                    onClearClick = { handleEvent(SearchOverlayEvent.OnClearRecents) }
                )
            }

            QuickActions(
                onStartSipClick = onStartSipClick,
                onBookFdClick = onBookFdClick
            )
        }
    }
}

/**
 * The real input. A [BasicTextField] rather than the outlined `AppSearchBar`, because this one
 * is a filled pill with the magnifier leading — and it is the only typable search field in the
 * flow now that the one on the fund list is a button.
 */
@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.dp16, vertical = Spacing.dp12),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.back_arrow),
            contentDescription = "Close search",
            tint = Primary,
            modifier = Modifier
                .size(Spacing.dp22)
                .clickable(
                    onClick = onDismiss,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .height(Spacing.dp52)
                .clip(RoundedCornerShape(Spacing.dp12))
                .background(GreyBox)
                .padding(horizontal = Spacing.dp16),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.search_icon),
                contentDescription = null,
                tint = if (query.isBlank()) GreyText else Primary,
                modifier = Modifier
                    .size(Spacing.dp20)
                    .clickable(
                        onClick = onSubmit,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                if (query.isEmpty()) {
                    Text(
                        text = SEARCH_PLACEHOLDER,
                        style = MaterialTheme.typography.bodyLarge,
                        color = GreyText
                    )
                }

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Black),
                    cursorBrush = SolidColor(Secondary),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }
        }
    }
}

private const val SEARCH_PLACEHOLDER = "Search Mutual funds, FDs..."

/** Terms the user has actually searched, newest first. */
@Composable
private fun RecentSearchesCard(
    searches: List<RecentSearch>,
    onRecentClick: (String) -> Unit,
    onClearClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(GreyBox)
            .padding(vertical = Spacing.dp20, horizontal = Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_clock),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(Spacing.dp18)
            )
            Text(
                text = "Recent Searches",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Primary,
                modifier = Modifier.weight(1f)
            )
            Text(
                // A recents list the user cannot empty is a list they are stuck with.
                text = "Clear",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Secondary,
                modifier = Modifier.clickable(
                    onClick = onClearClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            )
        }

        searches.forEach { search ->
            RecentSearchRow(
                query = search.query,
                onClick = { onRecentClick(search.query) }
            )
        }
    }
}

@Composable
private fun RecentSearchRow(
    query: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = Spacing.dp6),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.search_icon),
            contentDescription = null,
            tint = GreyText,
            modifier = Modifier.size(Spacing.dp16)
        )
        Text(
            text = query,
            style = MaterialTheme.typography.bodyLarge,
            color = Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** Two shortcuts for users who came to the search without a term in mind. */
@Composable
private fun QuickActions(
    onStartSipClick: () -> Unit,
    onBookFdClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            QuickActionCard(
                icon = Res.drawable.upward_trend_icon,
                iconBackground = PrimaryContainer,
                title = "Start a SIP",
                subtitle = "Invest monthly",
                onClick = onStartSipClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                icon = Res.drawable.profile_bank,
                iconBackground = MutualFundIconBg,
                title = "Book FD",
                subtitle = "Secure returns",
                onClick = onBookFdClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    icon: DrawableResource,
    iconBackground: androidx.compose.ui.graphics.Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .genericDropShadow(RoundedCornerShape(Spacing.dp16))
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .clickable(onClick = onClick)
            .padding(Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        Box(
            modifier = Modifier
                .size(Spacing.dp40)
                .clip(CircleShape)
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(Spacing.dp20)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp2)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = GreyText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun SearchOverlayPreview() {
    JantaNiveshTheme {
        SearchOverlay(
            state = SearchOverlayUiState(
                query = "",
                recentSearches = listOf(
                    RecentSearch("SBI Fixed Deposit", 4),
                    RecentSearch("HDFC Mid Cap Fund", 3),
                    RecentSearch("Top ELSS Funds 2024", 2),
                    RecentSearch("Gold ETF", 1)
                )
            ),
            handleEvent = {},
            onDismiss = {},
            onStartSipClick = {},
            onBookFdClick = {}
        )
    }
}
