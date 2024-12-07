package me.anno.aoc23.day8

import me.anno.utils.Utils.readLines

fun isStartNode(node: String): Boolean {
    return node.endsWith('A')
}

fun isEndNode(node: String): Boolean {
    return node.endsWith('Z')
}

/**
 * Probably correct, but too slow; ~ 15900 steps/ms
 * */
fun main() {
    val lines = readLines(23, 8, "data.txt")
    val rlSequence = lines[0].filter { it in "LR" } // filter just in case
    val paths = lines.subList(2, lines.size)
        .associate { parsePath(it) }
    var numSteps = 0L
    val positions = paths.keys
        .filter { isStartNode(it) }
        .toMutableList()
    // given a position and a rlSequence-index, we can compute how many steps it will take
    // there is very few end positions, so the expected step gain is pretty big :)
    val t0 = System.nanoTime()
    while (true) {
        var good = true
        val isRight = rlSequence[((numSteps++) % rlSequence.length).toInt()] == 'R'
        for (i in positions.indices) {
            var position = positions[i]
            val path = paths[position]!!
            position = if (isRight) path.second else path.first
            positions[i] = position
            good = good and isEndNode(position)
        }
        if (numSteps % 10000000L == 0L) {
            val dt = System.nanoTime() - t0
            println("$numSteps, ${(numSteps * 1000_000 / dt)} steps/ms")
        }
        if (good) {
            break
        }
    }
    println(numSteps)
}