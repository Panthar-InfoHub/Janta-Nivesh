package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.ExistingFundsLumpSumSideEffect
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.ExistingFundsLumpSumViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.LumpSumAdd
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.subHeading
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.MoneyTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.MutualFundIcon
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.core.utils.rememberBrowserReturnLauncher
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundPortfolioDomain

@Composable
fun ExistingFundLumpSumScreen(
    onBack: () -> Unit,
) {
    val viewModel: ExistingFundsLumpSumViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedFunds by viewModel.addedFundList.collectAsStateWithLifecycle()
    val buttonLoading by viewModel.buttonLoading.collectAsStateWithLifecycle()

    val browserLauncher = rememberBrowserReturnLauncher()

    LaunchedEffect(Unit){
        viewModel.sideEffect.collect{
            when(it){
                is ExistingFundsLumpSumSideEffect.OpenBrowserLink -> {
                    browserLauncher.launch(it.link){
                        viewModel.loadPortfolio()
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) {
        BackHeader(title = "Existing Funds", showBack = true, onBack = onBack)

        Box(
            modifier = Modifier.weight(1f).fillMaxSize(),
        ) {
            UiStateContainer(
                uiState = uiState,
                onRetry = { viewModel.loadPortfolio() }
            ) { data ->
                FolioFundsLumpSumContent(
                    funds = data.mutualFunds,
                    selectedFunds =selectedFunds,
                    onTopUpClick = {
                        viewModel.investMoreLumpsum()
                    },
                    onAddFund = { prodId, folio, amount ->
                        viewModel.addOrUpdateFund(
                            prodId = prodId,
                            folio = folio,
                            amount = amount
                        )
                    },
                    onRemove = { folioId ->
                        viewModel.removeFund(folioId)
                    },
                    buttonLoading= buttonLoading
                )
            }
        }
    }
}

@Composable
fun FolioFundsLumpSumContent(
    funds: List<MutualFundPortfolioDomain>,
    onTopUpClick: () -> Unit,
    selectedFunds: List<LumpSumAdd>,
    onAddFund: (String, String, Long) -> Unit,
    onRemove: (String) -> Unit,
    buttonLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(funds) { fund ->
                val percentage = fund.returnPercentage.replace("%", "").toDoubleOrNull() ?: 0.0
                val currentValue = (fund.currentValue).toLong()

                val selectedFund = selectedFunds.firstOrNull { it.folio == fund.folio }

                MutualFundsFolioCardLumpSum(
                    title = fund.title,
                    folio = fund.folio,
                    iconUrl = fund.icon,
                    returnPercentage = percentage,
                    investedAmount = fund.amount.toLong(),
                    currentValue = currentValue,
                    minAmount = fund.minLumpSumAmount,
                    extraAmountAdded = selectedFund != null,
                    extraAmount = selectedFund?.amount ?: 0L,
                    onAddClick = { amount ->
                        onAddFund(
                            fund.schemeId.toString(),
                            fund.folio,
                            amount
                        )
                    },
                    onRemove = {
                        onRemove(fund.folio)
                    }
                )
            }
        }
        NextButtonFooter(
            value = "Top Up",
            onClick = { onTopUpClick() },
            enabled = selectedFunds.isNotEmpty(),
            loading = buttonLoading
        )
    }
}

@Composable
fun MutualFundsFolioCardLumpSum(
    title: String,
    folio: String,
    iconUrl: String,
    returnPercentage: Double,
    investedAmount: Long,
    currentValue: Long,
    onAddClick: (Long) -> Unit,
    minAmount: Long,
    extraAmountAdded: Boolean,
    extraAmount: Long,
    onRemove: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var addAmount by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(LocalShapes.current.roundedDp15)
            .clip(LocalShapes.current.roundedDp15)
            .background(Color.White)
            .clickable{
                expanded = !expanded
            }.then(
                if (extraAmountAdded)
                    Modifier.border(1.dp,Primary, LocalShapes.current.roundedDp15)
                else Modifier
            ),
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
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Folio no. ",
                            color = titleColor,
                            style = tinyLabel
                        )

                        Text(
                            text = folio,
                            color = Color(0xff919191),
                            style = tinyLabel
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 20.dp),
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
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 20.dp),
                thickness = 0.5.dp
            )
            AnimatedVisibility(!expanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (extraAmountAdded) {
                        Column {
                            Text(
                                text = "Selected Amount",
                                style = MaterialTheme.typography.titleSmall,
                                color = titleColor
                            )

                            Text(
                                text = ("₹" + formatMoneyAfterL(extraAmount))
                                    .withInterRupee(),
                                style = MaterialTheme.typography.labelSmall,
                                color = appGreen
                            )
                        }

                        Row{
                            Text(
                                text = "Edit",
                                style = subHeading,
                                color = Primary,
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    addAmount = extraAmount.toString()
                                    expanded = true
                                }
                            )
                            Text(
                                text = " | ",
                                style = subHeading,
                                color = Primary,
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    addAmount = extraAmount.toString()
                                    expanded = true
                                }
                            )
                            Text(
                                text = "Remove",
                                style = subHeading,
                                color = appRed,
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onRemove()
                                }
                            )
                        }
                    } else {
                        Text(
                            text = "+ Click to add more",
                            style = subHeading,
                            color = Primary,
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                expanded = true
                            }
                        )
                    }
                }
            }
            AnimatedContent(expanded){
                if (it){
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 20.dp),
                         verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Enter the Amount",
                            style = MaterialTheme.typography.titleSmall,
                            color = titleColor
                        )
                        MoneyTextField(
                            value = addAmount,
                            onValueChange = {value->
                                addAmount = value.filter { it.isDigit() }
                            },
                            placeHolder = "Add More Amount",
                            showWarning = (addAmount.toLongOrNull()?: 0L) < minAmount && addAmount.isNotEmpty(),
                            warningText = "Amount Must be more than ₹${minAmount}",
                            modifier = Modifier.fillMaxWidth()
                        )

                        AppButton(
                            text = "Add",
                            onClick = {
                                addAmount.toLongOrNull()?.let{
                                    onAddClick(it)
                                    expanded = false
                                }
                            },
                            modifier = Modifier.width(120.dp).height(40.dp),
                            style = AppButtonDefaults.style(shape = LocalShapes.current.roundedDp12),
                            enabled = (addAmount.toLongOrNull()?: 0L) >= minAmount
                        )

                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun FolioFundsContentLSPreview() {
    val sampleFunds = listOf(
        MutualFundPortfolioDomain(
            id = "1",
            title = "SBI Bluechip Fund Direct Growth",
            category = "Equity",
            amount = 125000.0,
            currentValue = 143000.0,
            returnAmount = 18000.0,
            returnPercentage = "14.4%",
            folio = "12345678/90",
            icon = "",
            minSipAmount = 500L,
            minLumpSumAmount = 5000L,
            schemeId = 1,
            balanceUnits = 961.53
        ),
        MutualFundPortfolioDomain(
            id = "2",
            title = "HDFC Top 100 Fund Direct Plan Growth",
            category = "Equity",
            amount = 50000.0,
            currentValue = 47400.0,
            returnAmount = -2600.0,
            returnPercentage = "-5.2%",
            folio = "98765432/11",
            icon = "",
            minSipAmount = 100L,
            minLumpSumAmount = 1000L,
            schemeId = 2,
            balanceUnits = 588.23
        ),
        MutualFundPortfolioDomain(
            id = "3",
            title = "ICICI Prudential Technology Fund",
            category = "Sectoral/Thematic",
            amount = 75000.0,
            currentValue = 92000.0,
            returnAmount = 17000.0,
            returnPercentage = "22.7%",
            folio = "11223344/55",
            icon = "",
            minSipAmount = 100L,
            minLumpSumAmount = 5000L,
            schemeId = 3,
            balanceUnits = 845.12
        ),
        MutualFundPortfolioDomain(
            id = "4",
            title = "Parag Parikh Flexi Cap Fund",
            category = "Flexi Cap",
            amount = 100000.0,
            currentValue = 118500.0,
            returnAmount = 18500.0,
            returnPercentage = "18.5%",
            folio = "55667788/99",
            icon = "",
            minSipAmount = 1000L,
            minLumpSumAmount = 1000L,
            schemeId = 4,
            balanceUnits = 421.76
        )
    )

    JantaNiveshTheme {
        FolioFundsLumpSumContent(
            funds = sampleFunds,
            onTopUpClick = {},
            selectedFunds = emptyList(),
            { _, _, _ ->},
            {},
            false
        )
    }
}