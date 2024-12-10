package me.anno.aoc23.day13

import me.anno.aoc23.day11.transpose
import me.anno.utils.Utils.readLines

fun main() {
    val lines = readLines(23, 13, "data.txt")
    val field = ArrayList<String>()
    var sum = 0
    for (line in lines) {
        if (line.isBlank()) {
            sum += sumReflections2(field)
            field.clear()
        } else {
            field.add(line)
        }
    }
    sum += sumReflections2(field)
    println(sum)
}

fun sumReflections2(field: List<String>): Int {
    if (field.isEmpty()) return 0
    return sumReflectionsVertical2(field) * 100 +
            sumReflectionsVertical2(transpose(field))
}

fun sumReflectionsVertical2(field: List<String>): Int {
    return field.indices
        .filter { y -> isReflectionLine2(field, y) }
        .sum()
}

fun isReflectionLine2(field: List<String>, y: Int): Boolean {
    var errors = 0
    for (di in 0 until Int.MAX_VALUE) {
        val left = field.getOrNull(y - (di + 1)) ?: break
        val right = field.getOrNull(y + di) ?: break
        errors += numErrors(left, right)
        if (errors > 1) return false // optimization
    }
    return errors == 1
}

fun numErrors(left: String, right: String): Int {
    if (left == right) return 0
    return left.indices.count { idx -> left[idx] != right[idx] }
}
