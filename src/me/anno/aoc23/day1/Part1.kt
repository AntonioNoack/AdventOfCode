package me.anno.aoc23.day1

import me.anno.utils.Utils.readLines

fun sumFirstLast(name: String): Int {
    return readLines(23, 1, name)
        .map { line -> line.filter { it in '0'..'9' } }
        .sumOf { "${it.first()}${it.last()}".toInt() }
}

fun main() {
    assert(142 == sumFirstLast("sample.txt"))
    println(sumFirstLast("data.txt"))
}