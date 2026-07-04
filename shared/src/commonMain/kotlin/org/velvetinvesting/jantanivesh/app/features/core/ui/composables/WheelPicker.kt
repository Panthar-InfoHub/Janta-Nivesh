package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.toString

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> WheelPicker(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialItem: T?,
    visibleItems: Int = 5,
    itemHeight: Dp = 44.dp,
    onSelectionChanged: (T) -> Unit,
){

    val centerIndex = visibleItems / 2

    val initialIndex = remember(items, initialItem) {

        items.indexOf(initialItem)
            .takeIf { it >= 0 }
            ?: 0
    }

    val listState = rememberLazyListState()

    val flingBehavior = rememberSnapFlingBehavior(
        lazyListState = listState
    )

    LaunchedEffect(Unit) {

        listState.scrollToItem(
            initialIndex
        )
    }


    val selectedIndex by remember {

        derivedStateOf {

            val viewportCenter =
                (listState.layoutInfo.viewportStartOffset +
                        listState.layoutInfo.viewportEndOffset) / 2

            listState.layoutInfo.visibleItemsInfo
                .minByOrNull {
                    abs(
                        (it.offset + it.size / 2) -
                                viewportCenter
                    )
                }
                ?.index
                ?: initialIndex
        }
    }

    LaunchedEffect(selectedIndex) {

        items.getOrNull(selectedIndex)
            ?.let(onSelectionChanged)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight * visibleItems),
        contentAlignment = Alignment.Center
    ) {

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                vertical = itemHeight * centerIndex
            )
        ) {

            itemsIndexed(items) { index, item ->

                val distance = abs(index - selectedIndex)

                val selected = distance == 0

                val fontSize = when (distance) {
                    0 -> 22.sp
                    1 -> 16.sp
                    2 -> 14.sp
                    else -> 14.sp
                }

                val alpha = when (distance) {
                    0 -> 1f
                    1 -> .9f
                    2 -> .65f
                    else -> .45f
                }

                val linePadding = 32.dp

                val lineThickness = 0.8.dp

                val lineAlpha = 0.2f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .graphicsLayer {
                            this.alpha = alpha
                        },

                    contentAlignment = Alignment.Center
                ) {

                    if (index != 0) {

                        HorizontalDivider(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .padding(horizontal = linePadding),

                            thickness = lineThickness,

                            color = MaterialTheme.colorScheme.outline.copy(
                                alpha = lineAlpha
                            )
                        )
                    }

                    if (index != items.lastIndex) {

                        HorizontalDivider(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(horizontal = linePadding),

                            thickness = lineThickness,

                            color = MaterialTheme.colorScheme.outline.copy(
                                alpha = lineAlpha
                            )
                        )
                    }

                    Text(
                        text = item.toString(),

                        fontSize = fontSize,
                        fontFamily = InterFontFamily,
                        fontWeight = if (selected) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        },

                        color = if (selected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun YearPicker(
    modifier: Modifier = Modifier,
    selectedYear: Int?,
    startOffsetYears: Int = 1,
    yearsAhead: Int = 100,
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {

    val currentYear = remember {

        Clock.System.now()
            .toLocalDateTime(
                TimeZone.currentSystemDefault()
            )
            .year
    }

    val years = remember {

        (
                currentYear + startOffsetYears..
                        currentYear + yearsAhead
                ).toList()
    }

    val initialYear = selectedYear ?: years.first()

    var wheelYear by remember(selectedYear) {
        mutableIntStateOf(initialYear)
    }
    Box(
        modifier=modifier.fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(
                onClick = onDismiss,
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            )
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier= Modifier.fillMaxWidth()
                .clip(LocalShapes.current.roundedDp15)
                .clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }
                ) {}
                .background(Color.White)
                .padding(top = 28.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Select Year",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFontFamily,
                color = Color.Black
            )
            WheelPicker(
                modifier = Modifier,
                items = years,
                initialItem = initialYear,
                onSelectionChanged = {
                    wheelYear = it
                }
            )
            Row(
                modifier=Modifier.fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InvertedAppButton(
                    text = "Cancel",
                    onClick =onDismiss,
                    modifier = Modifier.height(48.dp).weight(1f)
                )
                AppButton(
                    text = "Confirm",
                    onClick = {
                        onYearSelected(wheelYear)
                        onDismiss()
                    },
                    modifier = Modifier.height(48.dp).weight(1f)
                )

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun YearPickerPreview() {

    var selectedYear by remember {

        mutableIntStateOf(
            Clock.System.now()
                .toLocalDateTime(
                    TimeZone.currentSystemDefault()
                )
                .year + 1
        )
    }

    YearPicker(
        selectedYear = selectedYear,
        onYearSelected = {
            selectedYear = it
        },
        onDismiss = {}
    )
}