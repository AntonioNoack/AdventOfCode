package me.anno.aoc23.day1

import me.anno.utils.Utils.readLines

val numbers = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)

fun firstLetter(line: String): Char {
    for (i in line.indices) {
        if (line[i] in '0'..'9') {
            return line[i]
        }
        for ((key, value) in numbers) {
            if (line.startsWith(key, i)) {
                return value[0]
            }
        }
    }
    throw IllegalStateException("Missing first letter")
}

fun lastLetter(line: String): Char {
    for (i in line.indices.reversed()) {
        if (line[i] in '0'..'9') {
            return line[i]
        }
        for ((key, value) in numbers) {
            if (line.startsWith(key, i)) {
                return value[0]
            }
        }
    }
    throw IllegalStateException("Missing last letter")
}

fun sumFirstLast2(name: String): Int {
    return readLines(23, 1, name)
        .sumOf { line ->
            val firstLetter = firstLetter(line)
            val lastLetter = lastLetter(line)
            "$firstLetter$lastLetter".toInt()
        }
}

fun main() {
    assert(281 == sumFirstLast2("sample2.txt"))
    println(sumFirstLast2("data.txt"))
    // 54591??? correct
    // 54553??? not it apparently...
}