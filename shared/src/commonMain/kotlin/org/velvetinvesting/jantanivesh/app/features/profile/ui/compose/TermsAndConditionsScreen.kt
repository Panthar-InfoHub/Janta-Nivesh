package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import kotlin.time.Clock

object TermsAndConditionsTextStyle {

    @Composable
    fun screenTitle() = MaterialTheme.typography.headlineMedium

    @Composable
    fun sectionTitle() = MaterialTheme.typography.labelLarge

    @Composable
    fun bodyTitle() =

        MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)

    @Composable
    fun body() = MaterialTheme.typography.bodyMedium
}

@Suppress("SuspiciousIndentation")
@Composable

fun TermsAndConditionsScreen(
    onBack: () -> Unit = {}, modifier: Modifier = Modifier

) {

    var showDevDialog by remember {
        mutableStateOf(false)
    }

    var tapCount by remember { mutableIntStateOf(0) }
    var firstTapTime by remember { mutableLongStateOf(0L) }

    val effectiveDateLabel = stringResource(Res.string.tac_effective_date_label)
    val effectiveDateValue = stringResource(Res.string.tac_effective_date_value)
    val lastUpdatedLabel = stringResource(Res.string.tac_last_updated_label)
    val lastUpdatedValue = stringResource(Res.string.tac_last_updated_value)

        Column(
            modifier = Modifier.fillMaxSize().background(White).padding(
                PaddingValues(
                    vertical = Spacing.dp24
                )
            )
        ) {
            BackHeader(
                title = stringResource(Res.string.terms_and_conditions),
                showBack = true,
                onBack = onBack,
                modifier = Modifier.padding(horizontal = Spacing.dp16)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.dp20), modifier = Modifier.padding(horizontal = Spacing.dp24).weight(1f)){
                item{
                    Text(
                        text = stringResource(Res.string.terms_and_conditions),
                        style = TermsAndConditionsTextStyle.screenTitle()
                    )
                }
                item{
                    Text(
                        text = stringResource(Res.string.tac_intro),
                        style = TermsAndConditionsTextStyle.body()
                    )
                }
                item{
                    Column {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                    )
                                ) {
                                    append(effectiveDateLabel)
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                ) {
                                    append(effectiveDateValue)
                                }
                            },
                            style = TermsAndConditionsTextStyle.body()
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                ) {
                                    append(lastUpdatedLabel)
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                ) {
                                    append(lastUpdatedValue)
                                }
                            },
                            style = TermsAndConditionsTextStyle.body()
                        )
                    }
                }
                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec1_title), body = listOf(
                            stringResource(Res.string.tac_sec1_body1)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec2_title), body = listOf(
                            stringResource(Res.string.tac_sec2_body1)
                        ), bullets = listOf(
                            stringResource(Res.string.tac_sec2_bullet1),
                            stringResource(Res.string.tac_sec2_bullet2),
                            stringResource(Res.string.tac_sec2_bullet3),
                            stringResource(Res.string.tac_sec2_bullet4)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec3_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec3_body1)
                        ),
                        remark = stringResource(Res.string.tac_sec3_remark),
                        bullets = listOf(
                            stringResource(Res.string.tac_sec3_bullet1),
                            stringResource(Res.string.tac_sec3_bullet2),
                            stringResource(Res.string.tac_sec3_bullet3)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec4_title),
                        body = listOf(stringResource(Res.string.tac_sec4_body1)),
                        bullets = listOf(
                            stringResource(Res.string.tac_sec4_bullet1),
                            stringResource(Res.string.tac_sec4_bullet2),
                            stringResource(Res.string.tac_sec4_bullet3),
                            stringResource(Res.string.tac_sec4_bullet4),
                            stringResource(Res.string.tac_sec4_bullet5),
                            stringResource(Res.string.tac_sec4_bullet6)
                        ),
                        remark = stringResource(Res.string.tac_sec4_remark)
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec5_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec5_body1)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec6_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec6_body1)
                        ),
                        bullets = listOf(
                            stringResource(Res.string.tac_sec6_bullet1),
                            stringResource(Res.string.tac_sec6_bullet2),
                            stringResource(Res.string.tac_sec6_bullet3),
                            stringResource(Res.string.tac_sec6_bullet4),
                            stringResource(Res.string.tac_sec6_bullet5)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec7_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec7_body1)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec8_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec8_body1)
                        ),
                        bullets = listOf(
                            stringResource(Res.string.tac_sec8_bullet1),
                            stringResource(Res.string.tac_sec8_bullet2),
                            stringResource(Res.string.tac_sec8_bullet3),
                            stringResource(Res.string.tac_sec8_bullet4)
                        ),
                        remark = stringResource(Res.string.tac_sec8_remark)
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec9_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec9_body1)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec10_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec10_body1)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec11_title),
                        body = listOf(stringResource(Res.string.tac_sec11_body1)),
                        bullets = listOf(
                            stringResource(Res.string.tac_sec11_bullet1),
                            stringResource(Res.string.tac_sec11_bullet2),
                            stringResource(Res.string.tac_sec11_bullet3),
                            stringResource(Res.string.tac_sec11_bullet4),
                            stringResource(Res.string.tac_sec11_bullet5),
                            stringResource(Res.string.tac_sec11_bullet6),
                            stringResource(Res.string.tac_sec11_bullet7)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec12_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec12_body1)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec13_title),
                        body = listOf(stringResource(Res.string.tac_sec13_body1)),
                        bullets = listOf(
                            stringResource(Res.string.tac_sec13_bullet1),
                            stringResource(Res.string.tac_sec13_bullet2),
                            stringResource(Res.string.tac_sec13_bullet3),
                            stringResource(Res.string.tac_sec13_bullet4)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec14_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec14_body1)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec15_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec15_body1)
                        )
                    )
                }

                item{
                    TermsSection(
                        title = stringResource(Res.string.tac_sec16_title),
                        body = listOf(
                            stringResource(Res.string.tac_sec16_body1)
                        )
                    )
                }

                item{
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
                    ) {

                        Text(
                            text = stringResource(Res.string.tac_sec17_title),
                            style = TermsAndConditionsTextStyle.sectionTitle()
                        )

                        Text(
                            text = stringResource(Res.string.tac_sec17_company),
                            style = TermsAndConditionsTextStyle.body()
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
                        ) {

                            Text(
                                text = stringResource(Res.string.tac_email_label),
                                style = TermsAndConditionsTextStyle.bodyTitle(),
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    val currentTime = Clock.System.now().toEpochMilliseconds()
                                    if (currentTime - firstTapTime > 5_000) {
                                        tapCount = 0
                                        firstTapTime = currentTime
                                    }
                                    if (tapCount == 0) {
                                        firstTapTime = currentTime
                                    }
                                    tapCount++
                                    if (tapCount >= 10) {
                                        tapCount = 0
                                        firstTapTime = 0L
                                        showDevDialog = true
                                    }
                                })

                            Text(
                                text = stringResource(Res.string.tac_email_value),
                                style = TermsAndConditionsTextStyle.body()
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
                        ) {

                            Text(
                                text = stringResource(Res.string.tac_phone_label),
                                style = TermsAndConditionsTextStyle.bodyTitle()
                            )

                            Text(
                                text = stringResource(Res.string.tac_phone_value),
                                style = TermsAndConditionsTextStyle.body()
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
                        ) {

                            Text(
                                text = stringResource(Res.string.tac_office_label),
                                style = TermsAndConditionsTextStyle.bodyTitle()
                            )

                            Text(
                                text = stringResource(Res.string.tac_office_value),
                                style = TermsAndConditionsTextStyle.body()
                            )
                        }
                    }
                }
            }
        }


