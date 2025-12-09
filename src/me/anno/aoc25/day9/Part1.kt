package me.anno.aoc25.day9

import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import kotlin.math.abs

fun main() {
    val corners = readLines(25, 9, "data.txt").map {
        val (x, y) = it.split(',').map { it.toInt() }
        Vector2i(x, y)
    }
    val combinations = corners.indices.flatMap { j ->
        corners.indices.mapNotNull { i ->
            if (i > j) Pair(i, j)
            else null
        }
    }.map { (i, j) ->
        val ci = corners[i]
        val cj = corners[j]
        val dx = abs(ci.x - cj.x) + 1L
        val dy = abs(ci.y - cj.y) + 1L
        dx * dy
    }
    println(combinations.max())
}