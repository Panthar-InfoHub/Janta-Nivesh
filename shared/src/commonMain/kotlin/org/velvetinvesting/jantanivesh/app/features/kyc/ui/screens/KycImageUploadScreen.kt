package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.kyc_image_upload_file_size_error
import jantanivesh.shared.generated.resources.kyc_image_upload_photo_title
import jantanivesh.shared.generated.resources.kyc_image_upload_signature_title
import jantanivesh.shared.generated.resources.selected_desc
import jantanivesh.shared.generated.resources.tick_icon
import jantanivesh.shared.generated.resources.upload_photo_icon
import jantanivesh.shared.generated.resources.upload_signature_icon
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.UploadBoxBackground
import org.velvetinvesting.jantanivesh.app.core.theme.UploadBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.utils.ImageUploader
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.dashedBorder
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCImageUploaderEvent
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCImageUploaderUiState

@Composable
fun KycImageUploadScreen(
    state: KYCImageUploaderUiState,
    onEvent: (KYCImageUploaderEvent) -> Unit,
    onBack: () -> Unit
) {


    val scope= rememberCoroutineScope()
    val fileLimitError = stringResource(Res.string.kyc_image_upload_file_size_error)
    ImageUploader(
        showGallery = state.showSignatureSelector,
        onDismiss = {
            onEvent(KYCImageUploaderEvent.hideSignatureSelector)
        },
        onSelected = { photoResult ->
            photoResult.fileSize?.let { size ->
                if (size > 5_242_880L) {
                    scope.launch {
                        SnackBarController.showError(fileLimitError)
                    }
                    onEvent(KYCImageUploaderEvent.hideSignatureSelector)
                    return@ImageUploader
                }
            }

            onEvent(
                KYCImageUploaderEvent.OnSignatureSelected(
                    photoResult.loadBytes(),
                    photoResult.mimeType ?:"image/jpeg"
                )
            )
        }
    )

    ImageUploader(
        showGallery = state.showPhotoSelector,
        onDismiss = {
            onEvent(KYCImageUploaderEvent.hidePhotoSelector)
        },
        onSelected = { photoResult ->
            photoResult.fileSize?.let { size ->
                if (size > 5_242_880L) {
                    scope.launch {
                        SnackBarController.showError(fileLimitError)
                    }
                    onEvent(KYCImageUploaderEvent.hidePhotoSelector)
                    return@ImageUploader
                }
            }

            onEvent(
                KYCImageUploaderEvent.OnUserPhotoSelected(
                    photoResult.loadBytes(),
                    photoResult.mimeType ?:"image/jpeg"

                )
            )
        }
    )

    Scaffold(
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
        },
        containerColor = White
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
        ) {
            BackHeader(
                title = "Verification Details",
                onBack = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp24)
            )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
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
                    title = "Upload Signature/" + stringResource(Res.string.kyc_image_upload_signature_title),
                    instructionText = "Click to Upload Signature",
                    icon = Res.drawable.upload_signature_icon,
                    topLineColor = Primary.copy(alpha = 0.5f),
                    isSelected = state.signatureBytes != null,
                    onClick = {
                        onEvent(KYCImageUploaderEvent.showSignatureSelector)
                    }
                )
            }

            item {
                UploadDocumentCard(
                    title = "Upload Photo/" + stringResource(Res.string.kyc_image_upload_photo_title),
                    instructionText = "Click to Upload Photo",
                    icon = Res.drawable.upload_photo_icon,
                    topLineColor = SelectedBoxBorder.copy(alpha = 0.4f),
                    isSelected = state.userPhotoBytes != null,
                    onClick = {
                        onEvent(KYCImageUploaderEvent.showPhotoSelector)
                    }
                )
            }
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
                                    contentDescription = stringResource(Res.string.selected_desc),
                                    tint = Primary,
                                    modifier = Modifier.size(Spacing.dp24)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = stringResource(Res.string.selected_desc),
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

@Preview(locale = "hi")
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
