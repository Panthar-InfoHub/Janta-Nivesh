package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchasePlan
import org.velvetinvesting.jantanivesh.app.features.plans.ui.compose.ChoosePlanScreen
import org.velvetinvesting.jantanivesh.app.features.plans.ui.compose.PlansHomeScreen
import org.velvetinvesting.jantanivesh.app.features.plans.ui.compose.PurchaseSuccessScreen
import org.velvetinvesting.jantanivesh.app.features.plans.ui.compose.RedeemScreen
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.ChoosePlanEffect
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.ChoosePlanViewModel
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.RedeemViewModel

@Composable
fun PlansNavigation() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { pv ->
        NavHost(
            navController = navController,
            startDestination = Route.PlansHome,
            modifier = Modifier
                .fillMaxSize()
        ) {
            composable<Route.PlansHome> {
                PlansHomeScreen(
                    onPurchaseClick = {
                        navController.navigate(Route.ChoosePlan) {
                            launchSingleTop = true
                        }
                    },
                    onRedeemClick = {
                        navController.navigate(Route.Redeem) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Route.ChoosePlan> {
                val vm: ChoosePlanViewModel = koinViewModel()

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            is ChoosePlanEffect.PurchasePlanConfirmed -> {
                                navController.navigate(
                                    Route.PurchaseSuccess(
                                        schemeName = effect.schemeName,
                                        amount = effect.plan.amount,
                                        installmentDay = effect.plan.installmentDay ?: 0,
                                        startDate = effect.plan.startDate.orEmpty()
                                    )
                                ) {
                                    // The plan is registered — going back to the form would only
                                    // invite a duplicate SIP.
                                    popUpTo<Route.ChoosePlan> { inclusive = true }
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                ChoosePlanScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.PurchaseSuccess> { entry ->
                val route = entry.toRoute<Route.PurchaseSuccess>()

                PurchaseSuccessScreen(
                    plan = PurchasePlan(
                        id = "",
                        state = PurchasePlan.CONFIRMED,
                        scheme = "",
                        folioNumber = null,
                        amount = route.amount,
                        frequency = "monthly",
                        installmentDay = route.installmentDay.takeIf { it > 0 },
                        numberOfInstallments = null,
                        remainingInstallments = null,
                        startDate = route.startDate.takeIf { it.isNotBlank() }
                    ),
                    schemeName = route.schemeName,
                    onViewHoldingsClick = {
                        navController.navigate(Route.Redeem) {
                            launchSingleTop = true
                            popUpTo<Route.PlansHome>()
                        }
                    },
                    onDoneClick = {
                        navController.popBackStack(Route.PlansHome, inclusive = false)
                    }
                )
            }

            composable<Route.Redeem> {
                val vm: RedeemViewModel = koinViewModel()
                val state by vm.uiState.collectAsStateWithLifecycle()

                RedeemScreen(
                    state = state,
                    handleEvent = vm::handleEvent,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
