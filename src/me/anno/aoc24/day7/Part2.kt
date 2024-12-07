package me.anno.aoc24.day7

import me.anno.utils.Utils.readLines
import kotlin.math.pow

fun maskSolvesTask2(task: Task, mask0: Int): Boolean {
    val parts = task.parts
    if (parts.isEmpty()) return task.result == 0L
    var result = parts[0]
    var mask = mask0
    for (i in 1 until parts.size) {
        val part = parts[i]
        if (part <= 0) throw IllegalArgumentException("Expected >= 1")
        when (mask % 3) {
            0 -> result += part
            1 -> result *= part
            2 -> result = "$result$part".toLong()
        }
        mask /= 3
        // are we guaranteed that there are no 0s???
        if (result > task.result) {
            return false
        }
    }
    return result == task.result
}

fun canSolveTask2(task: Task): Boolean {
    val numFlags = task.parts.size - 1
    val numTests = 3.0.pow(numFlags).toInt()
    for (i in 0 until numTests) {
        if (maskSolvesTask2(task, i)) {
            return true
        }
    }
    return false
}

fun main() {
    val tasks = readLines(24, 7, "data.txt")
        .mapNotNull { parseTask(it) }
        .filter { canSolveTask2(it) }
        .sumOf { it.result }
    // solution: 110365987435001
    println("solvable: $tasks")
}