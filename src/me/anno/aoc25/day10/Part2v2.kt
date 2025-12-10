package me.anno.aoc25.day10

import me.anno.utils.Utils.readLines
import java.util.*
import kotlin.math.max
import kotlin.math.min

// optimized, but runs OOM

fun main() {
    val lines = readLines(25, 10, "data.txt")
    val tasks = lines.map { it.parseTask() }
    // .run { subList(6, size) }

    var sumBest = 0
    tasks@ for (task in tasks) {

        val numLights = task.lightTarget.length
        val numButtons = task.buttonToLight.size

        val numBitsEach = min(128 / numLights, 9)
        val mask = (1L shl numBitsEach) - 1
        val numDigits = 64 / numBitsEach

        // println("numBitsEach: $numBitsEach")

        fun add(index: Int): Long {
            return 1L shl (index * numBitsEach)
        }

        fun get(array: Long, index: Int): Int {
            return ((array ushr (index * numBitsEach)) and mask).toInt()
        }

        val buttonToLight = task.buttonToLight.map { buttons ->
            var sum0 = 0L
            var sum1 = 0L
            for (button in buttons) {
                if (button < numDigits) sum0 += add(button)
                else sum1 += add(button - numDigits)
            }
            sum0 to sum1
        }

        check(numLights <= 2 * numDigits){
            "$numLights vs $numDigits"
        }

        var target0 = 0L
        var target1 = 0L
        for (i in 0 until min(numDigits, numLights)) {
            check(task.requirements[i] < mask - 1) {
                "Requirement too large: $numLights * ${task.requirements[i]}"
            }
            target0 += add(i) * task.requirements[i]
        }
        for (i in numDigits until numLights) {
            check(task.requirements[i] < mask - 1) {
                "Requirement too large: $numLights * ${task.requirements[i]}"
            }
            target1 += add(i - numDigits) * task.requirements[i]
        }

        fun heuristic(sum0: Long, sum1: Long): Int {
            var maxValue = 0
            for (i in 0 until numDigits) {
                val thisValue = get(target0, i) - get(sum0, i)
                maxValue = max(maxValue, thisValue)
            }
            for (i in 0 until numLights - numDigits) {
                val thisValue = get(target1, i) - get(sum1, i)
                maxValue = max(maxValue, thisValue)
            }
            return maxValue
        }

        data class State(val distance: Int, val sum0: Long, val sum1: Long) {
            val score = distance + heuristic(this.sum0, this.sum1)
        }

        val checked = HashSet<State>(16 shl 10)
        val next = PriorityQueue<State>(compareBy { it.score })
        val state0 = State(0, 0, 0)
        next.add(state0)
        checked.add(state0)

        var t0 = System.nanoTime()
        loop@ while (true) {
            val curr = next.poll() ?: break
            // println("curr: $curr")

            val t1 = System.nanoTime()
            if (t1 > t0 + 1e9) {
                println("${checked.size}, ${curr.distance}")
                t0 = t1
            }

            for (buttonId in 0 until numButtons) {
                val button = buttonToLight[buttonId]
                val nextSum0 = curr.sum0 + button.first
                val nextSum1 = curr.sum1 + button.second
                val nextDistance = curr.distance + 1
                if (nextSum0 == target0 && nextSum1 == target1) {
                    println("Best: $nextDistance")
                    sumBest += nextDistance
                    continue@tasks
                } else if ((0 until numDigits).all { lightId ->
                        get(nextSum0, lightId) <=
                                get(target0, lightId)
                    } && (0 until numLights - numDigits).all { lightId ->
                        get(nextSum1, lightId) <=
                                get(target1, lightId)
                    }) {
                    val state1 = State(nextDistance, nextSum0, nextSum1)
                    if (checked.add(state1)) {
                        next.add(state1)
                    }
                }
            }
        }
        throw IllegalStateException("No solution found")
    }
    println("Total: $sumBest")
}