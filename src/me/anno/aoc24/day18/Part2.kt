package me.anno.aoc24.day18

import me.anno.utils.Utils.readLines

// no need to optimize this one, it's fast enough as is (slightly less than a second)
fun main() {
    val falls = readLines(24, 18, "data.txt")
        .map { it.split(',').map { p -> p.toInt() } }
    val (sx, sy, numFalls) = falls[0]
    for (numFallsI in numFalls until falls.size - 1) {
        val falls1 = falls.subList(1, numFallsI + 1)
        if (findPathLength(sx, sy, falls1) < offset) {
            println("First blocking coordinates: ${falls1.last().joinToString(",")}")
            break
        }
    }
}