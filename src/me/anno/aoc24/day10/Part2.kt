package me.anno.aoc24.day10

import me.anno.aoc24.day6.Vector2i
import me.anno.utils.Utils.readLines

fun main() {
    val field = readLines(24, 10, "data.txt")
    val totalScore = field.flatMapIndexed { y: Int, line: String ->
        line.mapIndexed { x, c ->
            if (c == start) {
                calculateRanking(field, Vector2i(x, y))
            } else 0
        }
    }.sum()
    println(totalScore)
}

/**
 * nearly the same algorithm, just counting how many trails variations lead to the target
 * */
fun calculateRanking(field: List<String>, trailHead: Vector2i): Int {
    var nextStep = start
    var currentPositions = HashMap<Vector2i, Int>()
    var nextPositions = HashMap<Vector2i, Int>()
    currentPositions[trailHead] = 1
    val sx = field[0].length
    val sy = field.size
    while (true) {

        nextStep++

        for ((pos, count) in currentPositions) {
            for (dir in directions) {
                val maybePosition = pos + dir
                if (maybePosition.x in 0 until sx && maybePosition.y in 0 until sy &&
                    field[maybePosition.y][maybePosition.x] == nextStep
                ) {
                    nextPositions[maybePosition] = (nextPositions[maybePosition] ?: 0) + count
                }
            }
        }

        if (nextStep == end) {
            return nextPositions.values.sum()
        } else {
            val tmp = currentPositions
            currentPositions = nextPositions
            nextPositions = tmp
            tmp.clear()
        }
    }
}