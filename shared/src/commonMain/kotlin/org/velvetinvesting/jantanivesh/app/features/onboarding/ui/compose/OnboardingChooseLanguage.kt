package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import jantanivesh.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.models.LanguageOption
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ChooseLanguageEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ChooseLanguageEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ChooseLanguageUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ChooseLanguageViewModel
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
                .padding(horizontal = 24.dp, vertical = Spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {

            Text(
                stringResource(Res.string.choose_languages),
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                stringResource(Res.string.choose_languages_subtitle),
                color = GreyText
            )

            Text(
                stringResource(Res.string.primary_language),
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryLanguageText
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalShapes.current.roundedDp16)
                    .background(GreyBox)
                    .border(
                        width = 1.dp,
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
                            stringResource(Res.string.english),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )
                        Text(
                            stringResource(Res.string.default_system_language),
                            style = MaterialTheme.typography.titleSmall,
                            color = GreyText
                        )
                    }
                    Icon(
                        painter = painterResource(Res.drawable.lock_icon),
                        contentDescription = stringResource(Res.string.lock_icon_desc),
                        tint = GreyLock,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }

            Text(
                stringResource(Res.string.secondary_language),
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
                        language = language.nativeName,
                        languageSpelling = language.englishName,
                        isSelected = state.selectedLanguageId == language.id,
                        onClick = {
                            onEvent(ChooseLanguageEvent.OnLanguageSelected(language.id))
                        },
                    )
                }
            }

            AppButton(
                text = stringResource(Res.string.continue_text),
                onClick = {},
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
                    modifier = Modifier.height(58.dp)
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
            .border(width = 1.2.dp, color = borderColor, shape = LocalShapes.current.roundedDp16)
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
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(Spacing.dp4)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun OnboardingChooseLanguagePreview() {
    val mockLanguages = listOf(
        LanguageOption("hi", "हिन्दी", "Hindi"),
        LanguageOption("mr", "मराठी", "Marathi"),
        LanguageOption("gu", "ગુજરાતી", "Gujarati"),
        LanguageOption("ta", "தமிழ்", "Tamil"),
        LanguageOption("te", "తెలుగు", "Telugu"),
        LanguageOption("bn", "বাংলা", "Bengali")
    )

    val dummyState = ChooseLanguageUiState(
        availableSecondaryLanguages = mockLanguages,
        selectedLanguageId = "hi"
    )

    JantaNiveshTheme {
        OnboardingChooseLanguage(
            state = dummyState,
            onEvent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}