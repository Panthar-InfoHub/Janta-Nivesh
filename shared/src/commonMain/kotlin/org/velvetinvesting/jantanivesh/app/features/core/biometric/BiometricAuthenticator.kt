package org.velvetinvesting.jantanivesh.app.features.core.biometric

import androidx.compose.runtime.Composable

interface BiometricAuthenticator {
    fun isBiometricAvailable(): Boolean
    fun authenticate(
        title: String,
        subtitle: String,
        description: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}

@Composable
expect fun rememberBiometricAuthenticator(): BiometricAuthenticator
