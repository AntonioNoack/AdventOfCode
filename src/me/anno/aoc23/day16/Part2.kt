package me.anno.aoc23.day16

import me.anno.aoc24.day6.Vector2i
import me.anno.utils.Utils.readLines

fun main() {
    val field = readLines(23, 16, "data.txt")
    val sy = field.size
    val sx = field[0].length
    val startRays =
        (0 until sx).map { x ->
            Ray(Vector2i(x, sy - 1), 0)
        } + (0 until sy).map { y ->
            Ray(Vector2i(0, y), 1)
        } + (0 until sx).map { x ->
            Ray(Vector2i(x, 0), 2)
        } + (0 until sy).map { y ->
            Ray(Vector2i(sx - 1, y), 3)
        }
    println(startRays.maxOfOrNull { start -> numEnergizedTiles(start, field) })
}