package me.anno.aoc25.day6

import me.anno.utils.Utils.readLines

fun String.splitBySpaces(): List<String> {
    return split(' ').filter { it.isNotEmpty() }
}

fun main() {
    val lines = readLines(25, 6, "data.txt")
    val table = lines.map { it.splitBySpaces() }
    val sy = table.size - 1
    val sx = table[0].size
    val solutions = List(sx) { x ->
        val operation = table.last()[x]
        val numbers = List(sy) { y -> table[y][x].toLong() }
        when (operation) {
            "+" -> numbers.reduce { a, b -> Math.addExact(a, b) }
            "*" -> numbers.reduce { a, b -> Math.multiplyExact(a, b) }
            else -> throw NotImplementedError(operation)
        }
    }
    println("Sol: $solutions -> ${solutions.reduce { a, b -> Math.addExact(a, b) }}")
}