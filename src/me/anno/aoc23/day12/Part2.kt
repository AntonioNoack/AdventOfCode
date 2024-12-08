package me.anno.aoc23.day12

import me.anno.utils.Utils.readLines
import kotlin.math.min

/**
 * todo this algorithm isn't working yet...
 * */
fun main() {
    val rows = readLines(23, 12, "sample.txt")
        .map { parseRow(it) }
        // .map { unfoldRow(it) }
        .map { addWorkingBit(it) }
    val counters = rows.map { countPossiblePatternsV2(it) }
    println(counters)
    println(counters.sum())
}

fun unfoldRow(row: SpringRow): SpringRow {
    return SpringRow(row.pattern.repeat(5), (0 until 5).flatMap { row.damageSequence })
}

fun countPossiblePatternsV2(row: SpringRow): Long {
    val numUnknowns = row.pattern.count { it == unknown }
    val expectedNumBits = row.damageSequence.sum() - row.pattern.count { it == damaged }
    println("Count patterns for $row, $expectedNumBits, $numUnknowns")
    return countPossiblePatternsV2(row, 0, expectedNumBits, numUnknowns, 0)
}

fun Boolean.toInt(n: Int = 1): Int {
    return if (this) n else 0
}

fun countPossiblePatternsV2(
    row: SpringRow,
    patternIndex0: Int,
    remainingBits: Int,
    remainingUnknowns: Int,
    sequenceIndex: Int
): Long {

    if (remainingUnknowns < 0) {
        // impossible case
        return 0
    }

    val (pattern, sequence) = row
    if (patternIndex0 == pattern.length) {
        if (remainingUnknowns != 0) {
            throw IllegalStateException()
        }
        return if (remainingBits > 0) 0 else 1
    }

    var patternIndex = patternIndex0
    while (patternIndex < pattern.length && pattern[patternIndex] == working) {
        patternIndex++
    }

    var startDamaged = 0
    while (patternIndex < pattern.length && pattern[patternIndex] == damaged) {
        startDamaged++
        patternIndex++
    }

    if (startDamaged > 0 && (sequenceIndex >= sequence.size || startDamaged > sequence[sequenceIndex])) {
        // impossible
        return 0
    }

    var numUnknown = 0
    while (patternIndex < pattern.length && pattern[patternIndex] == unknown) {
        numUnknown++
        patternIndex++
    }

    if (numUnknown == 0) {
        // easy, just continue with the next one
        return if ((startDamaged == 0) || (sequenceIndex < sequence.size && startDamaged == sequence[sequenceIndex])) {
            countPossiblePatternsV2( // continue, where we left off
                row, patternIndex, remainingBits, remainingUnknowns,
                sequenceIndex + 1
            )
        } else 0 // impossible
    }

    var endDamaged = 0
    while (patternIndex < pattern.length && pattern[patternIndex] == damaged) {
        endDamaged++
        patternIndex++
    }

    // todo what about the case "??####???" ???

    val availableUnknown = min(numUnknown, remainingBits)
    val availableSum = startDamaged + availableUnknown + endDamaged
    // go through all ways [sequenceIndex, ... sum(sequence@next...) < startDamaged + numUnknown + endDamaged],
    //  and check whether they are generally possible, how many ways there are (recursively), and sum that
    var newSequenceIndex = sequenceIndex + (startDamaged > 0).toInt() + (endDamaged > 0).toInt()
    var sum = 0L
    while (newSequenceIndex <= sequence.size) {
        val requiredSum = (sequenceIndex until newSequenceIndex).sumOf { idx -> sequence[idx] }
        if (requiredSum > availableSum) break

        val numCombinations = numCombinations(
            sequence, sequenceIndex, newSequenceIndex,
            startDamaged, endDamaged, numUnknown, remainingBits
        )

        if (numCombinations > 0) {
            val usedUnknown = availableSum - (startDamaged + endDamaged)
            val numFollowingPossibilities = countPossiblePatternsV2(
                row, patternIndex,
                remainingBits - usedUnknown,
                remainingBits - usedUnknown,
                newSequenceIndex
            )
            if (numFollowingPossibilities > 0) {
                println("sum[$patternIndex0,$remainingBits,$remainingUnknowns][$sequenceIndex,$newSequenceIndex] += $numFollowingPossibilities * $numCombinations")
            }
            sum += numFollowingPossibilities * numCombinations
        }

        newSequenceIndex++
    }

    return sum
}

