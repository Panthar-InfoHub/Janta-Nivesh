package org.velvetinvesting.jantanivesh.app.core.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

/**
 * Backed by the platform [LocationManager] rather than Play Services, so the app keeps working on
 * devices without Google services and needs no extra dependency.
 */
class AndroidLocationProvider(
    private val context: Context
) : LocationProvider {

    private val locationManager: LocationManager?
        get() = ContextCompat.getSystemService(context, LocationManager::class.java)

    override fun hasLocationPermission(): Boolean {
        return PERMISSIONS.any { permission ->
            ContextCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED
        }
    }

    override fun isLocationEnabled(): Boolean {
        val manager = locationManager ?: return false
        return LocationManagerCompat.isLocationEnabled(manager)
    }

    override suspend fun getCurrentLocation(timeoutMillis: Long): LocationResult {
        if (!hasLocationPermission()) return LocationResult.PermissionDenied

        val manager = locationManager ?: return LocationResult.Failed("Location is unavailable")
        if (!LocationManagerCompat.isLocationEnabled(manager)) return LocationResult.LocationDisabled

        // A recent cached fix is good enough for a KYC stamp and avoids making the user wait for
        // the radio to warm up; anything older falls through to a live request.
        manager.freshestCachedLocation()?.let {
            return LocationResult.Success(it.toCoordinates())
        }

        val provider = manager.firstEnabledProvider()
            ?: return LocationResult.Failed("No location provider is available")

        val location = withTimeoutOrNull(timeoutMillis) {
            manager.awaitSingleUpdate(provider)
        } ?: return LocationResult.Failed("Timed out while getting your location")

        return LocationResult.Success(location.toCoordinates())
    }

    private fun LocationManager.freshestCachedLocation(): Location? {
        val now = System.currentTimeMillis()
        return PROVIDERS
            .mapNotNull { provider ->
                runCatching { getLastKnownLocation(provider) }.getOrNull()
            }
            .filter { now - it.time <= MAX_CACHE_AGE_MILLIS }
            .maxByOrNull { it.time }
    }

    private fun LocationManager.firstEnabledProvider(): String? =
        PROVIDERS.firstOrNull { provider ->
            runCatching { isProviderEnabled(provider) }.getOrDefault(false)
        }

    /**
     * Registers for updates and resumes on the first fix. Updates are requested on the main
     * looper because [LocationManager] delivers to the caller's looper, and a coroutine
     * dispatcher thread has none.
     */
    private suspend fun LocationManager.awaitSingleUpdate(provider: String): Location? =
        withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { continuation ->
                val listener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        removeUpdates(this)
                        if (continuation.isActive) continuation.resume(location)
                    }

                    // Present for source compatibility with API < 30, where they are abstract.
                    override fun onProviderEnabled(provider: String) = Unit
                    override fun onProviderDisabled(provider: String) = Unit
                }

                try {
                    requestLocationUpdates(provider, 0L, 0f, listener, Looper.getMainLooper())
                } catch (e: SecurityException) {
                    if (continuation.isActive) continuation.resume(null)
                    return@suspendCancellableCoroutine
                }

                continuation.invokeOnCancellation {
                    runCatching { removeUpdates(listener) }
                }
            }
        }

    private fun Location.toCoordinates() = GeoCoordinates(latitude, longitude)

    private companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val PROVIDERS = listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER
        )

        const val MAX_CACHE_AGE_MILLIS = 2 * 60 * 1000L
    }
}
