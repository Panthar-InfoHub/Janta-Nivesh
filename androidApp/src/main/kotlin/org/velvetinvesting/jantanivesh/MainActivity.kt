package org.velvetinvesting.jantanivesh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.Transparent.hashCode(),
                darkScrim = Color.Transparent.hashCode()
            )
        )
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}
