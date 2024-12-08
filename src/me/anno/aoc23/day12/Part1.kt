package me.anno.aoc23.day12

import me.anno.aoc24.day7.hasFlag
import me.anno.utils.Utils.readLines

val working = '.'
val damaged = '#'
val unknown = '?'

data class SpringRow(val pattern: String, val damageSequence: List<Int>)

fun main() {
    val rows = readLines(23, 12, "sample.txt")
        .map { parseRow(it) }.map { addWorkingBit(it) }
    println(rows)
    val counters = rows.map { countPossiblePatterns(it) }
    println(counters)
    println(counters.sum())
}

fun parseRow(line: String): SpringRow {
    val sp = line.indexOf(' ')
    val pattern = line.substring(0, sp)
    val sequence = line.substring(sp + 1).split(',')
        .map { it.toInt() }
    return SpringRow(pattern, sequence)
}

fun addWorkingBit(row: SpringRow): SpringRow {
    // multiple dots in a row are irrelevant, so remove them
    var pattern = row.pattern
    if (!pattern.endsWith(working)) {
        pattern += working
    }
    while (true) {
        val length0 = pattern.length
        pattern = pattern.replace("..", ".")
        if (length0 == pattern.length) break
    }
    return SpringRow(pattern, row.damageSequence)
}

fun countPossiblePatterns(row: SpringRow): Int {
    var sum = 0
    val numUnknowns = row.pattern.count { it == unknown }
    val expectedNumBits = row.damageSequence.sum() - row.pattern.count { it == damaged }
    // all patterns with incorrect count of bits can be discarded
    // -> we could iterate over permutations...
    if (numUnknowns > 25) throw IllegalStateException("Too many unknowns, $numUnknowns -> $expectedNumBits")
    // println("Checking $numUnknowns -> $expectedNumBits")
    for (mask in 0 until (1 shl numUnknowns)) {
        if (mask.countOneBits() != expectedNumBits) {
            continue
        }
        val expected = matchesSequence(row, mask)
        /*val actual = getSequence(row, mask) == row.damageSequence
        if(expected != actual) {
            throw IllegalStateException("Incorrect answer, $row, $mask -> $expected, ${getSequence(row,mask)}")
        }*/
        if (expected) {
            sum++
        }
    }
    return sum
}

fun matchesSequence(row: SpringRow, mask0: Int): Boolean {
    val (pattern, sequence) = row
    var mask = mask0
    var si = 0
    var numDamaged = 0
    for (i in pattern.indices) {
        var pi = pattern[i]
        if (pi == unknown) {
            pi = if (mask.hasFlag(1)) damaged else working
            mask = mask ushr 1
        }
        when (pi) {
            working -> {
                if (numDamaged > 0) {
                    if (sequence[si] != numDamaged) {
                        // not the expected sequence
                        return false
                    }
                    si++
                    numDamaged = 0
                }
            }
            damaged -> {
                if (si >= sequence.size) {
                    // to many sequences of broken springs
                    return false
                }
                numDamaged++
            }
        }
    }
    return si == sequence.size
}

fun getSequence(row: SpringRow, mask0: Int): List<Int> {
    val pattern = row.pattern
    val result = ArrayList<Int>()
    var mask = mask0
    var numDamaged = 0
    for (i in pattern.indices) {
        var pi = pattern[i]
        if (pi == unknown) {
            pi = if (mask.hasFlag(1)) damaged else working
            mask = mask ushr 1
        }
        when (pi) {
            working -> {
                if (numDamaged > 0) {
                    result.add(numDamaged)
                    numDamaged = 0
                }
            }
            damaged -> numDamaged++
        }
    }
    return result
}
