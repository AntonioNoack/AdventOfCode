package me.anno.aoc25.day1

import me.anno.utils.Utils.readLines

fun main() {
    val sequence = readLines(25, 1, "data.txt")
    var zeroCtr = 0
    var position = 50
    val period = 100
    for (line in sequence) {
        val sign = when (line[0]) {
            'L' -> -1
            'R' -> +1
            else -> throw IllegalStateException()
        }

        val delta = line.substring(1).toInt()
        if (delta <= 0) throw IllegalStateException()

        repeat(delta) {
            position += sign
            if (position == -period || position == 0 || position == period) {
                zeroCtr++
                position = 0
            }
        }
    }
    println("Count: $zeroCtr")
}