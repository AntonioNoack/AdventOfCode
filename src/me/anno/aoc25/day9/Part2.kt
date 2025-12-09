package me.anno.aoc25.day9

import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * This would be the cleaner way, but idk, my isLeft-logic is incorrect
 * */
fun main() {
    val corners = readLines(25, 9, "sample.txt").map {
        val (x, y) = it.split(',').map { it.toInt() }
        Vector2i(x, y)
    }

    fun isGoingLeft(i: Int): Boolean {
        val prev = corners[(i + corners.size - 1) % corners.size]
        val self = corners[i]
        val next = corners[(i + 1) % corners.size]
        val ax = prev.x - self.x
        val ay = prev.y - self.y
        val bx = next.x - self.x
        val by = next.y - self.y
        println("$ax $ay vs $bx $by")
        return ax * by - ay * bx > 0
    }

    fun isCombinationValid(ij: Pair<Int, Int>): Boolean {
        val c0 = corners[ij.first]
        val c1 = corners[ij.second]
        val x0 = min(c0.x, c1.x)
        val y0 = min(c0.y, c1.y)
        val x1 = max(c0.x, c1.x)
        val y1 = max(c0.y, c1.y)
        return corners.indices.all { cornerId ->
            val (x, y) = corners[cornerId]
            x !in x0 + 1 until x1 ||
                    y !in y0 + 1 until y1/* ||
                    run {
                        println("Checking #$cornerId in $c0..$c1")
                        !isGoingLeft(cornerId)
                    }*/
        }
    }

    val combinations = corners.indices.flatMap { j ->
        corners.indices.mapNotNull { i ->
            if (i > j) Pair(i, j)
            else null
        }
    }.map { ij ->
        val (i, j) = ij
        val ci = corners[i]
        val cj = corners[j]
        val dx = abs(ci.x - cj.x) + 1L
        val dy = abs(ci.y - cj.y) + 1L
        ij to (dx * dy)
    }.sortedByDescending { it.second }
    for (combination in combinations) {
        if (isCombinationValid(combination.first)) {
            println("Largest: $combination")
            break
        }
    }

}