package me.anno.aoc25.day2

import me.anno.utils.Utils.readLines
import kotlin.math.min

fun nextInvalidIdNx(i: Long, values: LongArray): Long {
    if (i > values.last()) throw IllegalStateException()
    val idx = values.binarySearch(i)
    if (idx < 0) {
        val idx = -idx - 1
        if (idx > 0) {
            check(i > values[idx - 1])
        }
        return values[idx]
    }
    return i
}

fun Long.pow(n: Int): Long {
    var r = 1L
    repeat(n) {
        r = Math.multiplyExact(r, this)
    }
    return r
}

fun multiplier(base: Int, repeats: Int): Long {
    val base = base.toLong()
    var result = 0L
    repeat(repeats) { n ->
        result += base.pow(n)
    }
    println("$base x $repeats -> $result")
    return result
}

fun generate(numRepeats: Int): LongArray {
    val powers = Long.MAX_VALUE.toString().length / numRepeats
    val total = 10L.pow(powers) - 1
    var base = 10
    var multiplier = multiplier(base, numRepeats)
    return LongArray(total.toInt()) { i ->
        val id = i + 1
        if (id == base) {
            base *= 10
            multiplier = multiplier(base, numRepeats)
        }
        id * multiplier
    }
}

val n3x = generate(3)
fun nextInvalidId3x(i: Long): Long {
    return nextInvalidIdNx(i, n3x)
}

val n5x = generate(5)
fun nextInvalidId5x(i: Long): Long {
    return nextInvalidIdNx(i, n5x)
}

val n7x = generate(7)
fun nextInvalidId7x(i: Long): Long {
    return nextInvalidIdNx(i, n7x)
}

fun sumInvalidIdsNx(range: LongRange): Long {
    var i = range.first
    var sum = 0L
    check(range.last.toString().length < 11) // else we need an 11-times check
    while (true) {
        val j2 = nextInvalidId2x(i)
        val j3 = nextInvalidId3x(i)
        val j5 = nextInvalidId5x(i)
        val j7 = nextInvalidId7x(i)
        val j = min(min(j2, j3), min(j5, j7))
        if (j > range.last) {
            println("// $range[$i] -> $j")
            return sum
        }
        println("$range[$i] -> $j")
        sum += j
        i = j + 1
    }
}

fun main() {
    // println("3x: ${n3x.toList()}")
    // println("5x: ${n5x.toList()}")
    println("7x: ${n7x.toList()}")
    val lines = readLines(25, 2, "data.txt").joinToString("")
    val ranges = lines
        .split(',')
        .map {
            val (a, b) = it.split('-')
            a.toLong()..b.toLong()
        }
    val sum = ranges.sumOf { sumInvalidIdsNx(it) }
    println("Sum: $sum")
}