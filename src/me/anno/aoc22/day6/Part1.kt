package me.anno.aoc22.day6

import me.anno.utils.Utils.readLines

fun main() {
    val lines = readLines(22, 6, "data.txt")
    for (i in lines.indices step 2) {
        val line = lines[i]
        val result = indexOfFirstFourUniqueLetters(line, 4)
        println(result)
        val control = lines.getOrNull(i + 1)?.toInt()
        if (control != null && control != result) {
            throw IllegalStateException("Expected $control, but got $result")
        }
    }
    // solution: 1100
}

fun indexOfFirstFourUniqueLetters(line: String, n: Int): Int {
    return line.indices.first { i ->
        val subList = line.substring(i, i + n)
        subList.toHashSet().size == n
    } + n
}