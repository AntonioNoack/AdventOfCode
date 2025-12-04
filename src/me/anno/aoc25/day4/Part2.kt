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

val removed = 'x'

fun main() {

    val lines0 = readLines(25, 4, "data.txt")
    val runs = 1000

    val t0 = System.nanoTime()
    repeat(runs) {
        val grid = lines0.map { line ->
            StringBuilder(line) // make line mutable
        }

        val sx = grid[0].length
        val sy = grid.size

        val stack = IntArray(sx * sy)
        var stackSize = 0

        // forklifts can access all fields from the get-go
        var count = 0
        for (x in 0 until sx) {
            for (y in 0 until sy) {
                if (canRemoveRoll(grid, x, y)) {
                    grid[y][x] = removed
                    count++
                    stack[stackSize++] = x
                    stack[stackSize++] = y
                }
            }
        }

        while (stackSize > 0) {
            stackSize -= 2
            val x = stack[stackSize]
            val y = stack[stackSize + 1]

            for (n in neighbors) {
                val nx = x + n.x
                val ny = y + n.y
                if (canRemoveRoll(grid, nx, ny)) {
                    grid[ny][nx] = removed
                    count++

                    stack[stackSize++] = nx
                    stack[stackSize++] = ny
                }
            }
        }

        if (it == 0) {
            println("Count: $count")
        }
    }

    // start: 24 ms,
    // now: 9.4 ms (by removing objects and manual stack)
    // and with 1000 runs now 1.1 ms/run :)
    val t1 = System.nanoTime()
    println("Took ${(t1 - t0) / (runs * 1e6f)} ms/run")

}