package me.anno.aoc23.day18

import me.anno.utils.Vector2i
import me.anno.utils.Utils.readLines

enum class Direction(val v: Vector2i) {
    UP(Vector2i(0, -1)),
    RIGHT(Vector2i(1, 0)),
    DOWN(Vector2i(0, 1)),
    LEFT(Vector2i(-1, 0));

    fun next(): Direction {
        return entries[(ordinal + 1) and 3]
    }
}

val charToDir = mapOf(
    'U' to Direction.UP,
    'R' to Direction.RIGHT,
    'D' to Direction.DOWN,
    'L' to Direction.LEFT
)

data class Line(var direction: Direction, val length: Int)

fun parseLine(line: String): Line {
    val dir = charToDir[line[0]]!!
    val j = line.indexOf(' ', 3)
    val length = line.substring(2, j).toInt()
    return Line(dir, length)
}

fun main() {
    val lines = readLines(23, 18, "data.txt")
        .map { parseLine(it) }
    val outline = HashSet<Vector2i>()
    fillLinePoints(lines, outline)
    val start = findFillStart(lines, outline)
    println(start)
    val inside = HashSet<Vector2i>()
    floodFill(start, outline, inside)
    println(outline)
    println(outline.size + inside.size)
}

fun fillLinePoints(lines: List<Line>, map: HashSet<Vector2i>) {
    var position = Vector2i(0, 0)
    val start = position
    for (line in lines) {
        for (i in 0 until line.length) {
            map += position
            position += line.direction.v
        }
    }
    if (start != position) {
        throw IllegalStateException("Not a circle")
    }
}

fun findFillStart(lines: List<Line>, map: HashSet<Vector2i>): Vector2i {
    var position = Vector2i(0, 0)
    for (line in lines) {
        val towardsInside = line.direction.next()
        for (i in 0 until line.length) {
            val maybeInside = position + towardsInside.v
            if (maybeInside !in map) {
                return maybeInside
            }
            position += line.direction.v
        }
    }
    throw IllegalStateException("Missing start")
}

fun floodFill(start: Vector2i, outline: Set<Vector2i>, inside: HashSet<Vector2i>) {
    val remaining = ArrayList<Vector2i>()
    fun add(pos: Vector2i) {
        if (pos !in outline && inside.add(pos)) {
            remaining.add(pos)
        }
    }
    add(start)
    while (true) {
        val next = remaining.removeLastOrNull() ?: break
        for (dir in Direction.entries) {
            add(next + dir.v)
        }
    }
}
