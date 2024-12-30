package me.anno.aoc22.day9

import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import kotlin.math.abs

class Snake(var head: Vector2i, var tailDelta: Vector2i) {
    val tail get() = head + tailDelta
    fun move(delta: Vector2i) {
        head += delta
        tailDelta -= delta
        val moveX = abs(tailDelta.x) >= 2
        val moveY = abs(tailDelta.y) >= 2
        if (moveX && moveY) {
            tailDelta.x = sign(tailDelta.x)
            tailDelta.y = sign(tailDelta.y)
        } else if (moveX) {
            tailDelta.x = sign(tailDelta.x)
            tailDelta.y = 0
        } else if (moveY) {
            tailDelta.y = sign(tailDelta.y)
            tailDelta.x = 0
        }
        if (abs(tailDelta.x) >= 2 || abs(tailDelta.y) >= 2) {
            throw IllegalStateException()
        }
    }

    override fun toString(): String {
        return "Snake { $head, $tailDelta }"
    }
}

fun sign(i: Int): Int {
    return when {
        i < 0 -> -1
        i > 0 -> +1
        else -> 0
    }
}

val directions = mapOf(
    'R' to Vector2i(1, 0),
    'L' to Vector2i(-1, 0),
    'U' to Vector2i(0, -1),
    'D' to Vector2i(0, 1)
)

fun main() {
    val snake = Snake(Vector2i(0, 0), Vector2i(0, 0))
    val instructions = readLines(22,9,"sample.txt")
    println(snake)
    for (instruction in instructions) {
        val dir = directions[instruction[0]] ?: throw IllegalStateException(instruction)
        val length = instruction.substring(2).toInt()
        snake.move(dir * length)
        println("$instruction -> $snake")
    }
}