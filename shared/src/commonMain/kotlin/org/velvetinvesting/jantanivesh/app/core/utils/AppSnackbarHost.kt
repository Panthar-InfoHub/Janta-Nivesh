package org.velvetinvesting.jantanivesh.app.core.utils

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AppSnackbarHost() {
    val snackbarHostState = remember { SnackbarHostState() }
    var currentSnackBar by remember { mutableStateOf<SnackBarType?>(null) }

    LaunchedEffect(Unit) {
        SnackBarController.snackBars.collect { snackBar ->
            currentSnackBar = snackBar

            snackbarHostState.showSnackbar(
                message = when (snackBar) {
                    is SnackBarType.Success -> snackBar.message
                    is SnackBarType.Error -> snackBar.message
                    is SnackBarType.Warning -> snackBar.message
                    is SnackBarType.Info -> snackBar.message
                    is SnackBarType.Neutral -> snackBar.message
                },
                withDismissAction = true
            )
        }
    }

    SnackbarHost(
        hostState = snackbarHostState
    ) { data ->
        AppSnackbar(
            snackbarData = data,
            type = currentSnackBar
        )
    }
}