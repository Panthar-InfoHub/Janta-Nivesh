package org.velvetinvesting.jantanivesh.app.features.core.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.utils.DateTimeUtils
import org.velvetinvesting.jantanivesh.app.core.utils.formatDateParts
import org.velvetinvesting.jantanivesh.app.core.utils.formatMillisDateParts


@Composable
fun AppDatePicker(
    show: Boolean,
    selectedDate: Long?,
    onDismiss: () -> Unit,
    onDateSelected: (Long?) -> Unit
) {
    if (!show) return

    val state = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= DateTimeUtils.getCurrentEpochMillis()
            }
        }
    )

    val (year, formattedDate) = remember(selectedDate) {
        if (selectedDate == null) {
            val today = DateTimeUtils.today()
            formatDateParts(today)
        } else {
            formatMillisDateParts(selectedDate) ?: run {
                val today =  DateTimeUtils.today()
                formatDateParts(today)
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancel",
                    color = Primary,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFontFamily,
                    fontSize = 16.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(state.selectedDateMillis)
                onDismiss()
            }) {
                Text(
                    "OK",
                    color = Primary,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFontFamily,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        },
        modifier = Modifier.scale(0.9f)
    ) {
        DatePicker(
            state = state,
            title = null,
            headline = {
                Box(Modifier.fillMaxWidth().background(Primary)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 20.dp)
                    ) {
                        Text(
                            text = year,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = InterFontFamily,
                            color = Color.White
                        )
                        Text(
                            text = formattedDate,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = InterFontFamily,
                            color = Color.White
                        )
                    }
                }
            },
            showModeToggle = false
        )
    }
}