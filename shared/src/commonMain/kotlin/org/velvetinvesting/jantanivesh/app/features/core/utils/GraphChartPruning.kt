package org.velvetinvesting.jantanivesh.app.features.core.utils

import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundGraphPointsDomain


fun List<MutualFundGraphPointsDomain>.pruneForGraph(
    maxBasePoints: Int = 180
): List<MutualFundGraphPointsDomain> {

    if (size <= maxBasePoints) return this

    val result = mutableListOf<MutualFundGraphPointsDomain>()

    var segment = mutableListOf<MutualFundGraphPointsDomain>()
    segment.add(first())

    fun getSlope(a: Double, b: Double): Int {
        return when {
            b > a -> 1
            b < a -> -1
            else -> 0
        }
    }

    var prevSlope = 0

    for (i in 1 until size) {
        val prev = this[i - 1]
        val current = this[i]

        val slope = getSlope(prev.navValue, current.navValue)

        if (prevSlope == 0) {
            prevSlope = slope
            segment.add(current)
            continue
        }

        if (slope == prevSlope || slope == 0) {
            segment.add(current)
        } else {
            // 🔥 slope changed → process segment
            result.addAll(pruneSegment(segment))
            segment = mutableListOf(prev, current)
            prevSlope = slope
        }
    }

    // last segment
    if (segment.isNotEmpty()) {
        result.addAll(pruneSegment(segment))
    }

    return result
}

private fun pruneSegment(
    segment: List<MutualFundGraphPointsDomain>
): List<MutualFundGraphPointsDomain> {

    if (segment.size <= 2) return segment

    val result = mutableListOf<MutualFundGraphPointsDomain>()

    val size = segment.size

    val step = when {
        size > 100 -> 5
        size > 50 -> 3
        size > 20 -> 2
        else -> 1
    }

    result.add(segment.first())

    for (i in 1 until size - 1 step step) {
        result.add(segment[i])
    }

    result.add(segment.last())

    return result
}