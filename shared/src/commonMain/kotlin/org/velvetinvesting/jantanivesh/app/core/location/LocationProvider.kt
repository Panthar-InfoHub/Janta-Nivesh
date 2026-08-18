package org.velvetinvesting.jantanivesh.app.core.location

data class GeoCoordinates(
    val latitude: Double,
    val longitude: Double
)

sealed interface LocationResult {
    data class Success(val coordinates: GeoCoordinates) : LocationResult

    /** The user has not granted (or has revoked) the foreground location permission. */
    data object PermissionDenied : LocationResult

    /** Permission is fine but location services are switched off device-wide. */
    data object LocationDisabled : LocationResult

    data class Failed(val message: String) : LocationResult
}

/**
 * Reads the device's current position.
 *
 * Requesting the permission is deliberately not part of this interface: on both platforms that
 * has to happen from the UI layer, so it lives in [rememberLocationPermissionRequester]. This
 * interface only reports whether permission is already held and performs the actual fix.
 */
interface LocationProvider {

    fun hasLocationPermission(): Boolean

    fun isLocationEnabled(): Boolean

    /**
     * Suspends until a fix arrives, the [timeoutMillis] elapses, or the attempt fails. Never
     * throws — every outcome is modelled as a [LocationResult].
     */
    suspend fun getCurrentLocation(timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS): LocationResult

    companion object {
        const val DEFAULT_TIMEOUT_MILLIS = 15_000L
    }
}
