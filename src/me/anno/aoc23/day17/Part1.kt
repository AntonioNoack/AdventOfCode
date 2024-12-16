package me.anno.aoc23.day17

import me.anno.utils.Utils.directions
import me.anno.utils.Vector2i
import me.anno.utils.Utils.readLines
import java.util.*
import kotlin.math.min

fun main() {
    val field = readLines(23, 17, "sample.txt")
    val sx = field[0].length
    val sy = field.size
    val path = shortestPath(Vector2i(0, 0), Vector2i(sx - 1, sy - 1), field)
    // we're better than the ideal :(
    println(path)
}

data class Path(val steps: List<Vector2i>, val position: Vector2i, val last3Dirs: List<Int>, val cost: Int) :
    Comparable<Path> {
    override fun compareTo(other: Path): Int {
        return cost.compareTo(other.cost)
    }
}

fun isValidDirection(dir: Int, last3Dirs: List<Int>, end: Vector2i, nextPos: Vector2i): Boolean {
    return nextPos.x in 0..end.x && nextPos.y in 0..end.y &&
            !last3Dirs.all { it == dir } && !isOpposite(last3Dirs.last(), dir)
}

fun isOpposite(a: Int, b: Int): Boolean {
    val diff = (a - b).and(3)
    return diff == 2
}

fun shortestPath(start: Vector2i, end: Vector2i, field: List<String>): Path {

    val bestCosts = HashMap<Vector2i, Int>()
    val remaining = PriorityQueue<Path>()

    remaining.add(Path(listOf(start), start, listOf(-1, -1, -1), 0))

    val costThreshold = 10 // 10 finds a better solution than ideal with sample.txt
    var i = 0
    while (true) {
        if (i++ % 100000 == 0) {
            println(i)
        }
        val here = remaining.poll()!!
        for (dir in 0 until 4) {
            val direction = directions[dir]
            val nextPos = here.position + direction
            if (isValidDirection(dir, here.last3Dirs, end, nextPos)) {
                val nextCost = here.cost + (field[nextPos.y][nextPos.x] - '0')
                val prevBestCost = bestCosts[nextPos] ?: Int.MAX_VALUE
                if (nextCost - costThreshold < prevBestCost) {
                    val path = Path(here.steps + nextPos, nextPos, here.last3Dirs.subList(1, 3) + dir, nextCost)
                    if (nextPos == end) {
                        val newField = field.map {
                            it.toCharArray()
                        }

                        for (pos in path.steps) {
                            newField[pos.y][pos.x] = '.'
                        }
                        println(newField.joinToString("\n") { String(it) })
                        println(path.steps.subList(1, path.steps.size).map { field[it.y][it.x] })
                        println(path.steps.subList(1, path.steps.size).map { field[it.y][it.x] }.sumOf { it - '0' })
                        return path
                    }
                    remaining.add(path)
                    bestCosts[nextPos] = min(prevBestCost, nextCost)
                }
            }
        }
    }
}