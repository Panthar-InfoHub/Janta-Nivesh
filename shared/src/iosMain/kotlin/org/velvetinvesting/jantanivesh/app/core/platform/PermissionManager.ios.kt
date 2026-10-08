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
import platform.Photos.PHAuthorizationStatusLimited
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
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

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
                callback.reportOnMain(permission, PermissionStatus.GRANTED)
            }
            PermissionType.NOTIFICATION -> {
                askNotificationPermission(permission, callback)
            }
        }
    }

    private fun askGalleryPermission(status: PHAuthorizationStatus, permission: PermissionType, callback: PermissionCallback) {
        when (status) {
            PHAuthorizationStatusNotDetermined -> PHPhotoLibrary.requestAuthorization { newStatus ->
                askGalleryPermission(newStatus, permission, callback)
            }
            // Limited ("Selected Photos") still lets the user pick from the photos they allowed.
            else -> callback.reportOnMain(
                permission,
                if (status.isGalleryAccessGranted()) PermissionStatus.GRANTED else PermissionStatus.DENIED
            )
        }
    }

    @Composable
    actual override fun isPermissionGranted(permission: PermissionType): Boolean {
        var isGranted by remember(permission) { mutableStateOf(false) }

        return when (permission) {
            PermissionType.GALLERY -> PHPhotoLibrary.authorizationStatus().isGalleryAccessGranted()
            PermissionType.DOCUMENT -> true
            PermissionType.NOTIFICATION -> {

                val center = UNUserNotificationCenter.currentNotificationCenter()

                LaunchedEffect(permission) {
                    center.getNotificationSettingsWithCompletionHandler { settings ->
                        val granted = when (settings?.authorizationStatus) {
                            UNAuthorizationStatusAuthorized,
                            UNAuthorizationStatusProvisional,
                            UNAuthorizationStatusEphemeral -> true
                            else -> false
                        }
                        // The handler runs on a background queue; Compose state is set on main.
                        dispatch_async(dispatch_get_main_queue()) { isGranted = granted }
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
                callback.reportOnMain(permission, PermissionStatus.GRANTED)
            }

            UNAuthorizationStatusNotDetermined -> {
                center.requestAuthorizationWithOptions(
                    options = UNAuthorizationOptionAlert or
                            UNAuthorizationOptionSound or
                            UNAuthorizationOptionBadge
                ) { granted, _ ->
                    callback.reportOnMain(
                        permission,
                        if (granted) PermissionStatus.GRANTED else PermissionStatus.DENIED
                    )
                }
            }

            UNAuthorizationStatusDenied -> {
                callback.reportOnMain(permission, PermissionStatus.DENIED)
            }

            else -> {
                callback.reportOnMain(permission, PermissionStatus.DENIED)
            }
        }
    }
}

private fun PHAuthorizationStatus.isGalleryAccessGranted(): Boolean =
    this == PHAuthorizationStatusAuthorized || this == PHAuthorizationStatusLimited

/**
 * Photos and notification permission answers arrive on background queues; callers update UI
 * state from the callback, so it is always delivered on the main thread.
 */
private fun PermissionCallback.reportOnMain(permission: PermissionType, status: PermissionStatus) {
    dispatch_async(dispatch_get_main_queue()) { onPermissionStatus(permission, status) }
}
