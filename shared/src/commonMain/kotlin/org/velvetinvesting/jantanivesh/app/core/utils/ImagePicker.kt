package org.velvetinvesting.jantanivesh.app.core.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.ic_gallery
import jantanivesh.shared.generated.resources.upload_photo_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing

@Composable
fun ImageUploader(
    showGallery:Boolean,
    onDismiss:()->Unit,
    onSelected:(PhotoResult)->Unit
) {

//    if (showGallery) {
//        GalleryPickerLauncher(
//            onPhotosSelected = {
//                onSelected(it[0])
//            },
//            onError = {
//                Log("Picker", it.toString())
//            },
//            onDismiss = {
//                onDismiss()
//            },
//            allowMultiple = false,
//            mimeTypes = listOf(MimeType.IMAGE_PNG, MimeType.IMAGE_JPEG),
//            selectionLimit = 1,
//            cameraCaptureConfig = null,
//            enableCrop = false,
//            fileFilterDescription = "",
//            includeExif = false,
//            mimeTypeMismatchMessage = "Selected image must be PNG or JPEG"
//        )
//    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    if (!visible) return

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {

            Text(
                "Select Image",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom=Spacing.dp16, start = 12.dp)
            )

            BottomSheetItem(
                icon = Res.drawable.upload_photo_icon,
                text = "Camera",
                onClick = onCameraClick
            )

            BottomSheetItem(
                icon = Res.drawable.ic_gallery,
                text = "Gallery",
                onClick = onGalleryClick,
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun BottomSheetItem(
    text: String,
    onClick: () -> Unit,
    icon: DrawableResource
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = Spacing.dp4, horizontal = Spacing.dp12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Primary
        )
        Text(
            text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}