package me.anno.aoc22.day3

import me.anno.utils.Utils.readLines

val counts = IntArray(52)
val wasAdded = BooleanArray(52)
fun findCommonLetterPriority(lines: List<String>): Int {
    for (line in lines) {
        wasAdded.fill(false)
        for (c in line) {
            val pri = getPriority(c)
            val idx = pri - 1
            if (wasAdded[idx]) continue // skip duplicates in a single line
            wasAdded[idx] = true
            if (++counts[idx] == 3) {
                counts.fill(0)
                return pri
            }
        }
    }
    throw IllegalStateException()
}

fun main() {
    val lines = readLines(22, 3, "data.txt")
    var sum = 0
    for (li in lines.indices step 3) {
        sum += findCommonLetterPriority(lines.subList(li, li + 3))
    }
    println(sum)
    // solution: 2631
}