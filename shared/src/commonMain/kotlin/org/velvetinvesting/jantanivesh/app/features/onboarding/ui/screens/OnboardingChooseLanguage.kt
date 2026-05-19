package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.jantanivesh_logo
import jantanivesh.shared.generated.resources.lock_icon
import jantanivesh.shared.generated.resources.tick_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.features.core.composables.ScreenWideButton
import org.velvetinvesting.jantanivesh.app.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.theme.GreyLock
import org.velvetinvesting.jantanivesh.app.theme.GreyText
import org.velvetinvesting.jantanivesh.app.theme.Primary
import org.velvetinvesting.jantanivesh.app.theme.PrimaryLanguageText
import org.velvetinvesting.jantanivesh.app.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.theme.SelectedTextColor
import org.velvetinvesting.jantanivesh.app.theme.White

@Composable
fun OnboardingChooseLanguage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Choose Languages", style = MaterialTheme.typography.headlineLarge)
        Text(
            "English is your default primary language. Please select a\n" +
                    "secondary language.",
            color = GreyText
        )

        Text("Primary Language", style = MaterialTheme.typography.labelLarge, color = PrimaryLanguageText)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(GreyBox)
                .border(width = 1.dp, color = BoxBorder, shape = RoundedCornerShape(16.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("English", style = MaterialTheme.typography.labelLarge, color = Color.Black)
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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1.0f)
        ) {
            item { LanguageCard("हिन्दी", "Hindi", isSelected = true) }
            item { LanguageCard("मराठी", "Marathi", isSelected = false) }
            item { LanguageCard("ગુજરાતી", "Gujarati", isSelected = false) }
            item { LanguageCard("தமிழ்", "Tamil", isSelected = false) }
            item { LanguageCard("తెలుగు", "Telugu", isSelected = false) }
            item { LanguageCard("বাংলা", "Bengali", isSelected = false) }
        }

        ScreenWideButton(
            buttonText = "Continue",
            onClick = {},
            color = Primary,
            modifier = Modifier.fillMaxWidth()
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
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(width = 1.2.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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
                        .background(color = Primary)
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun OnboardingChooseLanguagePreview() {
    OnboardingChooseLanguage(Modifier.fillMaxSize())
}