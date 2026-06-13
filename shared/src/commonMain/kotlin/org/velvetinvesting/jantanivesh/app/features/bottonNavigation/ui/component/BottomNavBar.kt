package org.velvetinvesting.jantanivesh.app.features.bottonNavigation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.nav_icon_full_screener
import jantanivesh.shared.generated.resources.nav_icon_home
import jantanivesh.shared.generated.resources.nav_icon_incurance
import jantanivesh.shared.generated.resources.nav_icon_portfolio
import jantanivesh.shared.generated.resources.nav_icon_profile
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.navigation.Route
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary


@Composable
fun BottomNavBar(
    currentDestination: NavDestination?,
    onNavigate: (Any) -> Unit
) {
    val bottomBarItems = listOf(Route.Home, Route.FundScreener, Route.PortFolio, Route.Insurance, Route.Profile)
    val itemsLabels= listOf("Home","Fund Screener","Portfolio", "Insurance","Profile")
    val icons= listOf(
        Res.drawable.nav_icon_home,
        Res.drawable.nav_icon_full_screener,Res.drawable.nav_icon_portfolio,Res.drawable.nav_icon_incurance,Res.drawable.nav_icon_profile)

    Box(modifier=Modifier.fillMaxWidth()
        .dropShadow(
            shadow = Shadow(
                radius = 16.dp,
                color = Color(0xffDEE2F6)
            ),
            shape = RectangleShape
        )
        .background(Color.Red),
        contentAlignment = Alignment.Center) {

        NavigationBar(containerColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        ){
            bottomBarItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(item::class)
                    } == true,
                    onClick = { onNavigate(item) },
                    icon = {
                        Icon(
                            painter = painterResource(icons[index]),
                            contentDescription = itemsLabels[index],
                            modifier = Modifier.size(28.dp),
                        )
                    },
                    label = {
                        Text(
                            text=itemsLabels[index],
                            fontFamily = InterFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Primary,
                        unselectedTextColor = Primary,
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Secondary,
                        selectedTextColor = Secondary
                    )
                )
            }
        }
    }
}