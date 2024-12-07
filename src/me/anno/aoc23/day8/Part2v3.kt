package me.anno.aoc23.day8

import me.anno.utils.Utils.readLines
import kotlin.math.max

fun countStepsUntilZ(
    numSteps0: Long,
    rlSequence: IntArray, intPaths: IntArray,
    numEndNodes: Int,
    dstArray: IntArray, dstI: Int
): Long {
    var numSteps = 1L // one step is taken after first check
    var position = dstArray[dstI]
    var rlSequenceIndex = (numSteps0 % rlSequence.size).toInt()
    while (true) {
        val isRight = rlSequence[rlSequenceIndex++]
        position = intPaths[position * 2 + isRight]
        if (position < numEndNodes) {
            dstArray[dstI] = position
            return numSteps
        }
        if (rlSequenceIndex == rlSequence.size) {
            rlSequenceIndex = 0
        }
        numSteps++
    }
}

/**
 * calculate how many steps are needed to the next Z
 *
 * -> 382 steps/ns (1650x faster)
 * */
fun main() {

    val lines = readLines(23, 8, "data.txt")
    val rlSequence = lines[0].filter { it in "LR" } // filter just in case
        .map { if (it == 'R') 1 else 0 }.toIntArray()

    val paths = lines.subList(2, lines.size)
        .associate { parsePath(it) }
    val sortedNodes = paths.keys
        .sortedBy { if (isEndNode(it)) 0 else 1 } // end nodes first for fast end-checks
    val nodeToIndex = sortedNodes.withIndex()
        .associate { it.value to it.index }
    val numEndNodes = sortedNodes
        .indexOfFirst { !isEndNode(it) }

    val positions = initialPositions(nodeToIndex)
    val intPaths = fillIntPaths(sortedNodes, paths, nodeToIndex)

    println("initial: ${positions.toList()}")

    val numStepsOnZ = LongArray(positions.size)
    // fill this with A->Z
    for (i in positions.indices) {
        numStepsOnZ[i] = countStepsUntilZ(0L, rlSequence, intPaths, numEndNodes, positions, i)
    }

    println(numStepsOnZ.toList())
    val numStepsMap = LongArray(numEndNodes * rlSequence.size) // Z,rlSequenceIndex -> Z
    val t0 = System.nanoTime()
    var lastSteps = 0L
    var maxSteps = numStepsOnZ.max()
    while (true) {
        val minSteps = numStepsOnZ.min()
        if (minSteps == maxSteps) {
            val dt = System.nanoTime() - t0
            // Solution: 9858474970153, total: 25068ms
            println("Solution: $minSteps, total: ${dt / 1_000_000}ms")
            break
        }
        if (minSteps - 100000000000L > lastSteps) {
            val dt = System.nanoTime() - t0
            println("$minSteps, ${(minSteps / dt)} steps/ns")
            lastSteps = minSteps
        }
        val minIndex = numStepsOnZ.indexOf(minSteps)
        // cache this value, it's cacheable
        assert(positions[minIndex] < numEndNodes)
        val cacheIndex = positions[minIndex] * rlSequence.size + (minSteps % rlSequence.size).toInt()
        var numSteps = numStepsMap[cacheIndex]
        if (numSteps == 0L) {
            numSteps = countStepsUntilZ(minSteps, rlSequence, intPaths, numEndNodes, positions, minIndex)
            numStepsMap[cacheIndex] = numSteps
        }
        val nextSteps = minSteps + numSteps
        maxSteps = max(maxSteps, nextSteps)
        numStepsOnZ[minIndex] = nextSteps
    }
}