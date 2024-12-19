package me.anno.aoc24.day18

import me.anno.utils.Utils.directions
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import java.util.*

fun main() {
    val falls = readLines(24, 18, "data.txt")
        .map { it.split(',').map { p -> p.toInt() } }

    val (sx, sy, numFalls) = falls[0]
    val falls1 = falls.subList(1, numFalls + 1)
    // solution: 280
    println(findPathLength(sx, sy, falls1))
}

val unknown = 0
val brick = 1
val offset = 2

fun findPathLength(sx: Int, sy: Int, falls1: List<List<Int>>): Int {

    val field = Array(sy) { IntArray(sx) }
    for ((x, y) in falls1) {
        field[y][x] = brick
    }
    val remaining = PriorityQueue<Vector2i> { a, b ->
        field[a.y][a.x].compareTo(field[b.y][b.x])
    }
    field[0][0] = offset
    remaining.add(Vector2i(0, 0))
    while (true) {
        val here = remaining.poll() ?: break
        val minValue = field[here.y][here.x] + 1
        for (dir in directions) {
            val next = here + dir
            if (next.x in 0 until sx && next.y in 0 until sy) {
                val prevValue = field[next.y][next.x]
                if (prevValue == unknown) {
                    field[next.y][next.x] = minValue
                    remaining.add(next)
                }
            }
        }
    }
    /*for (row in field) {
        println(row.toList())
    }*/
    return field[sy - 1][sx - 1] - offset
}