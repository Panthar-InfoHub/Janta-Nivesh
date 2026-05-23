package org.velvetinvesting.jantanivesh

import androidx.compose.ui.window.ComposeUIViewController
import org.velvetinvesting.jantanivesh.app.core.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure ={ initializeKoin() }
) { App() }