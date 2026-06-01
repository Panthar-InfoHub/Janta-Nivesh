package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.core.platform.PdfViewer
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KycContractEvent
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KycContractUiState

@Preview
@Composable
fun KycContractScreenPreview() {
    KycContractScreen(
        state = KycContractUiState(),
        onEvent = {},
        onBack = {}
    )
}

@Composable
fun KycContractScreen(
    state: KycContractUiState,
    onEvent: (KycContractEvent) -> Unit,
    onBack: () -> Unit
) {
    val pdfViewer: PdfViewer = koinInject()
    LaunchedEffect(Unit) {
        if (state.contractPdfUrl == null) {
            onEvent(KycContractEvent.OnLoadContract)
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithBackButtonAndStepCount(
                stepCount = 4,
                totalSteps = 5,
                onBack = onBack,
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            )
        },
        bottomBar = {
            AppButton(
                text = "E-Sign Contract",
                onClick = { onEvent(KycContractEvent.OnStartESignClicked) },
                loading = state.isLoading,
                enabled = state.contractPdfUrl != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.dp24)
            )
        }
    ) { pv ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .padding(horizontal = Spacing.dp24),
            contentAlignment = Alignment.Center
        ) {
            if (state.contractPdfUrl != null) {
                pdfViewer.openPdf(state.contractPdfUrl)
            } else {
                Text(
                    text = "Generating your contract...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