fun numCombinations(
    sequence: List<Int>, sequenceIndex0: Int, newSequenceIndex0: Int, // [si, nsi[
    startDamaged: Int, endDamaged: Int,
    numUnknown0: Int, remainingBits0: Int,
): Long {

    var sequenceIndex = sequenceIndex0
    var newSequenceIndex = newSequenceIndex0
    var numUnknown = numUnknown0
    var remainingBits = remainingBits0

    if (sequenceIndex == newSequenceIndex) {
        return if (startDamaged > 0 || endDamaged > 0) 0 // impossible
        else 1 // possible
    }

    // fill-all-case
    if (startDamaged + numUnknown + endDamaged == sequence[sequenceIndex] &&
        sequenceIndex + 1 == newSequenceIndex
    ) return 1

    // there is stuff at the start
    if (startDamaged > 0) {
        // check if there is enough space
        val requiredBits = sequence[sequenceIndex] - startDamaged
        if (requiredBits < 0) return 0 // too many entries at start
        if (requiredBits > numUnknown) return 0 // not enough space
        val needsSeparator = sequenceIndex < newSequenceIndex + 1 // more than 1 element
        if (requiredBits >= numUnknown && needsSeparator) return 0 // not enough space for a separator
        if (requiredBits > remainingBits) return 0 // not enough remaining budget

        // startDamaged = 0
        println("Start Damaged: [$numUnknown,$remainingBits,$sequenceIndex] -= $requiredBits/1")
        numUnknown -= requiredBits + 1
        remainingBits -= requiredBits
        sequenceIndex++
    }

    if (endDamaged > 0) {
        val requiredBits = sequence[newSequenceIndex - 1] - endDamaged
        if (requiredBits < 0) return 0 // too many entries at end
        if (requiredBits > numUnknown) return 0 // not enough space
        val needsSeparator = sequenceIndex < newSequenceIndex + 1 // more than 1 element
        if (requiredBits >= numUnknown && needsSeparator) return 0 // not enough space for a separator
        if (requiredBits > remainingBits) return 0 // not enough remaining budget

        // endDamaged = 0
        numUnknown -= requiredBits + 1
        remainingBits -= requiredBits
        newSequenceIndex--
    }

    // the remaining gap is .????.
    val numElements = newSequenceIndex - sequenceIndex
    if (numElements < 0) return 0 // not possible
    if (numElements == 0) return 1 // easy case: nothing will be filled in
    val requiredSpaces = numElements - 1
    val requiredSpace = (sequenceIndex until newSequenceIndex).sumOf { idx -> sequence[idx] } + requiredSpaces
    if (requiredSpace > numUnknown) return 0 // not enough space
    val freeSpace = numUnknown - requiredSpace
    val sum = numPossibilities(numElements + 1, freeSpace)
    println("  numPoss[$numElements+1,$freeSpace] = $sum")
    return sum
}

/**
 * number of ways to distribute #numValues into #numSlots
 * */
fun numPossibilities(numSlots: Int, numValues: Int): Long {
    if (numValues < 0 || numSlots < 0) throw IllegalArgumentException()
    if (numValues == 0) return 1 // nothing required
    if (numSlots == 0) return 0 // more than 0 values, but no slots -> impossible
    if (numSlots == 1) return 1
    if (numSlots == 2) return numValues.toLong()
    var sum = 0L
    for (usedInFirstSlot in 0 until numValues) {
        sum += numPossibilities(numSlots - 1, numValues - usedInFirstSlot)
    }
    return sum
}
