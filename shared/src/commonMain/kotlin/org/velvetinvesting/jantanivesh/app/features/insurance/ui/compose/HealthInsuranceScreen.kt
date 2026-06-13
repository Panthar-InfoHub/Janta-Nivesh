package org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.health_insurance
import jantanivesh.shared.generated.resources.ins_annual_checkup
import jantanivesh.shared.generated.resources.ins_cashless_treatment
import jantanivesh.shared.generated.resources.ins_flexible_tenure
import jantanivesh.shared.generated.resources.ins_high_coverage
import jantanivesh.shared.generated.resources.ins_no_claim_bonus_health
import jantanivesh.shared.generated.resources.ins_tax_benefits
import jantanivesh.shared.generated.resources.ins_tax_saving
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InsuranceFeatureCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InsurancePopularPlansCard
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.HealthInsurancePlan
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.InsuranceEvent
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.InsuranceUiState
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.TermInsurancePlan


@Composable
fun HealthInsuranceScreen(
    state: InsuranceUiState,
    onEvent: (InsuranceEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold { pv ->
        Column(
            modifier.fillMaxSize().padding(horizontal = Spacing.dp16).padding(pv),

            ) {
            BackHeader(
                title = "Health Insurance",
                onBack = { onEvent(InsuranceEvent.OnBackClickedHealth) })

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(
                    bottom = Spacing.dp16
                )
            ) {

                item {
                    Text(
                        text = "Why Health Life?",
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
                                text = "Cashless Treatment",
                                icon = Res.drawable.ins_cashless_treatment,
                                modifier = Modifier.weight(1f)
                            )
                            InsuranceFeatureCard(
                                text = "Tax Saving",
                                icon = Res.drawable.ins_tax_saving,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            InsuranceFeatureCard(
                                text = "Annual Checkups",
                                icon = Res.drawable.ins_annual_checkup,
                                modifier = Modifier.weight(1f)
                            )
                            InsuranceFeatureCard(
                                text = "No Claims Bonus",
                                icon = Res.drawable.ins_no_claim_bonus_health,
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
                items(
                    state.healthInsurancePlansList
                ) { plan ->
                    InsurancePopularPlansCard(
                        id = plan.id,
                        icon = plan.icon,
                        title = plan.title,
                        subTitle = plan.subString,
                        tag = plan.tag,
                        coverAmount = plan.coverAmount,
                        premium = plan.premiumAmount,
                        onClick = { onEvent(InsuranceEvent.OnProductClickedHealth(id = plan.id)) }
                    )
                }
            }



            AppButton(
                "Request Callback",
                onClick = { onEvent(InsuranceEvent.OnRequestCallBackClickedHealth) },
                modifier = Modifier.fillMaxWidth()
            )

        }


    }
}



@Preview(showBackground = true)
@Composable
fun InsuranceHealthPreview() {
    JantaNiveshTheme {
        HealthInsuranceScreen(
            state = InsuranceUiState(
                healthInsurancePlansList = listOf(
                    HealthInsurancePlan(
                        id = "1",
                        icon = "",
                        title = "Care Supreme",
                        subString = "Care Health",
                        tag = "Most Popular",
                        coverAmount = "₹10 L",
                        premiumAmount = "₹850"
                    ),
                    HealthInsurancePlan(
                        id = "2",
                        icon = "",
                        title = "Optima Secure",
                        subString = "HDFC ERGO",
                        tag = "Cashless",
                        coverAmount = "₹10 L",
                        premiumAmount = "₹1,100"
                    ),
                    HealthInsurancePlan(
                        id = "3",
                        icon = "",
                        title = "Star Comprehensive",
                        subString = "IStar Health",
                        tag = "Best Value",
                        coverAmount = "₹1 Cr",
                        premiumAmount = "₹920"
                    )
                )
            ),
            onEvent = {}
        )
    }
}