package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.StateCode

@Preview
@Composable
fun StatePickerDialogPreview() {
    JantaNiveshTheme {
        StatePickerDialog(
            showDialog = true,
            selectedState = "MH",
            onDismiss = {},
            onStateSelected = {}
        )
    }
}

@Composable
fun StatePickerDialog(
    showDialog: Boolean,
    selectedState: String,
    onDismiss: () -> Unit,
    onStateSelected: (StateCode) -> Unit
) {
    if (!showDialog) return

    Dialog(onDismissRequest = onDismiss) {

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Text(
                    text = "Select State",
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.heightIn(max = 500.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(StateCode.entries) { state ->

                        val isSelected = selectedState == state.code

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected)
                                        Primary.copy(alpha = 0.08f)
                                    else
                                        Color.Transparent
                                )
                                .clickable {
                                    onStateSelected(state)
                                    onDismiss()
                                }
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 14.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "${state.displayName} (${state.code})",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (isSelected) Primary else Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}