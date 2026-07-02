package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.Gray45
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileLanguageEvent
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileLanguageUiState

@Composable
fun ProfileLanguageScreen(
    state: ProfileLanguageUiState,
    onEvent:(ProfileLanguageEvent)-> Unit
) {
    Scaffold(
        containerColor = White
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(Spacing.dp16)) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(
                    bottom = Spacing.dp16)
            ) {
                item {
                    BackHeader("Set Secondary Language", onBack = {onEvent(ProfileLanguageEvent.OnBackClicked)}, true)
                }
                item {
                    Text(
                        "Choose your preferred language for navigating the app. You can switch this to a secondary language anytime in your settings.",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Normal,
                        color = Gray45
                    )
                }



                items(state.languages) { lang ->
                    Box(
                        modifier = Modifier.genericDropShadow(RoundedCornerShape(Spacing.dp8))
                            .fillMaxWidth().clip(RoundedCornerShape(Spacing.dp8))
                            .clickable(
                                onClick = {onEvent(ProfileLanguageEvent.OnLanguageSelected(lang))}
                            )
                            .background(color = White).padding(Spacing.dp12)
                    ) {
                        Row {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    lang.displayName,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    lang.englishName,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal)
                                )
                            }
                            RadioButton(onClick = {onEvent(ProfileLanguageEvent.OnLanguageSelected(lang))}, selected = state.selectedLanguage==lang)
                        }

                    }
                }

            }
            AppButton("Save Changes", onClick = {onEvent(ProfileLanguageEvent.OnSaveClicked)}, modifier = Modifier.fillMaxWidth())

        }
    }
}


@Preview( showBackground = true)
@Composable
fun ProfilePreview2() {
    JantaNiveshTheme {
        ProfileLanguageScreen(onEvent = {}, state = ProfileLanguageUiState())
    }
}