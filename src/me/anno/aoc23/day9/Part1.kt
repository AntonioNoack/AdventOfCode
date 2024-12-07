package me.anno.aoc23.day9

import me.anno.utils.Utils.readLines

fun predictNextValue(numbers: IntArray): Int {
    if (numbers.all { it == 0 }) {
        return 0
    } else {
        val diffs = IntArray(numbers.size - 1) {
            numbers[it + 1] - numbers[it]
        }
        return numbers.last() + predictNextValue(diffs)
    }
}

fun readHistories(): List<IntArray> {
    return readLines(23, 9, "data.txt")
        .map { line -> line.split(' ').map { it.toInt() }.toIntArray() }
}

fun main() {
    val histories = readHistories()
    // predict the next value of each history...
        .sumOf { predictNextValue(it) }
    // solution: 1798691765
    println(histories)
}