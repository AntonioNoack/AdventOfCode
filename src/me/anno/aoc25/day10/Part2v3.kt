package me.anno.aoc25.day10

import me.anno.utils.Utils.readLines
import java.util.*
import kotlin.math.min

// ChatGPT suggested this -> not faster :(

fun encode(sum0: Long, sum1: Long): Long {
    return sum0 * 0x9E3779B97F4A7C1L + sum1
}

fun bidirectionalBFS(
    buttonToLight: List<Pair<Long, Long>>,
    target0: Long,
    target1: Long,
    numDigits: Int,
    numLights: Int,
    shifts: IntArray,
    mask: Long,
    target0Digits: IntArray,
    target1Digits: IntArray
): Int {

    data class Node(val sum0: Long, val sum1: Long)

    val start = Node(0L, 0L)
    val goal = Node(target0, target1)

    // frontier and distance maps
    val frontA = ArrayDeque<Node>()
    val frontB = ArrayDeque<Node>()

    val distA = HashMap<Long, Int>()
    val distB = HashMap<Long, Int>()

    frontA.add(start)
    distA[encode(start.sum0, start.sum1)] = 0

    frontB.add(goal)
    distB[encode(goal.sum0, goal.sum1)] = 0

    fun valid(sum0: Long, sum1: Long): Boolean {
        // check each digit <= target
        for (i in 0 until numDigits) {
            val v = ((sum0 ushr shifts[i]) and mask).toInt()
            if (v > target0Digits[i]) return false
        }
        val n1 = numLights - numDigits
        for (i in 0 until n1) {
            val v = ((sum1 ushr shifts[i]) and mask).toInt()
            if (v > target1Digits[i]) return false
        }
        return true
    }

    while (frontA.isNotEmpty() && frontB.isNotEmpty()) {

        // Always expand the smaller frontier
        if (frontA.size <= frontB.size) {

            val current = frontA.removeFirst()
            val dA = distA[encode(current.sum0, current.sum1)]!!

            for ((b0, b1) in buttonToLight) {
                val n0 = current.sum0 + b0
                val n1 = current.sum1 + b1

                if (!valid(n0, n1)) continue

                val key = encode(n0, n1)
                if (key in distA) continue    // already visited forward

                distA[key] = dA + 1

                // Check if backward search already visited this state
                if (key in distB) {
                    return dA + 1 + distB[key]!!
                }

                frontA.add(Node(n0, n1))
            }

        } else {

            // Expand backward frontier
            val current = frontB.removeFirst()
            val dB = distB[encode(current.sum0, current.sum1)]!!

            for ((b0, b1) in buttonToLight) {
                val p0 = current.sum0 - b0
                val p1 = current.sum1 - b1

                // Don't go below zero-digit (invalid)
                if (p0 < 0 || p1 < 0) continue
                if (!valid(p0, p1)) continue

                val key = encode(p0, p1)
                if (key in distB) continue   // visited backward

                distB[key] = dB + 1

                // Check intersection
                if (key in distA) {
                    return dB + 1 + distA[key]!!
                }

                frontB.add(Node(p0, p1))
            }
        }
    }

    error("No solution")
}

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

        check(numLights <= 2 * numDigits) {
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

        val target0Digits = IntArray(numDigits) {
            task.requirements.getOrNull(it) ?: 0
        }
        val target1Digits = IntArray(numDigits) {
            task.requirements.getOrNull(it + numDigits) ?: 0
        }
        val shifts = IntArray(numDigits) { it * numBitsEach }

        val best = bidirectionalBFS(
            buttonToLight,
            target0, target1,
            numDigits, numLights,
            shifts, mask,
            target0Digits, target1Digits
        )

        println("Best: $best")
        sumBest += best
    }
    println("Total: $sumBest")
}