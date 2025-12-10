package me.anno.aoc25.day10

import me.anno.utils.Utils.readLines

data class Task(
    val lightTarget: String,
    val buttonToLight: List<List<Int>>,
    val requirements: List<Int>
)

fun String.parseTask(): Task {
    check(this[0] == '[')
    val i0 = indexOf(']')
    val buttons = substring(1, i0)
    val i1 = indexOf('{')
    val requirements = substring(i1 + 1, length - 1).split(',').map { it.toInt() }
    val schematics = substring(i0 + 2, i1 - 1).split(' ').map {
        check(it.startsWith('('))
        check(it.endsWith(')'))
        it.substring(1, it.length - 1).split(',').map { v -> v.toInt() }
    }
    return Task(buttons, schematics, requirements)
}

fun main() {
    val lines = readLines(25, 10, "data.txt")
    val tasks = lines.map { it.parseTask() }

    var sum = 0
    for (task in tasks) {
        // todo a button press is either on or off -> binary
        //  find the number with the min amount of bits achieving the pattern
        //  -> use path finding?

        val numLights = task.lightTarget.length
        val numButtons = task.buttonToLight.size
        val combinations = 1 shl numButtons
        var best = Int.MAX_VALUE

        val target = BooleanArray(numLights) {
            task.lightTarget[it] == '#'
        }

        for (i in 0 until combinations) {
            if (i.countOneBits() >= best) continue
            // todo check if it is a solution
            val lights = BooleanArray(numLights)
            var v = i
            while (v != 0) {
                val bitIndex = v.countTrailingZeroBits()
                val button = task.buttonToLight[bitIndex]
                for (value in button) {
                    lights[value] = !lights[value]
                }

                v = v xor (1 shl bitIndex)
            }
            if (lights.contentEquals(target)) {
                best = i.countOneBits()
                // println("Better: $best for #$i")
            }
        }

        println("Best: $best")
        sum += best
    }
    println("Total: $sum")
}