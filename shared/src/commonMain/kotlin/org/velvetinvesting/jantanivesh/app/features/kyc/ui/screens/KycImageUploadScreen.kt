package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.tick_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCImageUploaderEvent
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCImageUploaderUiState

@Preview
@Composable
fun KycImageUploadScreenPreview() {
    KycImageUploadScreen(
        state = KYCImageUploaderUiState(),
        onEvent = {},
        onBack = {}
    )
}

@Composable
fun KycImageUploadScreen(
    state: KYCImageUploaderUiState,
    onEvent: (KYCImageUploaderEvent) -> Unit,
    onBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBarWithBackButtonAndStepCount(
                stepCount = 3,
                totalSteps = 5,
                onBack = onBack,
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            )
        },
        bottomBar = {
            AppButton(
                text = "Upload and Continue",
                onClick = { onEvent(KYCImageUploaderEvent.OnUploadClicked) },
                loading = state.isLoading,
                enabled = state.userPhotoBytes != null && state.signatureBytes != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.dp24)
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .padding(horizontal = Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
            Text(
                text = "Upload Documents",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = Spacing.dp12)
            )

            Text(
                text = "Please upload a clear photo of yourself and your signature.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            UploadCard(
                label = "Your Photo",
                isSelected = state.userPhotoBytes != null,
                onClick = {
                }
            )

            UploadCard(
                label = "Your Signature",
                isSelected = state.signatureBytes != null,
                onClick = {
                }
            )
        }
    }
}

@Composable
fun UploadCard(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(if (isSelected) Primary.copy(alpha = 0.05f) else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (isSelected) Primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(Spacing.dp12)
            )
            .clickable { onClick() }
            .padding(Spacing.dp16),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            if (isSelected) {
                Icon(
                    painter = painterResource(Res.drawable.tick_icon),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp32)
                )
            } else {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = if (isSelected) "$label Selected" else "Upload $label",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
