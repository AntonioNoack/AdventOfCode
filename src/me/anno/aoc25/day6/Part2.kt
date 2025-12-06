package me.anno.aoc25.day6

import me.anno.utils.Utils.readLines

fun main() {
    val table = readLines(25, 6, "data.txt")
    val sy = table.size - 1
    val sx = table.maxOf { it.length } // not all lines have the same length
    val operators = table.last()
    var sum = 0L
    var i1 = 0
    val numbers = ArrayList<Long>()
    while (i1 < operators.length) {
        val operation = operators[i1]
        check(operation in "+*")
        val i0 = i1++
        while (i1 < operators.length && operators[i1] == ' ') i1++
        if (i1 == operators.length) i1 = sx

        for (x in i0 until i1) {
            val v = String(CharArray(sy) { y ->
                table[y].getOrNull(x) ?: ' '
            })
            if (v.isBlank()) break // blank column -> break from loop
            numbers.add(v.trim().toLong())
        }

        sum += when (operation) {
            '+' -> numbers.reduce { a, b -> Math.addExact(a, b) }
            '*' -> numbers.reduce { a, b -> Math.multiplyExact(a, b) }
            else -> throw NotImplementedError("$operation")
        }
        numbers.clear()
    }

    println("Sum: $sum")
}