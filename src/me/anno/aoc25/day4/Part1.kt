package me.anno.aoc25.day4

import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

val neighbors = listOf(
    Vector2i(-1, -1),
    Vector2i(+0, -1),
    Vector2i(+1, -1),

    Vector2i(-1, +0),
    Vector2i(+1, +0),

    Vector2i(-1, +1),
    Vector2i(+0, +1),
    Vector2i(+1, +1),
)

fun isRoll(grid: List<CharSequence>, pos: Vector2i): Boolean {
    val line = grid.getOrNull(pos.y) ?: return false
    return line.getOrNull(pos.x) == '@'
}

fun isSymbol(grid: List<CharSequence>, pos: Vector2i, target: Char): Boolean {
    val line = grid.getOrNull(pos.y) ?: return false
    return line.getOrNull(pos.x) == '@'
}

fun canRemoveRoll(grid: List<CharSequence>, pos: Vector2i): Boolean {

    return neighbors.count { neighbor -> isRoll(grid, pos + neighbor) } < 4
}

fun main() {
    val grid = readLines(25, 4, "data.txt")
    val sx = grid[0].length
    var count = 0
    for (y in grid.indices) {
        for (x in 0 until sx) {
            val pos = Vector2i(x, y)
            if (isRoll(grid, pos) &&
                canRemoveRoll(grid, pos)
            ) count++
        }
    }
    println("Count: $count")
}