package me.anno.aoc25.day2

import me.anno.utils.Utils.readLines
import kotlin.math.max

fun generate2x(id: Long, base: Long): Long {
    return id * (base + 1)
}

fun nextInvalidId2x(i: Long, base: Long): Long {
    val id = max(i / base, 1L)
    val target = generate2x(id, base)
    if (i <= target) return target
    return if (id + 1 < base) {
        generate2x(id + 1, base)
    } else {
        nextInvalidId2x(i * 10)
    }
}

fun nextInvalidId2x(i: Long): Long {
    return when {
        i <= 11 -> 1_1
        i < 100 -> nextInvalidId2x(i, 10)
        i <= 10_10 -> 10_10
        i < 10_00 * 10 -> nextInvalidId2x(i, 100)
        i <= 100_100 -> 100_100
        i < 100_000 * 10 -> nextInvalidId2x(i, 1000)
        i <= 1000_1000 -> 1000_1000
        i < 1000_0000 * 10 -> nextInvalidId2x(i, 10_000)
        i <= 10000_10000 -> 10000_10000
        i < 10000_00000 * 10 -> nextInvalidId2x(i, 100_000)
        i <= 100000_100000 -> 100000_100000
        i < 100000_000000 * 10 -> nextInvalidId2x(i, 1000_000)
        i <= 1000000_1000000 -> 1000000_1000000
        i < 1000000_0000000 * 10 -> nextInvalidId2x(i, 10_000_000)
        i <= 10000000_10000000 -> 10000000_10000000
        i < 10000000_00000000 * 10 -> nextInvalidId2x(i, 100_000_000)
        i <= 100000000_100000000 -> 100000000_100000000
        i < 100000000_000000000 * 10 -> nextInvalidId2x(i, 1000_000_000)
        // i <= 1000000000_1000000000 -> 1000000000_1000000000
        else -> throw IllegalArgumentException("Not implemented: $i")
    }
}

fun sumInvalidIds2x(range: LongRange): Long {
    var i = range.first
    var sum = 0L
    while (true) {
        val j = nextInvalidId2x(i)
        if (j > range.last) {
            // println("// $range[$i] -> $j")
            return sum
        }
        // println("$range[$i] -> $j")
        sum += j
        i = j + 1
    }
}

fun main() {
    val lines = readLines(25, 2, "data.txt").joinToString("")
    val ranges = lines
        .split(',')
        .map {
            val (a, b) = it.split('-')
            a.toLong()..b.toLong()
        }
    val sum = ranges.sumOf { sumInvalidIds2x(it) }
    println("Sum: $sum") // 54641809925
}