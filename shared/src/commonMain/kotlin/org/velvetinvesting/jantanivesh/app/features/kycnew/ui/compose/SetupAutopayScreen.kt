package org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.important_note_desc
import jantanivesh.shared.generated.resources.info_filled_icon
import jantanivesh.shared.generated.resources.set_up_upi_autopay
import jantanivesh.shared.generated.resources.upi_autopay_subtitle
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InfoNoteCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.SetupAutopayEvent
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.SetupAutopayUiState

@Composable
fun SetupAutopayScreen(
    state: SetupAutopayUiState,
    handleEvent: (SetupAutopayEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = White)
            .padding(horizontal=Spacing.dp20)
            .clearFocusOnTap()
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
            contentPadding = PaddingValues(top = Spacing.dp24)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Set up UPI Autopay/ " + stringResource(Res.string.set_up_upi_autopay),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = "One approval, and your SIP runs on its own every month. Cancel any time./ " + stringResource(
                            Res.string.upi_autopay_subtitle
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray444
                    )
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
                    modifier = Modifier
                        .genericDropShadow()
                        .background(White, RoundedCornerShape(Spacing.dp24))
                        .padding(horizontal = Spacing.dp24, vertical = Spacing.dp32)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Type", style = MaterialTheme.typography.bodyLarge, color = Gray444)
                        Text(
                            text = state.autopayType,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Black
                        )
                    }
                    HorizontalDivider(
                        thickness = Spacing.dp1,
                        color = Secondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limit", style = MaterialTheme.typography.bodyLarge, color = Gray444)
                        Text(
                            text = state.autopayLimit,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Black
                        )
                    }
                    HorizontalDivider(
                        thickness = Spacing.dp1,
                        color = Secondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Bank", style = MaterialTheme.typography.bodyLarge, color = Gray444)
                        Text(
                            text = state.bankDetails,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Black
                        )
                    }
                }
            }
            item {
                InfoNoteCard(
                    icon = Res.drawable.info_filled_icon,
                    title = "Important Note",
                    subtitle = "Your bank statement and UPI app will show Cybrilla — the official partner processing these mutual-fund transactions for Janta Nivesh./" + stringResource(
                        Res.string.important_note_desc
                    )
                )
            }
            item{
                AppButton(
                    text = "Set Autopay",
                    onClick = { handleEvent(SetupAutopayEvent.OnSetAutopayClick) },
                    loading = state.isLoading,
                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp24).genericDropShadow()
                )
            }
        }
    }
}

@Preview(locale = "hi")
@Composable
private fun SetupAutopayScreenPreview() {
    JantaNiveshTheme {
        SetupAutopayScreen(
            state = SetupAutopayUiState(),
            handleEvent = {}
        )
    }
}