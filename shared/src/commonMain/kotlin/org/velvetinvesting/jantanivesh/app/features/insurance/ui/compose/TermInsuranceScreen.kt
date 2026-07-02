package org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.health_insurance
import jantanivesh.shared.generated.resources.ins_flexible_tenure
import jantanivesh.shared.generated.resources.ins_high_coverage
import jantanivesh.shared.generated.resources.ins_tax_benefits
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InsuranceFeatureCard

@Composable
fun TermInsuranceScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onRequestCallBackClick: () -> Unit
) {
    Scaffold {
        Column(
            modifier.fillMaxSize()
                .background(White).padding(horizontal = 16.dp),

        ) {
            BackHeader(
                title = "Term Insurance",
                onBack = {onBack() }
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16), contentPadding = PaddingValues(bottom = Spacing.dp16)
            ) {

                item {
                    Text(
                        text = "Why Term Life?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            InsuranceFeatureCard(
                                text = "High Coverage",
                                icon = Res.drawable.ins_high_coverage,
                                modifier = Modifier.weight(1f)
                            )
                            InsuranceFeatureCard(
                                text = "Tax Benefits",
                                icon = Res.drawable.ins_tax_benefits,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            InsuranceFeatureCard(
                                text = "Financial Security",
                                icon = Res.drawable.health_insurance,
                                modifier = Modifier.weight(1f)
                            )
                            InsuranceFeatureCard(
                                text = "Flexible Tenure",
                                icon = Res.drawable.ins_flexible_tenure,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Text(
                        "Popular Plans",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
//                items(
//                    state.termInsurancePlansList
//                ) { plan ->
//                    InsurancePopularPlansCard(
//                        id = plan.id,
//                        icon = plan.icon,
//                        title = plan.title,
//                        subTitle = plan.subString,
//                        tag = plan.tag,
//                        coverAmount = plan.coverAmount,
//                        premium = plan.premiumAmount,
//                        onClick = { onEvent(InsuranceEvent.OnProductClickedTerm(id = plan.id)) }
//                    )
//                }
            }



            AppButton(
                "Request Callback",
                onClick = { onRequestCallBackClick() },
                modifier = Modifier.fillMaxWidth()
            )

        }


    }
}

//@Preview(showBackground = true)
//@Composable
//fun InsuranceFeatureCardPreview() {
//    JantaNiveshTheme {
//        TermInsuranceScreen(
//            state = InsuranceUiState(
//                termInsurancePlansList = listOf(
//                    TermInsurancePlan(
//                        id = "1",
//                        icon = "",
//                        title = "LIC Tech Term",
//                        subString = "Life Insurance Corp.",
//                        tag = "Govt. Backed",
//                        coverAmount = "₹1 Cr",
//                        premiumAmount = "₹8,400"
//                    ),
//                    TermInsurancePlan(
//                        id = "2",
//                        icon = "",
//                        title = "Click 2 Protect",
//                        subString = "HDFC Life",
//                        tag = "Popular",
//                        coverAmount = "₹1 Cr",
//                        premiumAmount = "₹9,200"
//                    ),
//                    TermInsurancePlan(
//                        id = "3",
//                        icon = "",
//                        title = "iProtect Smart",
//                        subString = "ICICI Prudential",
//                        tag = "Best Value",
//                        coverAmount = "₹1 Cr",
//                        premiumAmount = "₹8,150"
//                    )
//                )
//            ),
//            onEvent = {}
//        )
//    }
//}