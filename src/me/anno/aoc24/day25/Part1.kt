package me.anno.aoc24.day25

import me.anno.utils.Utils.readLines
import me.anno.utils.Utils.split

data class Pins(val size: Int, val isKey: Boolean, val heights: List<Int>)

fun main() {
    val patterns = readLines(24, 25, "data.txt")
        .split("")
        .map { lines ->
            val isKey = lines[0][0] == '.'
            val heights = lines[0].indices.map { x ->
                lines.count { line -> line[x] == '#' } - 1
            }
            Pins(lines.size, isKey, heights)
        }
    if (patterns.groupBy { it.size }.size > 1) throw IllegalStateException("Multiple sizes")
    val keys = patterns.filter { it.isKey }
    val locks = patterns.filter { !it.isKey }
    val total = locks.sumOf { lock ->
        keys.count { key -> fitsTogether(key, lock) }
    }
    // println(patterns.joinToString("\n"))
    println(total)
    // solution: 3338
}

fun fitsTogether(key: Pins, lock: Pins): Boolean {
    return key.size == lock.size &&
            key.heights.indices.all { xi ->
                key.heights[xi] + lock.heights[xi] + 1 < key.size
            }
}