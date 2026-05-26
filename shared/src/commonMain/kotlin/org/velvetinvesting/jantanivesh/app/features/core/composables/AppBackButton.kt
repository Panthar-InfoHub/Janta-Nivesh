package org.velvetinvesting.jantanivesh.app.features.core.composables

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.arrowback_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberEvent

@Composable
fun AppBackButton(onClick: () -> Unit, modifier: Modifier = Modifier){
    IconButton(
        onClick = onClick,
        modifier = modifier.offset(x = (-Spacing.dp12)),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent,
        )
    ) {
        Icon(
            painter = painterResource(Res.drawable.arrowback_icon),
            contentDescription = "Go Back"
        )
    }
}