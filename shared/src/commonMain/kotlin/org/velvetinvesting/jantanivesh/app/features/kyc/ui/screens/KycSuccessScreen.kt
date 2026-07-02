package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.tick_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter

@Preview(locale = "hi")
@Composable
fun KycSuccessScreenPreview() {
    JantaNiveshTheme{
        KycSuccessScreen({}) {}
    }
}

@Composable
fun KycSuccessScreen(
    onBackClick: () -> Unit,
    onTradingAccountSetupClick: () -> Unit
) {
    Scaffold(
        containerColor = White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            BackHeader(
                title = "",
                onBack = onBackClick,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.dp16)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(horizontal = Spacing.dp24),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.tick_icon),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp64)
                )
                Text(
                    text = "KYC Completed Successfully!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = Spacing.dp24)
                )
                Text(
                    text = "Your account is now ready. You can start investing in mutual funds after setting up your trading account.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = Spacing.dp16)
                )
            }
            NextButtonFooter(
                value = "Setup Trading Account",
                onClick = onTradingAccountSetupClick,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
