package org.velvetinvesting.jantanivesh.app.features.login.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.localization.model.AppLanguage
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageEvent
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageUiState
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.GreyLock
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.PrimaryLanguageText
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedTextColor
import org.velvetinvesting.jantanivesh.app.core.theme.White


@Composable
fun OnboardingChooseLanguage(
    state: ChooseLanguageUiState,
    onEvent: (ChooseLanguageEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = Spacing.dp24, vertical = Spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {

            Text(
                "Choose Languages",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                "English is your default primary language. Please select a secondary language.",
                color = GreyText
            )

            Text(
                "Primary Language",
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryLanguageText
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalShapes.current.roundedDp16)
                    .background(GreyBox)
                    .border(
                        width = Spacing.dp1,
                        color = BoxBorder,
                        shape = LocalShapes.current.roundedDp16
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(Spacing.dp20)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                        Text(
                            "English",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )
                        Text(
                           "Default System Language",
                            style = MaterialTheme.typography.titleSmall,
                            color = GreyText
                        )
                    }
                    Icon(
                        painter = painterResource(Res.drawable.lock_icon),
                        contentDescription = stringResource(Res.string.lock_icon_desc),
                        tint = GreyLock,
                        modifier = Modifier.size(Spacing.dp21)
                    )
                }
            }

            Text(
                "Secondary Language",
                style = MaterialTheme.typography.labelLarge,
                color = GreyText
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
                modifier = Modifier.weight(1.0f)
            ) {
                items(state.availableSecondaryLanguages.size) { index ->
                    val language = state.availableSecondaryLanguages[index]

                    LanguageCard(
                        language = language.displayName,
                        languageSpelling = language.englishName,
                        isSelected = state.selectedLanguage == language,
                        onClick = {
                            onEvent(ChooseLanguageEvent.OnLanguageSelected(language))
                        },
                    )
                }
            }

            AppButton(
                text = stringResource(Res.string.continue_text),
                onClick = { onEvent(ChooseLanguageEvent.OnContinueClicked) },
                modifier = Modifier.fillMaxWidth(),
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.dp16),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.jantanivesh_logo),
                    contentDescription = stringResource(Res.string.janta_nivesh_logo_desc),
                    modifier = Modifier.height(Spacing.dp58)
                )
            }
        }
    }
}

@Composable
private fun LanguageCard(
    language: String,
    languageSpelling: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) SelectedBoxColor else White
    val borderColor = if (isSelected) SelectedBoxBorder else BoxBorder

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(LocalShapes.current.roundedDp16)
            .background(backgroundColor)
            .border(width = Spacing.dp1_2, color = borderColor, shape = LocalShapes.current.roundedDp16)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp16, vertical = Spacing.dp20)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                Text(
                    language,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isSelected) SelectedTextColor else Color.Black
                )
                Text(
                    languageSpelling,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) SelectedTextColor else GreyText
                )
            }
            if (isSelected) {
                Icon(
                    painter = painterResource(Res.drawable.tick_icon),
                    contentDescription = stringResource(Res.string.tick_icon_desc),
                    tint = White,
                    modifier = Modifier
                        .size(Spacing.dp22)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(Spacing.dp4)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, locale = "te")
fun OnboardingChooseLanguagePreview() {
    val mockLanguages = listOf(
        AppLanguage.HINDI,
        AppLanguage.MARATHI,
        AppLanguage.GUJARATI,
        AppLanguage.TAMIL,
        AppLanguage.TELUGU,
        AppLanguage.BENGALI
    )

    val dummyState = ChooseLanguageUiState(
        availableSecondaryLanguages = mockLanguages,
        selectedLanguage = AppLanguage.DEFAULT
    )

    JantaNiveshTheme {
        OnboardingChooseLanguage(
            state = dummyState,
            onEvent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}