package org.velvetinvesting.jantanivesh.app.features.splashscreen.domain.models

import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.img_first_pager
import jantanivesh.shared.generated.resources.img_secondpager
import jantanivesh.shared.generated.resources.img_thirdpager
import org.jetbrains.compose.resources.DrawableResource

data class PagerImage(
    val image: DrawableResource,
    val title: String,
    val subtitle: String
)

val pagerImageList = listOf(
    PagerImage(
        Res.drawable.img_first_pager,
        title = "Invest Smarter, Grow with Purpose",
        subtitle = "Explore Mutual funds and fixed deposits designed to support your wealth-building journey."
    ),
    PagerImage(
        Res.drawable.img_secondpager,
        title = "Keep your money safe!",
        subtitle = "Discover mutual funds and fixed deposits tailored to enhance your wealth-building journey."
    ),
    PagerImage(
        Res.drawable.img_thirdpager,
        title = "Choose own native language",
        subtitle = "Discover mutual funds and fixed deposits that can enhance your wealth!"
    )
)