package me.anno.aoc22.day4

import me.anno.utils.Utils.readLines

fun parseSection(line: String): IntRange {
    val i = line.indexOf('-')
    val start = line.substring(0, i).toInt()
    val end = line.substring(i + 1).toInt()
    return start..end
}

fun parseAssignmentPair(line: String): List<IntRange> {
    return line.split(',').map { parseSection(it) }
        .sortedBy { it.first }
}

fun containsCompletely(a: IntRange, b: IntRange): Boolean {
    return a.first >= b.first && a.last <= b.last
}

fun main() {
    val pairs = readLines(22, 4, "data.txt")
        .map { parseAssignmentPair(it) }
    println(pairs)
    val fullyContainsOther = pairs.count { (a, b) ->
        containsCompletely(a, b) || containsCompletely(b, a)
    }
    println(fullyContainsOther)
    // solution: 536
}