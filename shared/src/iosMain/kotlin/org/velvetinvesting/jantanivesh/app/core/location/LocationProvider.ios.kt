package org.velvetinvesting.jantanivesh.app.core.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume

/**
 * `CLLocationManager` must be created and driven from the main thread, and it reports results
 * through a delegate — hence the main-dispatcher hop and the callback bridge below.
 */
class IosLocationProvider : LocationProvider {

    override fun hasLocationPermission(): Boolean =
        CLLocationManager.authorizationStatus() in AUTHORIZED_STATUSES

    override fun isLocationEnabled(): Boolean = CLLocationManager.locationServicesEnabled()

    override suspend fun getCurrentLocation(timeoutMillis: Long): LocationResult {
        if (!hasLocationPermission()) return LocationResult.PermissionDenied
        if (!isLocationEnabled()) return LocationResult.LocationDisabled

        return withTimeoutOrNull(timeoutMillis) {
            withContext(Dispatchers.Main) { requestSingleFix() }
        } ?: LocationResult.Failed("Timed out while getting your location")
    }

    private suspend fun requestSingleFix(): LocationResult =
        suspendCancellableCoroutine { continuation ->
            val manager = CLLocationManager()

            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(
                    manager: CLLocationManager,
                    didUpdateLocations: List<*>
                ) {
                    val location = didUpdateLocations.lastOrNull() as? CLLocation
                    manager.delegate = null

                    if (!continuation.isActive) return

                    if (location == null) {
                        continuation.resume(LocationResult.Failed("No location was returned"))
                    } else {
                        continuation.resume(LocationResult.Success(location.toCoordinates()))
                    }
                }

                override fun locationManager(
                    manager: CLLocationManager,
                    didFailWithError: NSError
                ) {
                    manager.delegate = null
                    if (continuation.isActive) {
                        continuation.resume(
                            LocationResult.Failed(didFailWithError.localizedDescription)
                        )
                    }
                }
            }

            manager.delegate = delegate
            // Delivers exactly one fix (or one error) and then stops on its own.
            manager.requestLocation()

            continuation.invokeOnCancellation {
                manager.delegate = null
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    private fun CLLocation.toCoordinates(): GeoCoordinates =
        coordinate.useContents { GeoCoordinates(latitude = latitude, longitude = longitude) }

    private companion object {
        val AUTHORIZED_STATUSES = setOf(
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways
        )
    }
}
