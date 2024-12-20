package me.anno.aoc22.day6

import me.anno.utils.Utils.readLines

fun main() {
    val lines = readLines(22, 6, "data.txt")
    for (i in lines.indices step 2) {
        val line = lines[i]
        val result = indexOfFirstFourUniqueLetters(line, 14)
        println(result)
    }
    // solution: 2421
}