package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundGraphPointsDomain
import kotlin.math.abs
import kotlin.time.Instant

@Composable
fun NavLineChart(
    data: List<MutualFundGraphPointsDomain>,
    color: Color = Secondary,
    modifier: Modifier = Modifier.height(140.dp),
    progress: Float = 1f
) {
    var touchX by remember { mutableStateOf<Float?>(null) }
    val alpha by animateFloatAsState(if (touchX != null) 1f else 0f)
    val pathMeasureLine = remember { PathMeasure() }
    val textMeasurer = rememberTextMeasurer()
    val textStyle=titlesStyle

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> touchX = offset.x },
                    onDrag = { change, _ -> touchX = change.position.x },
                    onDragEnd = { touchX = null },
                    onDragCancel = { touchX = null }
                )
            }
    ) {


        if (data.size < 2) return@Canvas

        val width = size.width
        val height = size.height

        val chartStartX = 24f
        val chartEndX = width- 10f
        val chartTop = 60f
        val chartBottom = height - 50f

        val chartWidth = chartEndX - chartStartX
        val chartHeight = chartBottom - chartTop

        val maxNav = data.maxOf { it.navValue }

        val stepX = chartWidth / (data.size - 1)

        val points = data.mapIndexed { index, item ->
            val x = chartStartX + index * stepX

            val normalized =
                if (maxNav == 0.0) 0f
                else (item.navValue / maxNav).toFloat()

            val y = chartBottom - normalized * chartHeight

            Offset(x, y)
        }

        val activePoint = touchX?.let { tx ->
            points.minByOrNull { abs(it.x - tx) }
        }

        val activeIndex = activePoint?.let { points.indexOf(it) }

        /* ---------- Y-AXIS & GRID ---------- */
        val yPointsCount = 5
        for (i in 0 until yPointsCount) {
            val fraction = i / (yPointsCount - 1).toFloat()
            val valY = maxNav * fraction
            val y = chartBottom - fraction * chartHeight

            val label = valY.toInt().toString()
            val textLayout = textMeasurer.measure(
                AnnotatedString(label),
                style = textStyle.copy(fontSize = 10.sp, color = Color.Gray)
            )
            drawText(
                textLayout,
                topLeft = Offset(chartStartX - textLayout.size.width - 20f, y - textLayout.size.height / 2)
            )

            drawLine(
                color = Color.Gray.copy(alpha = 0.1f),
                start = Offset(chartStartX, y),
                end = Offset(chartEndX, y),
                strokeWidth = 1f
            )
        }

        drawLine(
            color = Color.Gray.copy(alpha = 0.3f),
            start = Offset(chartStartX, chartTop),
            end = Offset(chartStartX, chartBottom),
            strokeWidth = 2f
        )

        drawLine(
            color = Color.Gray.copy(alpha = 0.3f),
            start = Offset(chartStartX, chartBottom),
            end = Offset(chartEndX, chartBottom),
            strokeWidth = 2f
        )

        /* ---------- X-AXIS LABELS ---------- */
        val xPointsCount = 4
        val xIndices = List(xPointsCount) { i -> i * (data.size - 1) / (xPointsCount - 1) }
        xIndices.forEach { index ->
            val x = (chartStartX + index * stepX).coerceIn(maximumValue = chartEndX-20f, minimumValue = chartStartX+10f)
            val dateStr = data[index].date.toAxisDate()
            val textLayout = textMeasurer.measure(
                AnnotatedString(dateStr),
                style = textStyle.copy(fontSize = 10.sp, color = Color.Gray)
            )
            drawText(
                textLayout,
                topLeft = Offset(x - textLayout.size.width / 2, chartBottom + 36f)
            )
        }

        /* ---------- LINE PATH ---------- */

//        val linePath = Path().apply {
//            moveTo(points.first().x, points.first().y)
//            for (i in 1 until points.size) {
//                lineTo(points[i].x, points[i].y)
//            }
//        }

        val linePath = Path().apply {
            if (points.isEmpty()) return@apply

            moveTo(points.first().x, points.first().y)

            if (points.size == 1) return@apply

            // Catmull-Rom to Cubic Bezier conversion
            for (i in 0 until points.size - 1) {
                val p0 = points[(i - 1).coerceAtLeast(0)]
                val p1 = points[i]
                val p2 = points[i + 1]
                val p3 = points[(i + 2).coerceAtMost(points.size - 1)]

                // Control points derived from Catmull-Rom tension (0.3f = subtle smoothing)
                val tension = 0.3f

                val cp1x = p1.x + (p2.x - p0.x) * tension
                val cp1y = p1.y + (p2.y - p0.y) * tension

                val cp2x = p2.x - (p3.x - p1.x) * tension
                val cp2y = p2.y - (p3.y - p1.y) * tension

                cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
            }
        }

        pathMeasureLine.setPath(linePath, false)

        val animatedPath = Path()
        pathMeasureLine.getSegment(
            0f,
            pathMeasureLine.length * progress,
            animatedPath,
            true
        )

        /* ---------- AREA FILL ---------- */

        val areaPath = Path()
        pathMeasureLine.getSegment(
            0f,
            pathMeasureLine.length * progress,
            areaPath,
            true
        )

        val visibleX = chartStartX + chartWidth * progress

        areaPath.lineTo(visibleX, chartBottom)
        areaPath.lineTo(points.first().x, chartBottom)
        areaPath.close()

        drawPath(
            path = areaPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0.35f),
                    color.copy(alpha = 0.05f),
                    Color.Transparent
                ),
                startY = chartTop,
                endY = chartBottom
            )
        )

        /* ---------- LINE ---------- */

        drawPath(
            animatedPath,
            color,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )

        /*-------------Label Line & Text ------------------*/

        val topLinePadding= textStyle.fontSize.toPx() +12f

        activePoint?.let { point ->
            drawLine(
                color = color.copy(alpha = 0.6f),
                start = Offset(point.x, topLinePadding),
                end = Offset(point.x, size.height),
                strokeWidth = 2f,
                alpha=alpha
            )
        }


        activeIndex?.let { index ->
            val labelText = data[index].label

            val textLayout = textMeasurer.measure(
                AnnotatedString(labelText),
                style = textStyle
            )


            var labelX = points[index].x - textLayout.size.width / 2


            labelX = labelX.coerceIn(
                chartStartX,
                size.width - textLayout.size.width
            )

            val labelY = 0f

            drawText(
                textLayout,
                topLeft = Offset(labelX, labelY),
                alpha = alpha
            )
        }
    }
}

