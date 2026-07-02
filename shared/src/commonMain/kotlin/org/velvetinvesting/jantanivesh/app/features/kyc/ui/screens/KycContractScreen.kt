package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.ic_eye
import jantanivesh.shared.generated.resources.upload_signature_icon
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.core.platform.PdfViewer
import org.velvetinvesting.jantanivesh.app.core.theme.Border
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.TextPrimary
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoadingScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KycContractEvent
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KycContractUiState

@Composable
fun KycContractScreen(
    state: KycContractUiState,
    onEvent: (KycContractEvent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = White
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            BackHeader(
                title = "KYC Confirmation",
                onBack = onBack,
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            )

            when {
                state.isContractLoading -> {
                    LoadingScreen(
                        modifier = Modifier.weight(1f)
                    )
                }

                state.showError -> {
                    ErrorScreen(
                        errorMessage = state.errorMessage,
                        modifier = Modifier.weight(1f),
                        onRetryClick = {
                            onEvent(KycContractEvent.OnLoadContract)
                        }
                    )
                }

                else -> {
                    KycContractContent(
                        contractPdfUrl = state.contractPdfUrl,
                        isMarkedAsRead = state.isMarkedAsRead,
                        onToggleMarkedAsRead = {
                            onEvent(KycContractEvent.OnToggleMarkedAsRead)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (!state.isContractLoading && !state.showError) {
                NextButtonFooter(
                    value = "Complete KYC",
                    onClick = {
                        onEvent(KycContractEvent.OnStartESignClicked)
                    },
                    loading = state.isSubmitLoading,
                    enabled = state.isMarkedAsRead,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun KycContractContent(
    contractPdfUrl: String?,
    isMarkedAsRead: Boolean,
    onToggleMarkedAsRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pdfViewer: PdfViewer = koinInject()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = Spacing.dp24)
    ) {

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            PdfPreviewCard(
                onClick = {
                    contractPdfUrl?.let {
                        pdfViewer.openPdf(it)
                    }
                }
            )
        }

        ConsentSection(
            isChecked = isMarkedAsRead,
            onToggle = onToggleMarkedAsRead,
            modifier = Modifier.padding(horizontal = Spacing.dp24)
        )
    }
}

@Composable
fun PdfPreviewCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.dp24),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Spacing.dp16))
                .border(1.dp, Border.copy(alpha = 0.3f), RoundedCornerShape(Spacing.dp16))
                .background(White)
                .padding(vertical = Spacing.dp40),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.upload_signature_icon),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(Modifier.height(Spacing.dp24))

            Text(
                text = "View KYC Document",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Primary
            )

            Spacer(Modifier.height(Spacing.dp12))

            Text(
                text = "Tap to preview your KYC verification document and ensure all information is correct.",
                style = MaterialTheme.typography.bodyMedium,
                color = GreyText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.dp32)
            )

            Spacer(Modifier.height(Spacing.dp24))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(Spacing.dp12))
                    .border(1.dp, Primary.copy(alpha = 0.2f), RoundedCornerShape(Spacing.dp12))
                    .clickable { onClick() }
                    .padding(horizontal = Spacing.dp16, vertical = Spacing.dp8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_eye),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "PREVIEW DOCUMENT",
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ConsentSection(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onToggle: () -> Unit,
    text: String = "I have read and understood the KYC document and confirm that the provided details are accurate.",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onToggle() },
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    if (isChecked) Primary else Color.Transparent
                )
                .border(
                    width = 1.dp,
                    color = if (isChecked) Primary else Border.copy(0.5f),
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            lineHeight = 20.sp
        )
    }
}

@Preview(locale = "hi")
@Composable
fun KycContractScreenPreview() {
    JantaNiveshTheme{
        KycContractScreen(
            state = KycContractUiState(),
            onEvent = {},
            onBack = {}
        )
    }
}
