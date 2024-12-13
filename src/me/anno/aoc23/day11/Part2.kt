package me.anno.aoc23.day11

import me.anno.utils.Vector2i
import me.anno.utils.Utils.readLines
import kotlin.math.max
import kotlin.math.min

val numExpansions = 1_000_000L

fun main() {
    val space = readLines(23, 11, "data.txt")
    val (gapsX, gapsY) = findGaps(space)
    println(space.joinToString("\n"))
    val galaxies =
        space.flatMapIndexed { y: Int, line: String ->
            line.mapIndexedNotNull { x, c ->
                if (c == galaxy) Vector2i(x, y)
                else null
            }
        }
    var totalDistance = 0L
    for (j in galaxies.indices) {
        for (i in j + 1 until galaxies.size) {
            totalDistance += calculateDistance(gapsX, gapsY, galaxies[i], galaxies[j])
        }
    }
    println(totalDistance)
}

fun calculateDistance(gapsX: List<Int>, gapsY: List<Int>, galaxyA: Vector2i, galaxyB: Vector2i): Long {
    return calculateDistance(gapsX, galaxyA.x, galaxyB.x) + calculateDistance(gapsY, galaxyA.y, galaxyB.y)
}

fun calculateDistance(gaps: List<Int>, ax: Int, bx: Int): Long {
    val min = min(ax, bx)
    val max = max(ax, bx)
    val baseDistance = max - min
    val numGaps = findPrevGap(gaps, max) - findPrevGap(gaps, min)
    return numGaps * (numExpansions - 1) + baseDistance
}

fun findPrevGap(sortedGaps: List<Int>, value: Int): Int {
    val notFoundIndex = sortedGaps.binarySearch(value)
    assert(notFoundIndex < 0)
    return -notFoundIndex - 1
}

fun findGapsY(space: List<String>): List<Int> {
    return space.mapIndexedNotNull { y, line ->
        if (line.all { it == empty }) y else null
    }
}

fun findGaps(space: List<String>): Pair<List<Int>, List<Int>> {
    val spacesY = findGapsY(space)
    val spacesX = findGapsY(transpose(space))
    return spacesX to spacesY
}
