package me.anno.aoc22.day9

import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

fun main() {
    val snake = Snake(Vector2i(0, 0), Vector2i(0, 0))
    val instructions = readLines(22, 9, "data.txt")
    val tailVisited = HashSet<Vector2i>()
    tailVisited += snake.tail
    for (instruction in instructions) {
        val dir = directions[instruction[0]] ?: throw IllegalStateException(instruction)
        val length = instruction.substring(2).toInt()
        for (i in 0 until length) {
            snake.move(dir)
            tailVisited += snake.tail
        }
        println("$instruction -> $snake")
    }
    println(tailVisited.size)
    // solution: 5902
}