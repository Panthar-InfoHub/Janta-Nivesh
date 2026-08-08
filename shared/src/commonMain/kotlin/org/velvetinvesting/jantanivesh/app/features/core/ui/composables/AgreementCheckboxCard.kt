package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White


@Composable
fun AgreementCheckBoxCard(
    text: String,
    isConsentChecked: Boolean,
    onConsentChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .border(Spacing.dp1, FilterChipUnselected, RoundedCornerShape(Spacing.dp16))
            .background(GoalIconBg)
            .padding(Spacing.dp20),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = isConsentChecked,
            onCheckedChange = onConsentChange,
            colors = CheckboxDefaults.colors(checkedColor = Primary, checkmarkColor = White),
            modifier = Modifier
                .size(Spacing.dp24)
                .padding(top = Spacing.dp2)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = Gray444
        )
    }
}

@Preview
@Composable
private fun CheckedAgreementCheckBoxCardPreview(){
    JantaNiveshTheme {
        AgreementCheckBoxCard(text = "This is a preview of this card", true, {})
    }
}
@Preview
@Composable
private fun UncheckedAgreementCheckBoxCardPreview(){
    JantaNiveshTheme {
        AgreementCheckBoxCard(text = "This is a preview of this card", false, {})
    }
}