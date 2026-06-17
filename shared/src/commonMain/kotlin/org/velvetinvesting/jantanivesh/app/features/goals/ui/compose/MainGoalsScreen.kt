package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.goals_splash
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.YourGoalsEvent

@Preview(showBackground = true)
@Composable
fun MainGoalsScreenPreview() {
    JantaNiveshTheme {
        MainGoalsScreen(
            pv = PaddingValues(0.dp),
            onBackClick = {},
            handleEvent = {}
        )
    }
}

@Composable
fun MainGoalsScreen(
    pv: PaddingValues,
    onBackClick: () -> Unit,
    handleEvent: (YourGoalsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(pv)
            .padding(horizontal = Spacing.dp16)
    ) {
        BackHeader(
            onBack = onBackClick,
            title = "Your Goals",
            modifier = Modifier.statusBarsPadding()
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = Spacing.dp16)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.dp48),
            ) {
                Image(
                    painter = painterResource(Res.drawable.goals_splash),
                    contentDescription = null,
                    modifier = Modifier.size(Spacing.dp285)
                )
                Text(
                    text = "Set clear financial goals and take small steps each month to achieve them!",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )
            }
        }

        AppButton(
            text = "Set a Goal",
            onClick = { handleEvent(YourGoalsEvent.OnAddGoalClicked) },
            modifier = Modifier
                .genericDropShadow()
                .fillMaxWidth()
                .navigationBarsPadding(),
            style = AppButtonDefaults.style(shape = RoundedCornerShape(Spacing.dp16))
        )
    }
}