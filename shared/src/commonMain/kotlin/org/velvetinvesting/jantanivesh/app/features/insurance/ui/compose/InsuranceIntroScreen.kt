package org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.forward_icon
import jantanivesh.shared.generated.resources.general_insurance
import jantanivesh.shared.generated.resources.health_insurance
import jantanivesh.shared.generated.resources.term_insurance
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GrayIcon
import org.velvetinvesting.jantanivesh.app.core.theme.InsuranceBoxBackground
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.InsuranceEvent
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.InsuranceUiState
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.AddYourEmailScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.OnboardingUiState

@Composable
fun InsuranceIntroScreen(
    state: InsuranceUiState,
    onEvent: (InsuranceEvent) -> Unit,
    modifier: Modifier = Modifier

) {
    Scaffold { pv ->
        LazyColumn(
            modifier.fillMaxSize().padding(horizontal = Spacing.dp16).padding(top = 40.dp),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16), contentPadding = PaddingValues(bottom =
                Spacing.dp16)
        ) {
           item {
               InsuranceBanner(
                   Modifier,
                   onRequestCallbackClick = { onEvent(InsuranceEvent.OnRequestCallbackClick) })
           }
            item {
                Text(
                    "Explore Products",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Black
                )
            }
            item {
                ExploreProductCategoryCard(
                    "Term Insurance",
                    Res.drawable.term_insurance,
                    { onEvent(InsuranceEvent.OnTermClicked) })
            }
            item {
                ExploreProductCategoryCard(
                    "Health Insurance",
                    Res.drawable.health_insurance,
                    onClick = {
                        onEvent(
                            InsuranceEvent.OnHealthClicked
                        )
                    })
            }
item {
    ExploreProductCategoryCard(
        "General Insurance",
        Res.drawable.general_insurance,
        onClick = {
            onEvent(
                InsuranceEvent.OnGeneralClicked
            )
        })
}
        }
    }
}


@Composable
fun InsuranceBanner(modifier: Modifier = Modifier, onRequestCallbackClick: () -> Unit) {
    Box(
        modifier.fillMaxWidth().clip(RoundedCornerShape(Spacing.dp12))
            .background(color = Primary, shape = RoundedCornerShape(12.dp)).padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
            Text(
                "Talk to an insurance expert", style = MaterialTheme.typography.headlineSmall,
                color = White
            )
            Text(
                "Get personalized advice to secure your future.",
                style = MaterialTheme.typography.bodyMedium,
                color = White
            )
            Box(
                Modifier.clip(RoundedCornerShape(Spacing.dp8))
                    .background(color = White, shape = RoundedCornerShape(Spacing.dp8)).padding(
                        horizontal = Spacing.dp16, vertical = Spacing.dp8
                    ).clickable { onRequestCallbackClick() }
            ) {
                Text(
                    "Request Callback",
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary,
                )
            }
        }
    }
}

@Composable
fun ExploreProductCategoryCard(text: String, icon: DrawableResource, onClick: () -> Unit) {
    Box(
        Modifier.fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(Spacing.dp24)).clip(RoundedCornerShape(24.dp))
            .background(color = White).clickable { onClick() }.padding(Spacing.dp16)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.background(
                    color = InsuranceBoxBackground,
                    shape = RoundedCornerShape(12.dp)
                ).size(48.dp), contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "term insurance icon",
                    tint = Primary
                )
            }
            Text(
                text,
                style = MaterialTheme.typography.labelLarge,
                color = Black,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(Res.drawable.forward_icon),
                contentDescription = "forward icon",
                tint = GrayIcon
            )

        }
    }
}


@Preview(showBackground = true, locale = "hi")
@Composable
fun InsuranceScreenPreview() {
    JantaNiveshTheme {
        InsuranceIntroScreen(
            state = InsuranceUiState(),
            onEvent = {}
        )

    }
}

