package me.anno.aoc25.day3

import me.anno.utils.Utils.readLines

fun findMaxVoltage12(line: String): Long {
    // find 12 digits such that the score is maximal
    var pos = 0
    val len = 12
    val number = StringBuilder(12)
    for (i in 0 until len) {
        val limit = line.length + i - len + 1
        val bestChar = line.substring(pos, limit).max()
        pos = line.indexOf(bestChar, pos) + 1
        number.append(bestChar)
    }
    return number.toString().toLong()
}

fun main() {
    val lines = readLines(25, 3, "data.txt")
        .map { findMaxVoltage12(it) }
    println("$lines -> ${lines.sum()}")
}