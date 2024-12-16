package me.anno.aoc24.day6

import me.anno.utils.Utils.directions
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

data class Step(val ax: Int, val ay: Int, val dir: Int)

fun getObstaclePosition(field: Field, step: Step): Vector2i? {
    // if the obstacle is < stepIdx, return false
    val obstacleDir = directions[step.dir]
    val ox = step.ax + obstacleDir.x
    val oy = step.ay + obstacleDir.y
    return if (ox in 0 until field.sx && oy in 0 until field.sy) {
        Vector2i(ox, oy)
    } else null
}

object DoNothingWatcher : Watcher

fun isLoopIfPlaced(field: Field, obstaclePos: Vector2i): Boolean {
    field.lines[obstaclePos.y][obstaclePos.x] = obstacle
    val foundExit = walkGuard(field, DoNothingWatcher)
    field.lines[obstaclePos.y][obstaclePos.x] = empty // reset field
    return !foundExit
}

fun findTakenSteps(field: Field): Collection<Step> {
    val steps = ArrayList<Step>()
    val watcher = object : Watcher {
        override fun onWalk(ax: Int, ay: Int, dir: Int) {
            steps += Step(ax, ay, dir)
        }
    }
    assert(walkGuard(field, watcher))
    return steps
}

fun main() {

    val lines = readLines(24, 6, "data.txt")
    val field = Field(lines)

    val steps = findTakenSteps(field)
    val possibleObstacles = steps
        .mapNotNull { step -> getObstaclePosition(field, step) }
        .filter { obstacle -> field.lines[obstacle.y][obstacle.x] == empty } // must be neither blockade nor start
        .distinct()

    val validObstacles =
        possibleObstacles.filter { obstacle ->
            isLoopIfPlaced(field, obstacle)
        }

    // 415 is too low :(
    // 427 is too low, too :/
    // 1949 is too high :(
    // 1865? not correct, but maybe close :'(
    // 1911 -> correct :), we were missing corners where an agent turned twice in a row
    println("Path length: ${steps.size}, valid: ${validObstacles.size}")
}