//    DevSignaturePasswordDialog(
//        visible = showDevDialog, onDismiss = {
//            showDevDialog = false
//        })
}

@Composable
private fun TermsSection(
    title: String,
    body: List<String> = emptyList(),
    bullets: List<String> = emptyList(),
    remark: String? = null
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        Text(
            text = title, style = TermsAndConditionsTextStyle.sectionTitle()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            body.forEach { text ->
                Text(
                    text = text, style = TermsAndConditionsTextStyle.body()
                )
            }
        }
        bullets.forEach { bullet ->
            Text(
                text = "• $bullet",
                style = TermsAndConditionsTextStyle.body(),
                modifier = Modifier.fillMaxWidth().padding(start = Spacing.dp12)
            )
        }
        remark?.let {
            Text(
                text = it, style = TermsAndConditionsTextStyle.body()
            )
        }
        Spacer(modifier = Modifier.height(Spacing.dp4))
        HorizontalDivider(
            color = Color(0xFFEAEAEA)
        )
    }
}

//@Composable
//fun DevSignaturePasswordDialog(
//    visible: Boolean,
//    onDismiss: () -> Unit,
//) {
//    if (!visible) return
//
//    var password by remember { mutableStateOf("") }
//    var showQuote by remember { mutableStateOf(false) }
//    var isError by remember { mutableStateOf(false) }
//
//    Dialog(
//        onDismissRequest = onDismiss
//    ) {
//        Card(
//            shape = RoundedCornerShape(Spacing.dp24),
//        ) {
//            AnimatedContent(
//                targetState = showQuote, label = "dev_signature_dialog"
//            ) { revealed ->
//
//                if (!revealed) {
//
//                    Column(
//                        Modifier.fillMaxWidth().padding(Spacing.dp24),
//                        Arrangement.spacedBy(Spacing.dp16)
//                    ) {
//
//                        Text(
//                            text = stringResource(Res.string.tac_dialog_restricted_access),
//                            style = MaterialTheme.typography.titleLarge,
//                        )
//
//                        OutlinedTextField(
//                            value = password,
//                            onValueChange = {
//                                password = it
//                                isError = false
//                            },
//                            singleLine = true,
//                            visualTransformation = PasswordVisualTransformation(),
//                            keyboardOptions = KeyboardOptions(
//                                keyboardType = KeyboardType.NumberPassword
//                            ),
//                            isError = isError,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.End
//                        ) {
//
//                            TextButton(
//                                onClick = onDismiss
//                            ) {
//                                Text(stringResource(Res.string.tac_dialog_cancel))
//                            }
//
//                            Spacer(modifier = Modifier.width(Spacing.dp8))
//
//                            Button(
//                                onClick = {
//                                    if (password == "2907") {
//                                        showQuote = true
//                                    } else {
//                                        isError = true
//                                    }
//                                }) {
//                                Text(stringResource(Res.string.tac_dialog_unlock))
//                            }
//                        }
//                    }
//
//                } else {
//
//                    Column(
//                        modifier = Modifier.fillMaxWidth().padding(
//                            horizontal = Spacing.dp24, vertical = Spacing.dp32
//                        ),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
//                    ) {
//
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.spacedBy(Spacing.dp6)
//                        ) {
//                            Text(
//                                text = stringResource(Res.string.tac_quote_savy_text),
//                                style = MaterialTheme.typography.titleMedium,
//                                textAlign = TextAlign.Center
//                            )
//
//                            Text(
//                                text = stringResource(Res.string.tac_quote_savy_author),
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                        }
//
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.spacedBy(Spacing.dp6)
//                        ) {
//                            Text(
//                                text = stringResource(Res.string.tac_quote_ayano_text),
//                                style = MaterialTheme.typography.titleMedium,
//                                textAlign = TextAlign.Center
//                            )
//
//                            Text(
//                                text = stringResource(Res.string.tac_quote_ayano_author),
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                        }
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.spacedBy(Spacing.dp6)
//                        ) {
//                            Text(
//                                text = stringResource(Res.string.tac_quote_pickle_text),
//                                style = MaterialTheme.typography.titleMedium,
//                                textAlign = TextAlign.Center
//                            )
//
//                            Text(
//                                text = stringResource(Res.string.tac_quote_pickle_author),
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                        }
//                    }
//
//                }
//            }
//        }
//    }
//}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TermsAndConditionsScreenPreview() {
    JantaNiveshTheme {
        TermsAndConditionsScreen()
    }
}