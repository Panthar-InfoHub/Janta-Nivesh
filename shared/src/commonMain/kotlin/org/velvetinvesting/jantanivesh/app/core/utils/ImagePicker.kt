package org.velvetinvesting.jantanivesh.app.core.utils

import androidx.compose.runtime.Composable
import io.github.ismoy.imagepickerkmp.domain.models.MimeType
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import org.velvetinvesting.jantanivesh.app.core.platform.Log

@Composable
fun ImageUploader(
    showGallery:Boolean,
    onDismiss:()->Unit,
    onSelected:(PhotoResult)->Unit
) {

    if (showGallery) {
        GalleryPickerLauncher(
            onPhotosSelected = {
                onSelected(it[0])
            },
            onError = {
                Log("Picker", it.toString())
            },
            onDismiss = {
                onDismiss()
            },
            allowMultiple = false,
            mimeTypes = listOf(MimeType.IMAGE_PNG, MimeType.IMAGE_JPEG),
            selectionLimit = 1,
            cameraCaptureConfig = null,
            enableCrop = false,
            fileFilterDescription = "",
            includeExif = false,
            mimeTypeMismatchMessage = "Selected image must be PNG or JPEG"
        )
    }
}