package org.velvetinvesting.jantanivesh.app.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusEphemeral
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter

@Composable
actual fun createPermissionsManager(callback: PermissionCallback): PermissionsManager {
    return PermissionsManager(callback)
}

actual class PermissionsManager actual constructor(private val callback: PermissionCallback) : PermissionHandler {
    @Composable
    actual override fun askPermission(permission: PermissionType) {
        when (permission) {
            PermissionType.GALLERY -> {
                val status = remember { PHPhotoLibrary.authorizationStatus() }
                askGalleryPermission(status, permission, callback)
            }
            PermissionType.DOCUMENT -> {
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }
            PermissionType.NOTIFICATION -> {
                askNotificationPermission(permission, callback)
            }
        }
    }

    private fun askGalleryPermission(status: PHAuthorizationStatus, permission: PermissionType, callback: PermissionCallback) {
        when (status) {
            PHAuthorizationStatusAuthorized -> callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            PHAuthorizationStatusNotDetermined -> PHPhotoLibrary.requestAuthorization { newStatus ->
                askGalleryPermission(newStatus, permission, callback)
            }
            PHAuthorizationStatusDenied -> callback.onPermissionStatus(permission, PermissionStatus.DENIED)
            else -> error("Unknown gallery status $status")
        }
    }

    @Composable
    actual override fun isPermissionGranted(permission: PermissionType): Boolean {
        var isGranted by remember(permission) { mutableStateOf(false) }

        return when (permission) {
            PermissionType.GALLERY -> PHPhotoLibrary.authorizationStatus() == PHAuthorizationStatusAuthorized
            PermissionType.DOCUMENT -> true
            PermissionType.NOTIFICATION -> {

                val center = UNUserNotificationCenter.currentNotificationCenter()

                LaunchedEffect(permission) {
                    center.getNotificationSettingsWithCompletionHandler { settings ->
                        isGranted = when (settings?.authorizationStatus) {
                            UNAuthorizationStatusAuthorized,
                            UNAuthorizationStatusProvisional,
                            UNAuthorizationStatusEphemeral -> true
                            else -> false
                        }
                    }
                }
                isGranted
            }
        }
    }

    @Composable
    actual override fun launchSettings() {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        UIApplication.sharedApplication.openURL(url!!)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun askNotificationPermission(
    permission: PermissionType,
    callback: PermissionCallback
) {
    val center = UNUserNotificationCenter.currentNotificationCenter()

    center.getNotificationSettingsWithCompletionHandler { settings ->
        when (settings?.authorizationStatus) {

            UNAuthorizationStatusAuthorized,
            UNAuthorizationStatusProvisional,
            UNAuthorizationStatusEphemeral -> {
                callback.onPermissionStatus(permission, PermissionStatus.GRANTED)
            }

            UNAuthorizationStatusNotDetermined -> {
                center.requestAuthorizationWithOptions(
                    options = UNAuthorizationOptionAlert or
                            UNAuthorizationOptionSound or
                            UNAuthorizationOptionBadge
                ) { granted, _ ->
                    callback.onPermissionStatus(
                        permission,
                        if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
                    )
                }
            }

            UNAuthorizationStatusDenied -> {
                callback.onPermissionStatus(permission, PermissionStatus.DENIED)
            }

            else -> {
                callback.onPermissionStatus(permission, PermissionStatus.DENIED)
            }
        }
    }
}

