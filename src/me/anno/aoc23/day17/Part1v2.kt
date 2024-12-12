package me.anno.aoc23.day17

import me.anno.aoc24.day6.Vector2i
import me.anno.utils.Utils.readLines
import java.util.*
import kotlin.math.min

fun main() {
    val field = readLines(23, 17, "data.txt")
    val sx = field[0].length
    val sy = field.size
    val path = shortestPath2(Vector2i(0, 0), Vector2i(sx - 1, sy - 1), field)
    // we're better than the ideal :(, 738 -> because we turned 180°
    // -> 742
    println(path)
}

data class PathKey(val position: Vector2i, val dirList: DirList)

data class Path2(val position: Vector2i, val dirList: DirList, val cost: Int) :
    Comparable<Path2> {
    override fun compareTo(other: Path2): Int {
        return cost.compareTo(other.cost)
    }
}

data class DirList(val dir: Int, val length: Int) {
    operator fun plus(dir1: Int): DirList? {
        return if (dir1 == dir) {
            if (length <= 2) {
                DirList(dir, length + 1)
            } else null
        } else if (isOpposite(dir, dir1)) {
            null
        } else {
            DirList(dir1, 1)
        }
    }
}

fun isValidPosition(end: Vector2i, nextPos: Vector2i): Boolean {
    return nextPos.x in 0..end.x && nextPos.y in 0..end.y
}

fun shortestPath2(start: Vector2i, end: Vector2i, field: List<String>): Path2 {

    val remaining = PriorityQueue<Path2>()
    val uniquePaths = HashSet<PathKey>()

    remaining.add(Path2(start, DirList(0, 0), 0))

    while (true) {
        val here = remaining.poll()!!
        for (dir in 0 until 4) {
            val direction = directions[dir]
            val nextPos = here.position + direction
            val nextDirList = here.dirList + dir
            if (nextDirList != null && isValidPosition(end, nextPos)) {
                val nextCost = here.cost + (field[nextPos.y][nextPos.x] - '0')
                val path = Path2(nextPos, nextDirList, nextCost)
                if (uniquePaths.add(PathKey(nextPos, nextDirList))) {
                    if (nextPos == end) {
                        return path
                    }
                    remaining.add(path)
                }
            }
        }
    }
}