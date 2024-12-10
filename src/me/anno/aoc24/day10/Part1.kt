package me.anno.aoc24.day10

import me.anno.aoc24.day6.Vector2i
import me.anno.utils.Utils.readLines

val start = '0'
val end = '9'

fun main() {
    val field = readLines(24, 10, "data.txt")
    val totalScore = field.flatMapIndexed { y: Int, line: String ->
        line.mapIndexed { x, c ->
            if (c == start) {
                calculateScore(field, Vector2i(x, y))
            } else 0
        }
    }.sum()
    println(totalScore)
}

val directions = listOf(
    Vector2i(0, 1),
    Vector2i(1, 0),
    Vector2i(0, -1),
    Vector2i(-1, 0),
)

fun calculateScore(field: List<String>, trailHead: Vector2i): Int {
    var nextStep = start
    var currentPositions = HashSet<Vector2i>()
    var nextPositions = HashSet<Vector2i>()
    currentPositions += trailHead
    val sx = field[0].length
    val sy = field.size
    while (true) {

        nextStep++

        for (pos in currentPositions) {
            for (dir in directions) {
                val maybePosition = pos + dir
                if (maybePosition.x in 0 until sx && maybePosition.y in 0 until sy &&
                    field[maybePosition.y][maybePosition.x] == nextStep
                ) nextPositions.add(maybePosition)
            }
        }

        if (nextStep == end) {
            return nextPositions.size
        } else {
            val tmp = currentPositions
            currentPositions = nextPositions
            nextPositions = tmp
            tmp.clear()
        }
    }
}