private fun String.toAxisDate(): String {
    return try {
        val instant = Instant.parse(this)
        val date = instant.toLocalDateTime(TimeZone.UTC).date
        val month = date.month.name
            .lowercase()
            .replaceFirstChar { it.uppercase() }
            .take(3)

        val shortYear = (date.year % 100).toString().padStart(2, '0')

        "$month'$shortYear"
    } catch (e: Exception) {
        this.takeLast(2)
    }
}

@Preview
@Composable
fun NavLineChartPreview() {
    val sampleData = listOf(
        MutualFundGraphPointsDomain(10.0, "2023-01-01T00:00:00Z", "Jan 01: 10.0"),
        MutualFundGraphPointsDomain(12.5, "2023-02-01T00:00:00Z", "Feb 01: 12.5"),
        MutualFundGraphPointsDomain(11.0, "2023-03-01T00:00:00Z", "Mar 01: 11.0"),
        MutualFundGraphPointsDomain(15.2, "2023-04-01T00:00:00Z", "Apr 01: 15.2"),
        MutualFundGraphPointsDomain(14.0, "2023-05-01T00:00:00Z", "May 01: 14.0"),
        MutualFundGraphPointsDomain(18.5, "2023-06-01T00:00:00Z", "Jun 01: 18.5"),
        MutualFundGraphPointsDomain(22.0, "2023-07-01T00:00:00Z", "Jul 01: 22.0"),
        MutualFundGraphPointsDomain(20.0, "2023-08-01T00:00:00Z", "Aug 01: 20.0"),
        MutualFundGraphPointsDomain(25.0, "2023-09-01T00:00:00Z", "Sep 01: 25.0"),
        MutualFundGraphPointsDomain(28.5, "2023-10-01T00:00:00Z", "Oct 01: 28.5"),
        MutualFundGraphPointsDomain(27.0, "2023-11-01T00:00:00Z", "Nov 01: 27.0"),
        MutualFundGraphPointsDomain(32.0, "2023-12-01T00:00:00Z", "Dec 01: 32.0")
    )
    JantaNiveshTheme {
        Box(modifier = Modifier.background(Color.White).padding(16.dp)) {
            NavLineChart(
                data = sampleData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
    }
}
