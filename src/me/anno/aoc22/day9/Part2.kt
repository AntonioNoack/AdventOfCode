package me.anno.aoc22.day9

import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

class LongSnake(val length: Int) {

    val parts = ArrayList<Snake>(length - 1)
    val tail get() = parts.last().tail

    init {
        for (i in 1 until length) {
            parts.add(Snake(Vector2i(0, 0), Vector2i(0, 0)))
        }
    }

    fun move(delta: Vector2i) {
        var deltaI = delta
        for (i in 1 until length) {
            val p0 = parts[i - 1]
            p0.move(deltaI)
            val p1 = parts.getOrNull(i) ?: break // done
            if (p0.tail == p1.head) break
            deltaI = p0.tail - p1.head
        }
    }

    override fun toString(): String {
        return "LongSnake ${parts.map { it.head } + tail}"
    }
}

fun main() {
    val snake = LongSnake(10)
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
    // solution: 3139 -> too high :/
    //           2445 -> correct :)
    showSnake(snake, tailVisited)
    println(tailVisited.size)
}

fun showSnake(snake: LongSnake, tailVisited: HashSet<Vector2i>) {
    var min = Vector2i(0, 0)
    var max = min
    fun union(v: Vector2i) {
        min = min.min(v)
        max = max.max(v)
    }
    for (part in snake.parts) {
        union(part.head)
        union(part.tail)
    }
    for (pos in tailVisited) {
        union(pos)
    }
    val symbols = HashMap<Vector2i, Char>()
    for (pos in tailVisited) {
        symbols[pos] = '#'
    }
    for ((i, part) in snake.parts.withIndex().reversed()) {
        symbols[part.head] = '0' + i
    }
    symbols[Vector2i(0, 0)] = 's'
    val field = (min.y..max.y).map { y ->
        (min.x..max.x).map { x ->
            symbols[Vector2i(x, y)] ?: '.'
        }.joinToString("")
    }
    println(snake)
    for (line in field) {
        println(line)
    }
    println()
}