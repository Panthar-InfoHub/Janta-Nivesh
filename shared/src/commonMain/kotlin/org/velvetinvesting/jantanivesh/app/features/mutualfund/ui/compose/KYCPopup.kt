package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.kyc_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KYCPopup(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
){

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        sheetState=sheetState,
        dragHandle = null,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.size(94.dp)
                    .clip(CircleShape)
                    .background(appRed.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(Res.drawable.kyc_icon),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = appRed
                )
            }

            Text(
                text="Verify Identity (KYC)\n" +
                        "Just Few Steps",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Text(
                text="You’re a few steps away from starting your investment journey with us.",
                style = titlesStyle,
                textAlign = TextAlign.Center,
                color= titleColor
            )

            AppButton(
                modifier=Modifier.fillMaxWidth(),
                onClick = {
                    onClick()
                },
                text = "Complete your application",
            )

        }
    }

}