package me.anno.aoc23.day11

import me.anno.utils.Vector2i
import me.anno.utils.Utils.readLines
import kotlin.math.abs

val empty = '.'
val galaxy = '#'

fun main() {
    val space0 = readLines(23, 11, "data.txt")
    val space = expandSpace(space0)
    println(space.joinToString("\n"))
    val galaxies =
        space.flatMapIndexed { y: Int, line: String ->
            line.mapIndexedNotNull { x, c ->
                if (c == galaxy) Vector2i(x, y)
                else null
            }
        }
    var totalDistance = 0
    for (j in galaxies.indices) {
        for (i in j + 1 until galaxies.size) {
            totalDistance += calculateDistance(galaxies[i], galaxies[j])
        }
    }
    println(totalDistance)
}

fun calculateDistance(galaxyA: Vector2i, galaxyB: Vector2i): Int {
    return abs(galaxyA.x - galaxyB.x) + abs(galaxyA.y - galaxyB.y)
}

fun expandSpace(space: List<String>): List<String> {
    val space1 = expandSpaceVertically(space)
    val space1t = transpose(space1)
    val space2t = expandSpaceVertically(space1t)
    return transpose(space2t)
}

fun expandSpaceVertically(space: List<String>): List<String> {
    return space.flatMap { line ->
        if (line.all { it == empty }) listOf(line, line) // duplicate empty lines
        else listOf(line) // keep the rest the same
    }
}

fun transpose(space: List<String>): List<String> {
    val sx = space[0].length
    val sy = space.size
    return (0 until sx).map { x ->
        String(CharArray(sy) { y ->
            space[y][x]
        })
    }
}