package me.anno.aoc25.day3

import me.anno.utils.Utils.readLines
import kotlin.math.max

fun findMaxVoltage(line: String): Int {
    var best = 0
    for (i in 1 until line.length) {
        for (j in 0 until i) {
            val score = line[j].code * 256 + line[i].code
            best = max(score, best)
        }
    }
    val li = best / 256
    val lj = best % 256
    check(li in '0'.code..'9'.code)
    check(lj in '0'.code..'9'.code)
    return (li - '0'.code) * 10 + (lj - '0'.code)
}

fun main() {
    val lines = readLines(25, 3, "data.txt")
        .map { findMaxVoltage(it) }
    println("$lines -> ${lines.sum()}")
}