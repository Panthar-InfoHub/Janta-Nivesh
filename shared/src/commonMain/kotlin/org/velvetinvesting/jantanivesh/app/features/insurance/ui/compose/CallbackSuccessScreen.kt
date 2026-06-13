package org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.call
import jantanivesh.shared.generated.resources.headphone
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Gray65
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.UploadBoxBorder
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.InsuranceEvent
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.InsuranceUiState

@Composable
fun CallbackSuccessScreen(
    modifier: Modifier = Modifier,
    state: InsuranceUiState,
    onEvent: (InsuranceEvent) -> Unit
) {
    Scaffold { pv ->

        LazyColumn(
            modifier = modifier.fillMaxSize().padding(horizontal = Spacing.dp32).padding(pv),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(Spacing.dp16)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.padding(Spacing.dp12).size(72.dp).clip(CircleShape)
                            .background(color = UploadBoxBorder, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.call),
                            contentDescription = "headphone",
                            tint = Primary
                        )
                    }

                    Text(
                        "Callback Confirmed!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )


                    Text(
                        "Our insurance advisor will call you within 24 hours during your preferred time slot.",
                        style = MaterialTheme.typography.labelSmall,
                        color = Gray65,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        "Request ID:${state.requestId}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SelectedBoxBorder,
                        modifier = Modifier.padding(vertical = Spacing.dp12)
                    )


                    AppButton(
                        "Back to Home",
                        onClick = { onEvent(InsuranceEvent.BackToHomeClicked) },
                        modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp12)
                    )

                }
            }


        }


    }
}


@Preview(showBackground = true, locale = "hi")
@Composable
fun InsuranceScreenPreview3() {
    JantaNiveshTheme {
        CallbackSuccessScreen(state = InsuranceUiState(), onEvent = {})
    }
}
