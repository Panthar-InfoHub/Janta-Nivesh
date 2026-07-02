package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.info_icon
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.FolioFundsMFViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.MutualFundIcon
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEvent
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FolioFundDomain

@Composable
fun FolioFundMFScreen(
    folioId: String,
    onBack: () -> Unit,
    onFundClick: (FolioFundDomain) -> Unit,
    onTopUp: (String) -> Unit,
) {
    val viewModel: FolioFundsMFViewModel = koinViewModel(parameters = { parametersOf(folioId) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        AppEventsController.appEvent.collect {
            when(it){
                AppEvent.PortfolioRefreshEvent ->{
                    viewModel.loadFolioFunds()
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) {
        BackHeader(title = "Funds", showBack = true, onBack = onBack, modifier = Modifier.padding(horizontal = Spacing.dp20))

        Box(
            modifier = Modifier.weight(1f).fillMaxSize(),
        ) {
            UiStateContainer(
                uiState = uiState,
                onRetry = { viewModel.loadFolioFunds() }
            ) { data ->
                FolioFundsContent(
                    folioId = folioId,
                    funds = data,
                    onFundClick = onFundClick,
                    onTopUpClick = { onTopUp(data[0].id) }
                )
            }
        }
    }
}

@Composable
fun FolioFundsContent(
    folioId: String,
    funds: List<FolioFundDomain>,
    onFundClick: (FolioFundDomain) -> Unit,
    onTopUpClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FolioInfoSection(folioId = folioId)
            }

            items(funds) { fund ->
                val percentage = fund.returnPercentage.replace("%", "").toDoubleOrNull() ?: 0.0
                val currentValue = (fund.amount + fund.`return`).toLong()

                MutualFundsFolioCard(
                    title = fund.title,
                    folio = fund.folio,
                    iconUrl = fund.imgUrl,
                    returnPercentage = percentage,
                    isSip = fund.isSip,
                    investedAmount = fund.amount,
                    currentValue = currentValue,
                    onClick = { onFundClick(fund) }
                )
            }
        }
        NextButtonFooter(
            value = "Top Up",
            onClick = { onTopUpClick() },
        )
    }
}

@Composable
fun FolioInfoSection(folioId: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.info_icon),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp).size(16.dp),
        )
        Text(
            text = "Folio no. ",
            style = tinyLabel,
            color = titleColor
        )
        Text(
            text = folioId,
            style = MaterialTheme.typography.titleSmall,
            color = titleColor
        )
    }
}

@Composable
fun MutualFundsFolioCard(
    title: String,
    folio: String,
    iconUrl: String,
    returnPercentage: Double,
    isSip: Boolean,
    investedAmount: Long,
    currentValue: Long,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.padding(vertical = 24.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                SubcomposeAsyncImage(
                    modifier = Modifier.size(44.dp),
                    model = iconUrl,
                    contentDescription = null,

                    loading = {
                        MutualFundIcon(
                            schemeName = title,
                            size = 44.dp
                        )
                    },

                    error = {
                        MutualFundIcon(
                            schemeName = title,
                            size = 44.dp,
                            backgroundColor = Color(0xffEFEDF3),
                            textColor = Primary
                        )
                    },

                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        text = title,
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                }
                Text(
                    text = if (isSip) "SIP" else "Lumpsum",
                    color = titleColor,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.Top)
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 20.dp),
                thickness = 0.5.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Invested",
                        style = MaterialTheme.typography.titleSmall,
                        color = titleColor
                    )

                    Text(
                        text = ("₹" + formatMoneyAfterL(investedAmount))
                            .withInterRupee(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = "Current Value",
                        style = MaterialTheme.typography.titleSmall,
                        color = titleColor
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = ("₹" + formatMoneyAfterL(currentValue))
                                .withInterRupee(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Primary
                        )
                        Text(
                            text = " ($returnPercentage%)",
                            style = MaterialTheme.typography.titleSmall,
                            color = if (returnPercentage>0) appGreen else appRed
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MutualFundsFolioCardPreview() {
    JantaNiveshTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FB))
                .padding(16.dp)
        ) {
            MutualFundsFolioCard(
                title = "SBI Bluechip Fund Direct Growth Fund Direct Growth",
                folio = "12345678/90",
                iconUrl = "",
                investedAmount = 125000,
                currentValue = 143000,
                returnPercentage = -32.5,
                isSip = true,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FolioFundsContentPreview() {
    val sampleFunds = listOf(
        FolioFundDomain(
            id = "1",
            title = "SBI Bluechip Fund Direct Growth",
            category = "Equity",
            amount = 125000,
            isSip = true,
            startDate = "2023-01-01",
            returnPercentage = "14.4%",
            `return` = 18000.0,
            xirr = "15.2%",
            currentNav = 150.0,
            avgNav = 130.0,
            folio = "12345678/90",
            balanceUnits = 961.53,
            imgUrl = "",
            schemeId = 1,
            orderId = ""
        ),
        FolioFundDomain(
            id = "2",
            title = "HDFC Top 100 Fund Direct Plan Growth",
            category = "Equity",
            amount = 50000,
            isSip = false,
            startDate = "2022-06-15",
            returnPercentage = "-5.2%",
            `return` = -2600.0,
            xirr = "-4.1%",
            currentNav = 80.0,
            avgNav = 85.0,
            folio = "98765432/11",
            balanceUnits = 588.23,
            imgUrl = "",
            schemeId = 2,
            orderId = ""
        )
    )

    JantaNiveshTheme {
        FolioFundsContent(
            folioId = "12345678/90",
            funds = sampleFunds,
            onFundClick = {},
            onTopUpClick = {},
        )
    }
}