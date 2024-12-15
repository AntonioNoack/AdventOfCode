package me.anno.aoc23.day10

import me.anno.utils.Utils.findPosition
import me.anno.utils.Vector2i
import me.anno.utils.Utils.readLines

data class PipeTrack(val symbol: Char, val dir: Int)

val startSymbol = 'S'

// up: 0, right: 1, down: 2, left: 3
val directions = listOf(
    Vector2i(0, -1),
    Vector2i(1, 0),
    Vector2i(0, 1),
    Vector2i(-1, 0)
)

val pipes = mapOf(
    PipeTrack('-', 1) to 1,
    PipeTrack('-', 3) to 3,
    PipeTrack('|', 0) to 0,
    PipeTrack('|', 2) to 2,
    PipeTrack('F', 0) to 1,
    PipeTrack('F', 3) to 2,
    PipeTrack('J', 1) to 0,
    PipeTrack('J', 2) to 3,
    PipeTrack('L', 3) to 0,
    PipeTrack('L', 2) to 1,
    PipeTrack('7', 1) to 2,
    PipeTrack('7', 0) to 3,
)

fun trackUntilS(field: List<String>, dir0: Int, start: Vector2i, tracker: Tracker?): Int {
    val dirI = directions[dir0]
    var dir = dir0
    var px = start.x + dirI.x
    var py = start.y + dirI.y
    val sx = field[0].length
    var numSteps = 1
    while (true) {
        // track along sides of S until S is found again
        if (py !in field.indices || px !in 0 until sx) {
            // out of bounds
            return 0
        }
        val nextPipe = field[py][px]
        tracker?.nextTile(px, py, dir)
        if (nextPipe == startSymbol) return numSteps
        dir = pipes[PipeTrack(nextPipe, dir)] ?: return 0 // found invalid tile
        val dirJ = directions[dir]
        px += dirJ.x
        py += dirJ.y
        numSteps++
    }
}

fun main() {
    val field = readLines(23, 10, "sample.txt")
    val start = findPosition(field, startSymbol)
    for (i in 0 until 4) {
        val length = trackUntilS(field, i, start, null)
        if (length > 0) {
            println(length / 2)
            break
        }
    }
}