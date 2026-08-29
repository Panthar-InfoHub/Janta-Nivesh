package org.velvetinvesting.jantanivesh.app.features.core.biometric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

class IosBiometricAuthenticator : BiometricAuthenticator {
    @OptIn(ExperimentalForeignApi::class)
    override fun isBiometricAvailable(): Boolean {
        val context = LAContext()
        return context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, null)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun authenticate(
        title: String,
        subtitle: String,
        description: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val context = LAContext()
        context.evaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            localizedReason = description
        ) { success, error ->
            dispatch_async(dispatch_get_main_queue()) {
                if (success) {
                    onSuccess()
                } else {
                    onError(error?.localizedDescription ?: "Authentication failed")
                }
            }
        }
    }
}

@Composable
actual fun rememberBiometricAuthenticator(): BiometricAuthenticator {
    return remember { IosBiometricAuthenticator() }
}
