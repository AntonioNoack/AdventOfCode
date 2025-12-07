package me.anno.aoc25.day7

import me.anno.utils.Utils.readLines

fun main() {
    val lines = readLines(25, 7, "data.txt")
    val s = lines[0].indexOf('S')
    val rays = Array(lines.size) {
        LongArray(lines[0].length)
    }
    rays[0][s] = 1
    for (y in rays.indices) {
        for (x in rays[y].indices) {
            if (lines[y][x] == '^') {
                val count = rays[y - 1][x]
                rays[y][x - 1] += count
                rays[y][x + 1] += count
            }
            if (y > 0 && lines[y][x] == '.') {
                rays[y][x] += rays[y - 1][x]
            }
        }
    }
    println("Total: ${rays.last().sum()}")
}