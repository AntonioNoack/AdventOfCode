package me.anno.aoc23.day8

import me.anno.utils.Utils.readLines

fun fillIntPaths(
    sortedNodes: List<String>,
    paths: Map<String, Pair<String, String>>,
    nodeToIndex: Map<String, Int>
): IntArray {
    val intPaths = IntArray(sortedNodes.size * 2)
    for (i in sortedNodes.indices) {
        val path = paths[sortedNodes[i]]!!
        intPaths[i * 2] = nodeToIndex[path.first]!!
        intPaths[i * 2 + 1] = nodeToIndex[path.second]!!
    }
    return intPaths
}

fun initialPositions(nodeToIndex: Map<String, Int>): IntArray {
    return nodeToIndex
        .filter { isStartNode(it.key) }
        .map { it.value }.toIntArray()
}

/**
 * make everything integer based for speed
 *
 * still too slow; ~ 229 steps/µs (14x faster)
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

    var numSteps = 0L
    val positions = initialPositions(nodeToIndex)
    val intPaths = fillIntPaths(sortedNodes, paths, nodeToIndex)
    val t0 = System.nanoTime()
    while (true) {
        var good = true
        val isRight = rlSequence[((numSteps++) % rlSequence.size).toInt()]
        for (i in positions.indices) {
            var position = positions[i]
            position = intPaths[position * 2 + isRight]
            positions[i] = position
            good = good and (position < numEndNodes)
        }
        if (numSteps % 100000000L == 0L) {
            val dt = System.nanoTime() - t0
            println("$numSteps, ${(numSteps * 1000 / dt)} steps/µs")
        }
        if (good) {
            break
        }
    }
    println(numSteps)
}