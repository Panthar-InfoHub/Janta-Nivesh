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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.jantanivesh_logo
import jantanivesh.shared.generated.resources.lock_icon
import jantanivesh.shared.generated.resources.tick_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.onboarding.data.models.LanguageOption
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ChooseLanguageEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ChooseLanguageEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ChooseLanguageUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ChooseLanguageViewModel
import org.velvetinvesting.jantanivesh.app.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.theme.GreyLock
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.PrimaryLanguageText
import org.velvetinvesting.jantanivesh.app.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.theme.SelectedTextColor
import org.velvetinvesting.jantanivesh.app.theme.White

@Composable
fun OnBoardingChooseLanguageRoute(
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChooseLanguageViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 2. Listen for Side Effects (Navigation)
    // LaunchedEffect runs safely in the background and cleans itself up
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ChooseLanguageEffect.NavigateToNextScreen -> {
                    // 3. Trigger the callback
                    onNavigateNext()
                }
            }
        }
    }

    // 4. Render the UI
    OnboardingChooseLanguage(
        state = uiState,
        onEvent = viewModel::handleEvent,
        modifier = modifier
    )
}

@Composable
fun OnboardingChooseLanguage(
    state: ChooseLanguageUiState,
    onEvent: (ChooseLanguageEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {

        Text("Choose Languages", style = MaterialTheme.typography.headlineLarge)
        Text(
            "English is your default primary language. Please select a\n" +
                    "secondary language.",
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
                .border(width = 1.dp, color = BoxBorder, shape = LocalShapes.current.roundedDp16)
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
                    contentDescription = "Lock Icon",
                    tint = GreyLock,
                    modifier = Modifier.size(21.dp)
                )
            }
        }

        Text("Secondary Language", style = MaterialTheme.typography.labelLarge, color = GreyText)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
            modifier = Modifier.weight(1.0f)
        ) {
            // Loop through your state data instead of hardcoding items!
            items(state.availableSecondaryLanguages.size) { index ->
                val language = state.availableSecondaryLanguages[index]

                LanguageCard(
                    language = language.nativeName,
                    languageSpelling = language.englishName,
                    isSelected = state.selectedLanguageId == language.id,
                    modifier = Modifier.clickable {
                        onEvent(ChooseLanguageEvent.OnLanguageSelected(language.id))
                    }
                )
            }
        }

        AppButton(
            text = "Continue",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )

        Image(
            painter = painterResource(Res.drawable.jantanivesh_logo),
            contentDescription = "Janta Nivesh Logo",
            modifier = Modifier
                .size(height = 58.dp, width = 115.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun LanguageCard(
    language: String,
    languageSpelling: String,
    isSelected: Boolean,
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
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isSelected) SelectedTextColor else Color.Black
                )
                Text(
                    languageSpelling,
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isSelected) SelectedTextColor else GreyText
                )
            }
            if (isSelected) {
                Icon(
                    painter = painterResource(Res.drawable.tick_icon),
                    contentDescription = "tick icon",
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
        selectedLanguageId = "hi" // Pre-select Hindi to test the UI state
    )

    JantaNiveshTheme {
        OnboardingChooseLanguage(
            state = dummyState,
            onEvent = {}, // Do nothing in preview
            modifier = Modifier.fillMaxSize()
        )
    }
}