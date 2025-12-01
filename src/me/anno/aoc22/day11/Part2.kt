package me.anno.aoc22.day11

import me.anno.utils.Utils.readLines

fun main() {
    val lines = readLines(22, 11, "data.txt")
    runMonkeyBusiness(lines, false, 10_000)
}
