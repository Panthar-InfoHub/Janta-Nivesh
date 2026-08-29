package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.nominee_opt_out_terms_body
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader

/**
 * The full opt-out declaration, reached from the link on the nominee screen.
 *
 * Both language versions are shown one after the other rather than only the active one: this is
 * the regulated text behind a decision the user cannot easily undo, so the English original stays
 * on the page next to the translation.
 */
@Composable
fun NomineeOptOutTermsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = Spacing.dp20),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        BackHeader(
            title = "Nominee Opt Out Terms",
            onBack = onBackClick
        )

        LazyColumn (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
            contentPadding = PaddingValues(bottom = Spacing.dp20)
        ) {
            item{
                Text(
                    text = ENGLISH_TERMS,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray444,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item{
                Text(
                    text = stringResource(Res.string.nominee_opt_out_terms_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray444,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Held here rather than read from `values/strings.xml`, so that it stays in English even when
 * English is the user's selected language and the resource below resolves to English too.
 */
private const val ENGLISH_TERMS =
    "I hereby confirm that I do not wish to appoint any nominee(s) to my mutual fund folio " +
            "at this point of time.\n" +
            "I understand that –\n" +
            "(i) the nomination helps to quickly identify the person for transfer of securities " +
            "and helps in faster and smoother transmission of my securities to my legal heir " +
            "after my demise.\n" +
            "(ii) in the absence of a nomination, my legal heir(s) may require the submission of " +
            "certain additional legal or court-issued documents which may delay the process of " +
            "transmission of securities to my legal heir(s).\n" +
            "(iii) if no claim is made on the account / folio for a prolonged period after my " +
            "demise, the holdings may be treated as unclaimed assets, and they may be " +
            "transferred to Investor Education and Protection Fund Authority (IEPF) in " +
            "accordance with the applicable regulatory framework.\n" +
            "I confirm that I have understood the above implications and that my decision to " +
            "opt out of nomination is voluntary."

@Preview(locale = "hi", heightDp = 1400, showBackground = true)
@Composable
private fun NomineeOptOutTermsScreenPreview() {
    JantaNiveshTheme {
        NomineeOptOutTermsScreen(onBackClick = {})
    }
}
