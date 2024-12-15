package me.anno.aoc24.day15

import me.anno.utils.Utils.findPosition
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

val robot = '@'
val box = 'O'
val wall = '#'
val empty = '.'

val up = '^'
val right = '>'
val down = 'v'
val left = '<'

val directions = mapOf(
    up to Vector2i(0, -1),
    right to Vector2i(1, 0),
    down to Vector2i(0, 1),
    left to Vector2i(-1, 0)
)

fun getField(field: List<CharArray>, pos: Vector2i): Char {
    return field[pos.y][pos.x]
}

fun setField(field: List<CharArray>, pos: Vector2i, symbol: Char) {
    field[pos.y][pos.x] = symbol
}

fun printField(level: List<CharArray>) {
    for (row in level) {
        println(String(row))
    }
}

fun main() {
    val lines = readLines(24, 15, "data.txt")
    val emptyLineIdx = lines.indexOf("")
    val field0 = lines.subList(0, emptyLineIdx)
    val field1 = field0.map { it.toCharArray() }
    val moves = lines.subList(emptyLineIdx + 1, lines.size)
        .joinToString("").mapNotNull { directions[it] }
    var robotPos = findPosition(field0, robot)
    for (dir in moves) {
        // try to apply move...
        // first find out how many boxes should be moved
        var numBoxes = 0
        while (getField(field1, robotPos + (dir * (numBoxes + 1))) == box) {
            numBoxes++
        }
        // then check if the field after that is empty
        val gapPos = robotPos + (dir * (numBoxes + 1))
        if (getField(field1, gapPos) == empty) {
            // and if so, apply the move
            val newRobotPos = robotPos + dir
            if (numBoxes > 0) {
                setField(field1, gapPos, box)
            }
            setField(field1, robotPos, empty)
            setField(field1, newRobotPos, robot)
            robotPos = newRobotPos
        }
        // else nothing to do
    }
    printField(field1)
    println(getCoordSum(field1, box))
}

fun getCoordSum(field1: List<CharArray>, box: Char): Int {
    var coordSum = 0
    for (y in field1.indices) {
        val line = field1[y]
        for (x in line.indices) {
            if (line[x] == box) {
                val coords = x + y * 100
                coordSum += coords
            }
        }
    }
    return coordSum
}