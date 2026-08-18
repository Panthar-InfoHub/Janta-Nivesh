package org.velvetinvesting.jantanivesh.app.core.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.darwin.NSObject

@Composable
actual fun rememberLocationPermissionRequester(
    onResult: (granted: Boolean) -> Unit
): LocationPermissionRequester {
    val currentOnResult by rememberUpdatedState(onResult)
    val manager = remember { CLLocationManager() }

    DisposableEffect(manager) {
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
                val status = CLLocationManager.authorizationStatus()
                // Fires once on attach with the current status; only the user's actual answer is
                // interesting, so the still-undecided state is ignored.
                if (status != kCLAuthorizationStatusNotDetermined) {
                    currentOnResult(status.isAuthorized())
                }
            }
        }

        manager.delegate = delegate
        onDispose { manager.delegate = null }
    }

    return remember(manager) {
        LocationPermissionRequester {
            val status = CLLocationManager.authorizationStatus()
            if (status == kCLAuthorizationStatusNotDetermined) {
                // The answer arrives through the delegate above.
                manager.requestWhenInUseAuthorization()
            } else {
                // Already decided — iOS shows no prompt, so report the standing answer directly.
                currentOnResult(status.isAuthorized())
            }
        }
    }
}

private fun Int.isAuthorized(): Boolean =
    this == kCLAuthorizationStatusAuthorizedWhenInUse ||
            this == kCLAuthorizationStatusAuthorizedAlways
