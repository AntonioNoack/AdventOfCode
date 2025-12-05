package me.anno.aoc25.day5

import me.anno.utils.Utils.readLines
import me.anno.utils.Utils.split

fun main() {
    val lines = readLines(25, 5, "data.txt")
        .split("")
    check(lines.size == 2)
    val ranges = lines[0].map {
        val (min, max) = it.split('-')
        min.toLong()..max.toLong()
    }
    val available = lines[1].map { it.toLong() }
    val fresh = available.count { ranges.any { range -> it in range }}
    println("#fresh: $fresh")
}