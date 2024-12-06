package me.anno.aoc24.day4

import me.anno.utils.Utils.readLines

fun search(
    lines: List<String>,
    fx: Int, fy: Int,
): Int {
    val searched = "XMAS"
    val sx = lines[0].length
    val sy = lines.size
    var total = 0
    for (xi in 0 until sx) {
        field@ for (yi in 0 until sy) {
            // check that last character is inside field, too
            val ex = xi + fx * searched.lastIndex
            val ey = yi + fy * searched.lastIndex
            if (ex !in 0 until sx || ey !in 0 until sy) {
                continue@field
            }
            // check that each letter is correct
            for (ki in searched.indices) {
                val cx = xi + fx * ki
                val cy = yi + fy * ki
                val letter = lines[cy][cx]
                if (letter != searched[ki]) {
                    continue@field
                }
            }
            total++
        }
    }
    return total
}

/**
 * count XMAS in the riddle:
 *  - reversed, horizontal, vertical, diagonal
 * */
fun main() {
    val lines = readLines(24, 4, "data.txt")
    val sum = search(lines, +1, 0) + search(lines, -1, 0) +
            search(lines, 0, +1) + search(lines, 0, -1) +
            search(lines, +1, +1) + search(lines, -1, +1) +
            search(lines, +1, -1) + search(lines, -1, -1)
    println(sum)
}