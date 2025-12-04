package me.anno.aoc25.day4

import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

fun main() {
    val grid = readLines(25, 4, "data.txt").map {
        StringBuilder(it) // make line mutable
    }

    val t0 = System.nanoTime()
    val sx = grid[0].length
    val sy = grid.size

    val toCheckRolls = ArrayList<Vector2i>()

    // forklifts can access all fields from the get-go
    for (x in 0 until sx) {
        for (y in 0 until sy) {
            val pos = Vector2i(x, y)
            if (isRoll(grid, pos) && canRemoveRoll(grid, pos)) {
                toCheckRolls.add(pos)
            }
        }
    }

    var removedRolls = 0
    while (true) {
        val pos = toCheckRolls.removeLastOrNull() ?: break
        if (isRoll(grid, pos) && canRemoveRoll(grid, pos)) {
            removedRolls++
            grid[pos.y][pos.x] = 'x'

            // invalidate all neighbors
            for (n in neighbors) {
                val next = pos + n
                if (isRoll(grid, next) && canRemoveRoll(grid, next)) {
                    toCheckRolls.add(next)
                }
            }
        }
    }

    val t1 = System.nanoTime()
    println("Count: $removedRolls, took ${(t1 - t0) / 1e6f} ms") // 21ms -> fast enough :)
}