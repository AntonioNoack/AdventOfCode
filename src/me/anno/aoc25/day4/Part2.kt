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

private const val removed = 'x'

fun main() {

    val lines0 = readLines(25, 4, "data.txt")
    val runs = 1000

    val t0 = System.nanoTime()
    repeat(runs) { runId ->
        val grid = lines0.map { line ->
            StringBuilder(line) // make line mutable
        }

        val sx = grid[0].length
        val sy = grid.size

        var numRemovedRolls = 0

        fun removeRoll(x: Int, y: Int) {

            grid[y][x] = removed
            numRemovedRolls++

            for (n in neighbors) {
                val nx = x + n.x
                val ny = y + n.y
                if (canRemoveRoll(grid, nx, ny)) {
                    removeRoll(nx, ny)
                }
            }
        }

        // forklifts can access all fields from the get-go
        for (x in 0 until sx) {
            for (y in 0 until sy) {
                if (canRemoveRoll(grid, x, y)) {
                    removeRoll(x, y)
                }
            }
        }

        if (runId == 0) {
            println("Count: $numRemovedRolls")
        }
    }

    // start: 24 ms,
    // now: 9.4 ms (by removing objects and manual stack)
    // and with 1000 runs now 1.1 ms/run :)
    val t1 = System.nanoTime()
    println("Took ${(t1 - t0) / (runs * 1e6f)} ms/run")

}