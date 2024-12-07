package me.anno.aoc23.day9

fun main() {
    val histories = readHistories()
    histories.forEach { it.reverse() } // much too easy...
    val hash = histories.sumOf { predictNextValue(it) }
    // solution: 1104
    println(hash)
}