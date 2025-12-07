package me.anno.aoc25.day7

import me.anno.utils.Utils.readLines

fun main() {
    val lines = readLines(25, 7, "data.txt")
    val s = lines[0].indexOf('S')
    val rays = Array(lines.size) {
        BooleanArray(lines[0].length)
    }
    rays[0][s] = true
    var splits = 0
    for (y in rays.indices) {
        for (x in rays[y].indices) {
            if (lines[y][x] == '^' && rays[y - 1][x]) {
                splits++
                rays[y][x - 1] = true
                rays[y][x + 1] = true
            }
            if (y > 0 && lines[y][x] == '.' && rays[y - 1][x]) {
                rays[y][x] = true
            }
        }
    }
    println("Splits: $splits")
}