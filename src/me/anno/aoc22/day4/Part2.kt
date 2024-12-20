package me.anno.aoc22.day4

import me.anno.utils.Utils.readLines
import kotlin.math.max
import kotlin.math.min

fun containsALittle(a: IntRange, b: IntRange): Boolean {
    val min = max(a.first, b.first)
    val max = min(a.last, b.last)
    return min <= max
}

fun main() {
    val pairs = readLines(22, 4, "data.txt")
        .map { parseAssignmentPair(it) }
    println(pairs)
    val partiallyContainsOther = pairs.count { (a, b) ->
        containsALittle(a, b) || containsALittle(b, a)
    }
    println(partiallyContainsOther)
    // solution: 845
}