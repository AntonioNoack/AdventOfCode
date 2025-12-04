package me.anno.aoc25.day4

import me.anno.utils.Utils.readLines

fun isSymbol(grid: List<CharSequence>, x: Int, y: Int, symbol: Char): Boolean {
    val line = grid.getOrNull(y) ?: return false
    return x in line.indices && line[x] == symbol
}

fun canRemoveRoll(grid: List<CharSequence>, x: Int, y: Int): Boolean {
    return isSymbol(grid, x, y, toiletPaper) &&
            neighbors.count { neighbor -> isSymbol(grid, x + neighbor.x, y + neighbor.y, toiletPaper) } < 4
}

fun main() {
    val grid = readLines(25, 4, "data.txt").map {
        StringBuilder(it) // make line mutable
    }

    val t0 = System.nanoTime()
    val sx = grid[0].length
    val sy = grid.size

    val stack = IntArray(1024)
    var stackSize = 0

    var removedRolls = 0
    fun removeRollsRecursively(x: Int, y: Int) {
        removedRolls++
        grid[y][x] = 'x'

        // invalidate all neighbors
        for (n in neighbors) {
            val nx = x + n.x
            val ny = y + n.y
            if (canRemoveRoll(grid, nx, ny)) {
                removeRollsRecursively(nx, ny)
            }
        }
    }

    // forklifts can access all fields from the get-go
    for (x in 0 until sx) {
        for (y in 0 until sy) {
            if (canRemoveRoll(grid, x, y)) {
                removeRollsRecursively(x, y)
            }
        }
    }

    // start: 24 ms,
    // now: 10 ms (by removing objects and manual stack)
    val t1 = System.nanoTime()
    println("Count: $removedRolls, took ${(t1 - t0) / 1e6f} ms")
}