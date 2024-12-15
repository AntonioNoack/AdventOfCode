package me.anno.aoc23.day21

import me.anno.aoc23.day10.directions
import me.anno.utils.Utils.findPosition
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

val garden = '.'
val rock = '#'
val startSymbol = 'S' // also a garden

fun main() {
    println(countReachableFields(readLines(23, 21, "sample.txt"), 6))
    println(countReachableFields(readLines(23, 21, "data.txt"), 64))
}

val directions1 = directions

fun countReachableFields(field: List<String>, maxSteps: Int): Int {
    val start = findPosition(field, startSymbol)
    return countReachableFields(field, maxSteps, start)
}

fun countReachableFields(field: List<String>, maxSteps: Int, start: Vector2i): Int {
    var remaining0 = HashSet<Vector2i>()
    var remaining1 = HashSet<Vector2i>()
    val sx = field[0].length
    val sy = field.size
    remaining0.add(start)
    var i = 0
    while (true) {
        for (pos in remaining0) {
            for (dir in directions1) {
                val next = pos + dir
                if (next.y in 0 until sy && next.x in 0 until sx &&
                    field[next.y][next.x] != rock
                ) {
                    remaining1.add(next)
                }
            }
        }
        if (i++ == maxSteps) {
            return remaining0.size
        } else {
            val tmp = remaining0
            remaining0 = remaining1
            remaining1 = tmp
        }
    }
}