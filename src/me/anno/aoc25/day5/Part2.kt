package me.anno.aoc25.day5

import me.anno.utils.Utils.readLines
import me.anno.utils.Utils.split
import kotlin.math.max
import kotlin.math.min

fun mergeRanges(a: LongRange, b: LongRange): LongRange {
    val min = min(a.first, b.first)
    val max = max(a.last, b.last)
    return min..max
}

fun overlaps(a: LongRange, b: LongRange): Boolean {
    return a.first <= b.last && b.first <= a.last
}

fun main() {
    val lines = readLines(25, 5, "data.txt")
        .split("")
    check(lines.size == 2)
    val ranges = lines[0].map {
        val (min, max) = it.split('-')
        min.toLong()..max.toLong()
    }.toMutableList()
    ranges.sortBy { it.first() }
    // check overlaps method
    for (i in ranges.indices) {
        check(overlaps(ranges[i], ranges[i]))
    }
    // merge ranges
    for (i in ranges.indices) {
        var j = i + 1
        while (j < ranges.size) {
            if (overlaps(ranges[i], ranges[j])) {
                ranges[i] = mergeRanges(ranges[i], ranges[j])
                ranges.removeAt(j)
            } else j++
        }
    }
    // validate
    for (i in ranges.indices) {
        for (j in ranges.indices) {
            check(overlaps(ranges[i], ranges[j]) == (i == j))
        }
    }
    val numFresh = ranges.sumOf {
        it.last - it.first + 1
    }
    println("#fresh: $numFresh")
}