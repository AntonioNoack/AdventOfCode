package me.anno.aoc22.day12

import me.anno.utils.Utils.directions
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import java.util.*

fun find(letter: Char, lines: List<String>): Vector2i {
    for (y in lines.indices) {
        val x = lines[y].indexOf(letter)
        if (x >= 0) return Vector2i(x, y)
    }
    throw IllegalStateException("Missing '$letter'")
}

fun getHeight(letter: Char): Int {
    return when (letter) {
        'S' -> 0
        'E' -> 25
        in 'a'..'z' -> letter - 'a'
        else -> throw IllegalStateException("Invalid symbol $letter")
    }
}

fun main() {
    val lines = readLines(22, 12, "data.txt")
    val start = find('S', lines)
    findShortestPath(lines, listOf(start))
}

fun findShortestPath(lines: List<String>, starts: List<Vector2i>) {

    class Node(val pos: Vector2i, val height: Int, val distance: Int) : Comparable<Node> {
        override fun compareTo(other: Node): Int {
            return distance.compareTo(other.distance)
        }
    }

    val remaining = PriorityQueue<Node>()
    val done = HashSet<Vector2i>()

    for (start in starts) {
        done.add(start)
        remaining.add(Node(start, 0, 0))
    }

    val sizeX = lines[0].length
    val sizeY = lines.size

    while (true) {
        val node = remaining.poll() ?: break
        val (x0, y0) = node.pos
        val nextDistance = node.distance + 1
        for (dir in directions) {
            val x = x0 + dir.x
            val y = y0 + dir.y
            if (x in 0 until sizeX && y in 0 until sizeY) {
                val nextLetter = lines[y][x]
                val nextHeight = getHeight(nextLetter)
                if (nextHeight - node.height <= 1) {
                    val nextPos = Vector2i(x, y)
                    if (done.add(nextPos)) {
                        if (nextLetter == 'E') {
                            println("Found end after $nextDistance steps")
                            return
                        }

                        remaining.add(Node(nextPos, nextHeight, nextDistance))
                    }
                }
            }
        }
    }

    for (y in lines.indices) {
        println((0 until sizeX).joinToString("") { x ->
            if (Vector2i(x, y) in done) "x" else "_"
        })
    }
    throw IllegalStateException("Missing path, explored ${done.size}/${sizeX * sizeY} nodes")

}