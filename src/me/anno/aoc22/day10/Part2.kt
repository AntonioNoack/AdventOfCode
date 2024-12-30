package me.anno.aoc22.day10

import me.anno.utils.Utils.readLines
import kotlin.math.abs

fun main() {

    val instructions = readLines(22, 10, "data.txt")
    var x = 1
    var time = -1

    val queue = ArrayList<Int>()
    val crt = (0 until 6).map {
        BooleanArray(40)
    }

    fun tick() {
        queue.add(x)
        val value = queue.removeAt(0)
        println("$time: $value")

        if (time in 0 until 240) {
            val row = crt[time / 40]
            val timeX = time % 40
            if (abs(value - timeX) <= 1) {
                row[timeX] = true
            }
        }

        time++
    }

    // one less queue add... why???
    queue.add(x)
    for (instruction in instructions) {
        if (instruction.startsWith("noop")) {
            tick()
        } else {
            tick()
            tick()
            x += instruction.substring(5).toInt()
        }
    }
    tick()
    tick()
    tick()

    for (row in crt) {
        println(row.joinToString("") {
            if (it) "##" else "  "
        })
    }
    // solution: BZPAJELK
}