package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField

/**
 * Read-only text field that opens a date picker when tapped.
 *
 * A read-only [TitledAppTextField] swallows taps rather than forwarding them to an enclosing
 * `clickable`, so the press is observed through the field's own interaction source instead.
 */
@Composable
fun TitledDateField(
    title: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "YYYY-MM-DD",
    mandatory: Boolean = true
) {
    TitledAppTextField(
        title = title,
        value = value,
        onValueChange = { },
        placeholder = placeholder,
        mandatory = mandatory,
        readOnly = true,
        interactionSource = rememberDatePickerInteractionSource(onClick),
        modifier = modifier
    )
}

@Composable
fun rememberDatePickerInteractionSource(onClick: () -> Unit): MutableInteractionSource {
    val interactionSource = remember { MutableInteractionSource() }
    val currentOnClick by rememberUpdatedState(onClick)

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) currentOnClick()
        }
    }

    return interactionSource
}
