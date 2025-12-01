package me.anno.aoc22.day12

import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

fun main() {
    val lines = readLines(22, 12, "data.txt")
    val starts = lines.indices.flatMap { y ->
        val line = lines[y]
        line.indices.mapNotNull { x ->
            if (getHeight(line[x]) == 0) Vector2i(x, y)
            else null
        }
    }
    findShortestPath(lines, starts)
}