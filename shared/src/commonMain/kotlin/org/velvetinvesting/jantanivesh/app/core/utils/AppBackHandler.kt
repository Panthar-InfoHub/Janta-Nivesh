package org.velvetinvesting.jantanivesh.app.core.utils

import androidx.compose.runtime.Composable

@Composable
expect fun AppBackHandler(enabled: Boolean, onBack: () -> Unit)
