package me.anno.aoc25.day4

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym
import me.anno.aoc23.day21.sy
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

val toiletPaper = '@'

fun isSymbol(grid: List<CharSequence>, pos: Vector2i, symbol: Char): Boolean {
    val line = grid.getOrNull(pos.y) ?: return false
    return pos.x in line.indices && line[pos.x] == symbol
}

fun canRemoveRoll(grid: List<CharSequence>, pos: Vector2i): Boolean {
    return isSymbol(grid, pos, toiletPaper) &&
            neighbors.count { neighbor -> isSymbol(grid, pos + neighbor, toiletPaper) } < 4
}

fun main() {
    val grid = readLines(25, 4, "data.txt")
    val sx = grid[0].length
    var count = 0
    for (y in grid.indices) {
        for (x in 0 until sx) {
            val pos = Vector2i(x, y)
            if (canRemoveRoll(grid, pos)) count++
        }
    }
    println("Count: $count")
}