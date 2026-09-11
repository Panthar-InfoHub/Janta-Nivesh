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
        title = "निवेश, अब बिल्कुल आसान",
        subtitle = "छोटी शुरुआत करें और अपने कल के लिए निवेश करें।"
    ),
    PagerImage(
        Res.drawable.img_secondpager,
        title = "निवेश, अपनी भाषा में",
        subtitle = "अपनी भाषा में समझें और आसानी से निवेश करें।"
    ),
    PagerImage(
        Res.drawable.img_thirdpager,
        title = "निवेश, हर किसी के लिए",
        subtitle = "आप जो भी काम करते हैं, आप भी अपने सपनों के लिए निवेश कर सकते हैं।"
    )
)