package me.anno.aoc24.day4

import me.anno.utils.Utils.readLines

/**
 * find all cross-MAS
 * */
fun main() {
    val lines = readLines(24, 4, "data.txt")
    val sx = lines[0].length
    val sy = lines.size
    var sum = 0
    for (yi in 0 until sy - 2) {
        for (xi in 0 until sx - 2) {
            // check that center is A
            if (lines[yi + 1][xi + 1] != 'A') continue
            // check that diagonals aren't identical
            if (lines[yi][xi] == lines[yi + 2][xi + 2]) continue
            if (lines[yi + 2][xi] == lines[yi][xi + 2]) continue
            // check that all corners are M or S
            if (lines[yi][xi] !in "MS") continue
            if (lines[yi][xi + 2] !in "MS") continue
            if (lines[yi + 2][xi] !in "MS") continue
            if (lines[yi + 2][xi + 2] !in "MS") continue
            sum++
        }
    }
    println(sum)
}