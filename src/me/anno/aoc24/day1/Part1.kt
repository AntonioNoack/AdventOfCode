package me.anno.aoc24.day1

import me.anno.aoc24.utils.Utils.readLines
import kotlin.math.abs

class IntPair(val first: Int, val second: Int)

fun numbers(): List<IntPair> {
    return readLines(1, "data.txt")
        .map { line -> line.split(' ').mapNotNull { it.toIntOrNull() } }
        .filter { it.size == 2 }
        .map { IntPair(it[0], it[1]) }
}

fun main() {
    val numbers = numbers()
    val firstNumbers = numbers.map { it.first }.sorted()
    val secondNumbers = numbers.map { it.second }.sorted()
    val totalDifference = numbers.indices.sumOf {
        abs(secondNumbers[it] - firstNumbers[it])
    }
    println(totalDifference)
}