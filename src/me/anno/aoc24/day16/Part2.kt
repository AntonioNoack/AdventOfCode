package me.anno.aoc24.day16

import me.anno.utils.Utils.readLines

fun main() {
    val field = readLines(24, 16, "data.txt")
    findBestPath(field, true)
    // 585 is too low :(, why do we only find a single path???
    // solution: 622
}