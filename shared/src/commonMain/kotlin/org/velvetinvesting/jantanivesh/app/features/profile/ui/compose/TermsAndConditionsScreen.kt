package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import kotlin.time.Clock

object TermsAndConditionsTextStyle {

    @Composable

    fun screenTitle() =

        MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

    @Composable

    fun sectionTitle() =

        MaterialTheme.typography.labelLarge

    @Composable

    fun bodyTitle() =

        MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)

    @Composable

    fun body() =

        MaterialTheme.typography.bodySmall

}

@Composable

fun TermsAndConditionsScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier

) {

    var showDevDialog by remember {
        mutableStateOf(false)
    }

    var tapCount by remember { mutableIntStateOf(0) }
    var firstTapTime by remember { mutableLongStateOf(0L) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            BackHeader(
                title = "Terms & Conditions",
                showBack = true,
                onBack = onBack,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.dp16)
            )
        },
        containerColor = Color.White

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    PaddingValues(
                        start =24.dp,
                        end = 24.dp,
                        top = paddingValues.calculateTopPadding() + 24.dp,
                        bottom = paddingValues.calculateBottomPadding() + 24.dp
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Terms & Conditions",
                style = TermsAndConditionsTextStyle.screenTitle()
            )
            Text(
                text = "Please read these terms carefully before using Janta Nivesh Investing services.",
                style = TermsAndConditionsTextStyle.body()
            )
            Text(
                text = "Effective Date: March 30, 2025\nLast Updated: March 30, 2025",
                style = TermsAndConditionsTextStyle.body()
            )
            TermsSection(
                title = "1. About Janta Nivesh (a product under Velvet Investing)",
                body = listOf(
                    "Janta Nivesh Investing provides digital tools and services related to financial planning, goal tracking, investment facilitation, reporting, and related user experiences. Certain features may be enabled through third-party partners, including regulated intermediaries, technology service providers, KYC agencies, investment platforms, or financial institutions."
                )
            )

            TermsSection(
                title = "2. Eligibility",
                body = listOf(
                    "By using our services, you represent that:"
                ),
                bullets = listOf(
                    "you are legally capable of entering into a binding agreement",
                    "the information you provide is true, complete, and accurate",
                    "you will use the platform only for lawful purposes",
                    "you are authorized to provide all details submitted through your account"
                )
            )

            TermsSection(
                title = "3. Account Registration and Security",
                body = listOf(
                    "To access certain services, you may be required to register and verify your identity using OTP, KYC, or other authentication steps.\n" +
                            "You are responsible for:"
                ),
                remark = "You must notify us immediately if you suspect any unauthorized access or misuse.",

                bullets = listOf(

                    "maintaining the confidentiality of your login credentials",

                    "protecting unauthorized access to your account",

                    "ensuring that all activity under your account is authorized by you"

                )

            )

            TermsSection(

                title = "4. Services Provided",
                body = listOf("Janta Nivesh may offer services including:"),
                bullets = listOf(

                    "financial tracking",

                    "goal-based planning",

                    "investment recommendations",

                    "SIP management",

                    "mutual fund investments",

                    "financial insights"

                )
                ,
                remark = "Not all services may be available to all users at all times."

            )

            TermsSection(

                title = "5. No Personalized Investment Guarantee",

                body = listOf(
                    "Unless explicitly stated otherwise in a separate written advisory arrangement, the platform content, calculators, reports, and projections are for informational and facilitative purposes only. They should not be construed as guaranteed returns, assured outcomes, or a promise that any financial goal will be achieved.\n" +
                            "Investment decisions remain the user's responsibility."
                )

            )

            TermsSection(

                title = "6. User Responsibilities",

                body = listOf(
                    "You agree not to:"
                ),

                bullets = listOf(

                    "provide false, incomplete, or misleading information,",

                    "impersonate another person,",

                    "misuse the platform or attempt unauthorized access,",
                    "copy, reverse engineer, scrape, or exploit the platform without permission,",
                    "use the platform for unlawful, fraudulent, or abusive purposes."

                )

            )

            TermsSection(

                title = "7. KYC and Third-Party Dependencies",

                body = listOf(
                    "Certain services may require successful KYC, third-party verification, or partner approvals. Janta Nivesh is not responsible for delays, rejections, downtime, or restrictions caused by banking partners, KYC agencies, investment platforms, registrars, payment processors, or regulatory requirements."
                )

            )

            TermsSection(

                title = "8. Transactions and Instructions",
                body = listOf(
                    "By placing any investment, redemption, purchase, booking, or related instruction, you confirm that:"
                ),
                bullets = listOf(

                    "the details submitted by you are correct",

                    "you understand the product you are choosing",

                    "you authorize Janta Nivesh and/or its partners to process your request,",
                    "actual execution may depend on cut-off timings, partner validations, market conditions, and regulatory rules."
                ),
                remark="A transaction request is not deemed completed until confirmed by the relevant processing entity or partner."

            )

            TermsSection(

                title = "9. Fees and Charges",

                body = listOf(
                    "Any applicable fees, charges, taxes, levies, or deductions, if any, will be disclosed through the platform, through partner terms, or at the time of transaction, where applicable."
                )

            )

            TermsSection(

                title = "10. Intellectual Property",

                body = listOf(
                    "All platform content, including text, branding, design, UI, logos, software, graphics, and reports, unless otherwise stated, belongs to Janta Nivesh or its licensors and is protected by applicable intellectual property laws.\n" +
                            "You may not reproduce, distribute, modify, or commercially exploit any part of the platform without prior written permission."
                )

            )

            TermsSection(

                title = "11. Limitation of Liability",
                body = listOf("To the maximum extent permitted by law, Janta Nivesh shall not be liable for:"),

                bullets = listOf(
                    "any market losses",
                    "indirect or consequential damages",
                    "service interruptions",
                    "third-party failures",
                    "data delays",
                    "investment decisions made by users",
                    "technical or partner-side transaction issues beyond our reasonable control"
                )

            )

            TermsSection(

                title = "12. Indemnity",

                body = listOf(
                    "You agree to indemnify and hold harmless Janta Nivesh, its founders, employees, affiliates, and partners against claims, liabilities, damages, losses, or expenses arising from your misuse of the platform, breach of these Terms, or violation of law."
                )

            )

            TermsSection(

                title = "13. Suspension or Termination",
                body = listOf("We may suspend, restrict, or terminate access to your account if:"),
                bullets = listOf(
                    "required by law or regulation",
                    "we detect suspicious or fraudulent activity",
                    "you breach these Terms",
                    "your use creates operational, legal, or security risk"
                )

            )

            TermsSection(

                title = "14. Disclaimer of Warranties",

                body = listOf(
                    "The platform is provided on an \"as is\" and \"as available\" basis. We do not guarantee uninterrupted availability, error-free operation, or suitability for every user objective."
                )

            )

            TermsSection(

                title = "15. Governing Law and Jurisdiction",

                body = listOf(
                    "These Terms shall be governed by the laws of India. Any disputes shall be subject to the exclusive jurisdiction of the courts located in Gurugram, Haryana, unless otherwise required by applicable law."
                )

            )

            TermsSection(

                title = "16. Changes to Terms",

                body = listOf(
                    "We may revise these Terms from time to time. Updated Terms will be posted on this page with the latest effective date. Your continued use of the platform constitutes acceptance of such revised Terms"
                )

            )

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Text(
                    text = "17. Contact Us",
                    style = TermsAndConditionsTextStyle.sectionTitle()
                )

                Text(
                    text = "Janta Nivesh",
                    style = TermsAndConditionsTextStyle.body()
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Email",
                        style = TermsAndConditionsTextStyle.bodyTitle(),
                        modifier=Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
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
                        }
                    )

                    Text(
                        text = "Contact@jantanivesh.com",
                        style = TermsAndConditionsTextStyle.body()
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Phone",
                        style = TermsAndConditionsTextStyle.bodyTitle()
                    )

                    Text(
                        text = "+91 9992224817 / +91 7888353502",
                        style = TermsAndConditionsTextStyle.body()
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Office",
                        style = TermsAndConditionsTextStyle.bodyTitle()
                    )

                    Text(
                        text = "5th Floor, Two Horizon Centre, DLF Phase 5,\nGolf Course Road, Gurugram, India",
                        style = TermsAndConditionsTextStyle.body()
                    )
                }
            }
        }
    }

//
//    DevSignaturePasswordDialog(
//        visible = showDevDialog,
//        onDismiss = {
//            showDevDialog = false
//        }
//    )
}

@Composable
private fun TermsSection(
    title: String,
    body: List<String> = emptyList(),
    bullets: List<String> = emptyList(),
    remark: String?= null
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = TermsAndConditionsTextStyle.sectionTitle()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            body.forEach { text ->
                Text(
                    text = text,
                    style = TermsAndConditionsTextStyle.body()
                )
            }
        }
        bullets.forEach { bullet ->
            Text(
                text = "• $bullet",
                style = TermsAndConditionsTextStyle.body(),
                modifier = Modifier.fillMaxWidth().padding(start = 12.dp)
            )
        }
        remark?.let {
            Text(
                text = it,
                style = TermsAndConditionsTextStyle.body()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(
            color = Color(0xFFEAEAEA)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAndConditionsScreenPreview() {
    JantaNiveshTheme {
        TermsAndConditionsScreen()
    }
}
