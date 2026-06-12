package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun <T>AppDialogList(
    items: List<T>,
    textFormatter: (T) -> String,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss
    ){
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 28.dp, vertical = 36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {

                items(items) { item ->
                    Text(
                        text = textFormatter(item),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(item)
                                onDismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
}