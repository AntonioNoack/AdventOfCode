package me.anno.aoc24.day6

import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

// define walking directions
val directions = listOf(
    0 to -1,
    1 to 0,
    0 to 1,
    -1 to 0,
)

val obstacle = '#'
val empty = '.'
val agent = '^'

fun nextDir(dir: Int): Int {
    return (dir + 1) % directions.size
}

/**
 * returns whether agent exists the field
 * */
fun walkGuard(field: Field, watcher: Watcher): Boolean {

    // find agent position
    var (ax, ay) = field.start
    var dir = 0

    watcher.onWalk(ax, ay, dir)

    val uniqueTurns = HashSet<Step>()
    // while agent is still inside field, move around
    walking@ while (true) {
        val dirI = directions[dir]
        val nx = ax + dirI.first
        val ny = ay + dirI.second
        if (nx !in 0 until field.sx || ny !in 0 until field.sy) {
            // done, agent left field
            return true
        }
        val tile = field.lines[ny][nx]
        if (tile == obstacle) {
            // turn to the right
            dir = nextDir(dir)
            watcher.onWalk(ax, ay, dir)
            if (!uniqueTurns.add(Step(ax, ay, dir))) {
                // infinite loop -> agent never exists
                return false
            }
        } else {
            // mark field as reached
            ax = nx
            ay = ny
            watcher.onWalk(ax, ay, dir)
        }
    }
}

fun main() {

    val lines = readLines(24, 6, "data.txt")
    val field = Field(lines)

    // solution using only Part1:
    /*val reached = 'x'
    val watcher = object : Watcher {
        override fun onWalk(ax: Int, ay: Int, dir: Int) {
            field.lines[ay][ax] = reached
        }
    }

    assert(walkGuard(field, watcher))

    val reachedTiles = field.lines.sumOf { line ->
        line.count { it == reached }
    }*/

    // solution with Part2:
    val reachedTiles = findTakenSteps(field)
        .map { Vector2i(it.ax, it.ay) }
        .distinct().size
    println(reachedTiles)
}