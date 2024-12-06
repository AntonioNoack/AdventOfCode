package me.anno.aoc23.day3

import me.anno.utils.Utils.readLines

fun isSymbol(c: Char): Boolean {
    return c != '.' && c !in '0'..'9'
}

fun getPaddedLines(year: Int, day: Int, name: String): List<String> {
    val lines0 = readLines(year, day, name)
    val topBottom = ".".repeat(lines0[0].length + 2)
    // add padding to make testing for border easier
    return listOf(topBottom) + lines0.map { line -> ".$line." } + listOf(topBottom)
}

fun main() {
    val lines = getPaddedLines(23, 3, "data.txt")
    var sum = 0
    for (y in 1 until lines.size - 1) {
        var x = 1
        val line = lines[y]
        while (x < line.length) {
            if (line[x] in '0'..'9') {
                // collect number
                val x0 = x
                var number = line[x++] - '0'
                while (line[x] in '0'..'9') {
                    number = 10 * number + (line[x++] - '0')
                }
                // check if symbol is adjacent
                // range: x0 until x
                var hasSymbol = false
                symbolSearch@ for (yi in y - 1..y + 1) {
                    for (xi in x0 - 1..x) {
                        if (isSymbol(lines[yi][xi])) {
                            hasSymbol = true
                            break@symbolSearch
                        }
                    }
                }
                if (hasSymbol) {
                    sum += number
                }
            } else x++
        }
    }
    println(sum)
}