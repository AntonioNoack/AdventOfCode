package me.anno.aoc24.day14

import me.anno.aoc23.day12.toInt
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

fun main() {
    val robots = readLines(24, 14, "data.txt")
        .map { line -> parseRobot(line) }
    val quadrants = IntArray(4)
    val time = 100
    val spaceSize = Vector2i(101, 103)
    // val spaceSize = Vector2i(11, 7)
    // printRobotMap(robots, 0, spaceSize)
    // printRobotMap(robots, time, spaceSize)
    for (robot in robots) {
        val pos = robot.getPositionAt(time, spaceSize)
        val qx = getQuadrantHalf(pos.x, spaceSize.x) ?: continue
        val qy = getQuadrantHalf(pos.y, spaceSize.y) ?: continue
        quadrants[qx.toInt(2) + qy.toInt()]++
    }
    println(quadrants.toList())
    println(quadrants.reduce { a, b -> a * b })
}

fun printRobotMap(robots: List<Robot>, time: Int, spaceSize: Vector2i) {
    val positions = robots.map { it.getPositionAt(time, spaceSize) }
    println("time: $time, $positions")
    val map = positions
        .groupBy { it }.mapValues { it.value.size }
    for (y in 0 until spaceSize.y) {
        for (x in 0 until spaceSize.x) {
            val count = map[Vector2i(x, y)]
            print(if (count != null) ('0' + count) else '.')
        }
        println()
    }
}

fun parseRobot(line: String): Robot {
    //p=0,4 v=3,-3
    val i0 = 2
    val i1 = line.indexOf(',', 2)
    val i2 = line.indexOf(" v=", i1)
    val i3 = line.indexOf(',', i2)
    val px = line.substring(i0, i1).toInt()
    val py = line.substring(i1 + 1, i2).toInt()
    val vx = line.substring(i2 + 3, i3).toInt()
    val vy = line.substring(i3 + 1).toInt()
    return Robot(Vector2i(px, py), Vector2i(vx, vy))
}

fun getQuadrantHalf(x: Int, sizeX: Int): Boolean? {
    val middle = sizeX shr 1
    return when {
        x < middle -> false
        x > middle -> true
        else -> null
    }
}

class Robot(val position: Vector2i, val velocity: Vector2i) {
    fun getPositionAt(time: Int, spaceSize: Vector2i): Vector2i {
        return (position + velocity * time).posMod(spaceSize)
    }
}