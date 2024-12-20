package me.anno.aoc22.day3

import me.anno.utils.Utils.readLines

fun getPriority(type: Char): Int {
    return when (type) {
        in 'a'..'z' -> (type - 'a' + 1)
        in 'A'..'Z' -> (type - 'A' + 27)
        else -> throw IllegalStateException()
    }
}

val given = BooleanArray(52)
fun findDuplicateItem(line: String): Int {
    var ri = 0
    while (ri + ri < line.length) {
        val pri = getPriority(line[ri])
        given[pri - 1] = true
        ri++
    }
    while (ri < line.length) {
        val pri = getPriority(line[ri])
        if (given[pri - 1]) {
            given.fill(false)
            return pri
        }
        ri++
    }
    throw IllegalStateException()
}

fun main() {
    val duplicates = readLines(22, 3, "data.txt")
        .map { findDuplicateItem(it) }
    println(duplicates.sum())
    // solution: 8243
}