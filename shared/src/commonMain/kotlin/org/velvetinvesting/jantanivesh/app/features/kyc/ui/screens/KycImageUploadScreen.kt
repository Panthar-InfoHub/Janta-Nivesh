package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.tick_icon
import jantanivesh.shared.generated.resources.upload_photo_icon
import jantanivesh.shared.generated.resources.upload_signature_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.UploadBoxBackground
import org.velvetinvesting.jantanivesh.app.core.theme.UploadBoxBorder
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCImageUploaderEvent
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCImageUploaderUiState

@Preview
@Composable
fun KycImageUploadScreenPreview() {
    JantaNiveshTheme {
        KycImageUploadScreen(
            state = KYCImageUploaderUiState(),
            onEvent = {},
            onBack = {}
        )
    }
}

@Composable
fun KycImageUploadScreen(
    state: KYCImageUploaderUiState,
    onEvent: (KYCImageUploaderEvent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BackHeader(
                title = "Verification Details",
                onBack = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp24)
            )
        },
        bottomBar = {
            AppButton(
                text = "Save & Continue →",
                onClick = { onEvent(KYCImageUploaderEvent.OnUploadClicked) },
                loading = state.isLoading,
                enabled = state.userPhotoBytes != null && state.signatureBytes != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.dp24)
            )
        }
    ) { pv ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .padding(horizontal = Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
                    modifier = Modifier.padding(top = Spacing.dp12)
                ) {
                    Text(
                        text = "Upload Documents",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Please provide clear images of your signature and photo for identity verification.",
                        style = MaterialTheme.typography.labelSmall,
                        color = GreyText
                    )
                }
            }

            item {
                UploadDocumentCard(
                    title = "Upload Signature/ (हस्ताक्षर अपलोड करें)",
                    instructionText = "Click to Upload Signature",
                    icon = Res.drawable.upload_signature_icon,
                    topLineColor = Primary.copy(alpha = 0.5f),
                    isSelected = state.signatureBytes != null,
                    onClick = { /* TODO: Trigger Signature Upload intent/event */ }
                )
            }

            item {
                UploadDocumentCard(
                    title = "Upload Photo/ (फोटो अपलोड करें)",
                    instructionText = "Click to Upload Photo",
                    icon = Res.drawable.upload_photo_icon,
                    topLineColor = SelectedBoxBorder.copy(alpha = 0.4f),
                    isSelected = state.userPhotoBytes != null,
                    onClick = { /* TODO: Trigger Photo Upload intent/event */ }
                )
            }
        }
    }
}

@Composable
fun UploadDocumentCard(
    title: String,
    icon: DrawableResource,
    instructionText: String,
    topLineColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(Spacing.dp12))
    )
    {
        Column {
            // Top Accent Line
            HorizontalDivider(thickness = Spacing.dp4, color = topLineColor)

            Column(
                modifier = Modifier.padding(all = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = Black
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Spacing.dp8))
                        .background(
                            if (isSelected) Primary.copy(alpha = 0.05f)
                            else UploadBoxBackground
                        )
                        .dashedBorder(
                            color = if (isSelected) Primary else UploadBoxBorder,
                            strokeWidth = Spacing.dp2,
                            cornerRadius = Spacing.dp8,
                            dashLength = Spacing.dp6,
                            gapLength = Spacing.dp4,
                        )
                        .clickable { onClick() }
                        .padding(vertical = Spacing.dp32),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(Spacing.dp48)
                                .background(
                                    if (isSelected) Primary.copy(alpha = 0.1f) else UploadBoxBorder,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    painter = painterResource(Res.drawable.tick_icon),
                                    contentDescription = "Selected",
                                    tint = Primary,
                                    modifier = Modifier.size(Spacing.dp24)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = "Selected",
                                    tint = Primary,
                                    modifier = Modifier.size(Spacing.dp24)
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
                        ) {
                            Text(
                                text = if (isSelected) "Document Selected" else instructionText,
                                style = MaterialTheme.typography.labelMedium.copy(fontSize = 16.sp)
                            )
                            if (!isSelected) {
                                Text(
                                    text = "JPEG, PNG up to 5 MB",
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp),
                                    color = GreyText
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}
fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp = 1.dp,
    cornerRadius: Dp = 8.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 4.dp
) = this.drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx()),
                phase = 0f
            )
        ),
        cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
    )
}
