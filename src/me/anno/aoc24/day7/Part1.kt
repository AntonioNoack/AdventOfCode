package me.anno.aoc24.day7

import me.anno.utils.Utils.readLines

class Task(val result: Long, val parts: List<Long>)

fun parseTask(line: String): Task? {
    val colonIndex = line.indexOf(':')
    if (colonIndex < 0) return null
    val result = line.substring(0, colonIndex).toLong()
    val parts = line.substring(colonIndex + 2)
        .split(' ').map { it.toLong() }
    return Task(result, parts)
}

fun Int.hasFlag(flag: Int): Boolean {
    return this.and(flag) == flag
}

fun maskSolvesTask(task: Task, mask: Int): Boolean {
    val parts = task.parts
    if (parts.isEmpty()) return task.result == 0L
    var result = parts[0]
    for (i in 1 until parts.size) {
        val part = parts[i]
        if (part <= 0) throw IllegalArgumentException("Expected >= 1")
        val isMultiply = mask.hasFlag(1 shl (i - 1))
        if (isMultiply) {
            result *= part
        } else {
            result += part
        }
        // are we guaranteed that there are no 0s???
        if (result > task.result) {
            return false
        }
    }
    return result == task.result
}

fun canSolveTask(task: Task): Boolean {
    val numFlags = task.parts.size - 1
    val numTests = 1 shl numFlags
    for (i in 0 until numTests) {
        if (maskSolvesTask(task, i)) {
            return true
        }
    }
    return false
}

fun main() {
    val tasks = readLines(24, 7, "data.txt")
        .mapNotNull { parseTask(it) }
        .filter { canSolveTask(it) }
        .sumOf { it.result }
    // 434 -> just counted 😅
    // 663613490587 -> correct 😊
    println("solvable: $tasks")
}