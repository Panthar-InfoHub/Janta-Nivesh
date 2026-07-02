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
import jantanivesh.shared.generated.resources.ins_assistance
import jantanivesh.shared.generated.resources.ins_cover
import jantanivesh.shared.generated.resources.ins_no_claim_bonus_general
import jantanivesh.shared.generated.resources.ins_quick_claim
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InsuranceFeatureCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter


@Composable
fun GeneralInsuranceScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onRequestCallBackClick: () -> Unit
){
    Scaffold {
        Column(modifier.fillMaxSize()
            .background(White)) {
            BackHeader(
                title = "General Insurance",
                onBack = { onBack() },
                modifier= Modifier.padding(horizontal = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(
                    Spacing.dp16
                ),
                contentPadding = PaddingValues(
                    bottom = Spacing.dp16
                )
            ) {

                item {
                    Text(
                        text = "Why General Life?",
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
                                text = "Comprehensive Cover",
                                icon = Res.drawable.ins_cover,
                                modifier = Modifier.weight(1f)
                            )
                            InsuranceFeatureCard(
                                text = "Quick Claims",
                                icon = Res.drawable.ins_quick_claim,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            InsuranceFeatureCard(
                                text = "24x7 Assistance",
                                icon = Res.drawable.ins_assistance,
                                modifier = Modifier.weight(1f)
                            )
                            InsuranceFeatureCard(
                                text = "No Claims Bonus",
                                icon = Res.drawable.ins_no_claim_bonus_general,
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
//                    state.generalInsurancePlansList
//                ) { plan ->
//                    InsurancePopularPlansCardGeneral(
//                        id = plan.id,
//                        icon = plan.icon,
//                        title = plan.title,
//                        subTitle = plan.subString,
//                        coverAmount = plan.coverAmount,
//                        premium = plan.premiumAmount,
//                        onClick = { onEvent(InsuranceEvent.OnProductClickedGeneral(id = plan.id)) }
//                    )
//                }
            }



            NextButtonFooter(
                value = "Request Callback",
                onClick = { onRequestCallBackClick()},
                modifier = Modifier.fillMaxWidth()
            )

        }
    }


}

//@Preview(showBackground = true)
//@Composable
//fun InsuranceGeneralPreview() {
//    JantaNiveshTheme {
//        GeneralInsuranceScreen(
//            state = InsuranceUiState(
//                generalInsurancePlansList = listOf(
//                    GeneralInsurancePlan(
//                        id = "1",
//                        icon = "",
//                        title = "Car Insurance",
//                        subString = "Reliance General",
//
//                        coverAmount = "₹25,000",
//                        premiumAmount = "₹45"
//                    ),
//                    GeneralInsurancePlan(
//                        id = "2",
//                        icon = "",
//                        title = "Bike Insurance",
//                        subString = "HDFC ERGO",
//
//                        coverAmount = "₹5,000",
//                        premiumAmount = "₹12"
//                    ),
//                    GeneralInsurancePlan(
//                        id = "3",
//                        icon = "",
//                        title = "Home Insurance",
//                        subString = "ICICI Lombard",
//
//                        coverAmount = "₹250,000",
//                        premiumAmount = "₹85"
//                    )
//                )
//            ),
//            onEvent = {}
//        )
//    }
//}