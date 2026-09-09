package org.velvetinvesting.jantanivesh.app.core.platform

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

actual class PermissionsManager actual constructor(private val callback: PermissionCallback) :
    PermissionHandler {
    private var launcher: ManagedActivityResultLauncher<String, Boolean>? = null

    @Composable
    actual override fun askPermission(permission: PermissionType) {
        val context = LocalContext.current

        val permissionString = when (permission) {
            PermissionType.GALLERY -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
            }

            PermissionType.DOCUMENT -> {
                // ⚠️ Not needed actually, but fallback
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

            PermissionType.NOTIFICATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.POST_NOTIFICATIONS
                } else {
                    null
                }
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            val status = if(isGranted) PermissionStatus.GRANTED
            else  PermissionStatus.DENIED

            callback.onPermissionStatus(permission, status)
        }

        if (permissionString == null) {
            callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            return
        }

        LaunchedEffect(permission) {
            launcher.launch(permissionString)
        }
    }

    @Composable
    actual override fun isPermissionGranted(permission: PermissionType): Boolean {



        val context = LocalContext.current

        val permissionString = when (permission) {
            PermissionType.GALLERY -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
            }

            PermissionType.DOCUMENT -> {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

            PermissionType.NOTIFICATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.POST_NOTIFICATIONS
                } else {
                    null
                }
            }
        }
        if (permissionString == null) return true
        return ContextCompat.checkSelfPermission(
            context,
            permissionString
        ) == PackageManager.PERMISSION_GRANTED
    }

    @Composable
    actual override fun launchSettings() {
        val context = LocalContext.current

        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )

        context.startActivity(intent)
    }
}

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    return remember { PermissionsManager(callback) }
}