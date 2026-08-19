package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.janta_nivesh_logo_desc
import jantanivesh.shared.generated.resources.jantanivesh_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing

@Preview(showBackground = true)
@Composable
fun JantaNiveshAndVelvetLogo(modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.jantanivesh_logo),
            contentDescription = stringResource(Res.string.janta_nivesh_logo_desc),
            modifier = Modifier.height(Spacing.dp42)
        )
        PoweredByVelvet()
    }
}