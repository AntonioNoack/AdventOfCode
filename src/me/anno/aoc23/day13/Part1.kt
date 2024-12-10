package me.anno.aoc23.day13

import me.anno.aoc23.day11.transpose
import me.anno.utils.Utils.readLines

fun main() {
    val lines = readLines(23, 13, "data.txt")
    val field = ArrayList<String>()
    var sum = 0
    for (line in lines) {
        if (line.isBlank()) {
            sum += sumReflections(field)
            field.clear()
        } else {
            field.add(line)
        }
    }
    sum += sumReflections(field)
    // 25474 -> too low
    // 34772
    println(sum)
}

fun sumReflections(field: List<String>): Int {
    if (field.isEmpty()) return 0
    return sumReflectionsVertical(field) * 100 +
            sumReflectionsVertical(transpose(field))
}

fun sumReflectionsVertical(field: List<String>): Int {
    return field.indices
        .filter { y -> isReflectionLine(field, y) }
        .sum()
}

fun isReflectionLine(field: List<String>, y: Int): Boolean {
    for (di in 0 until Int.MAX_VALUE) {
        val left = field.getOrNull(y - (di + 1)) ?: break
        val right = field.getOrNull(y + di) ?: break
        if (left != right) return false
    }
    return true
}
