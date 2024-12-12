package me.anno.aoc23.day17

import me.anno.aoc24.day6.Vector2i
import me.anno.utils.Utils.readLines
import java.util.*

fun main() {
    val field = readLines(23, 17, "data.txt")
    val sx = field[0].length
    val sy = field.size
    val path = shortestPath3(Vector2i(0, 0), Vector2i(sx - 1, sy - 1), field)
    // solution: 918
    println(path)
}

fun DirList.plus3(dir1: Int): DirList? {
    return if (dir1 == dir) {
        // same direction
        if (length <= 9) {
            DirList(dir, length + 1)
        } else null
    } else if (isOpposite(dir, dir1) || length < 4) {
        null // illegal turn
    } else {
        DirList(dir1, 1)
    }
}

fun shortestPath3(start: Vector2i, end: Vector2i, field: List<String>): Path2 {

    val remaining = PriorityQueue<Path2>()
    val uniquePaths = HashSet<PathKey>()

    remaining.add(Path2(start, DirList(1, 0), 0))
    remaining.add(Path2(start, DirList(2, 0), 0))

    while (true) {
        val here = remaining.poll()!!
        for (dir in 0 until 4) {
            val direction = directions[dir]
            val nextPos = here.position + direction
            val nextDirList = here.dirList.plus3(dir)
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