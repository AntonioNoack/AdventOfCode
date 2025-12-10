package me.anno.aoc25.day10

import me.anno.utils.Utils.readLines
import java.util.*
import kotlin.math.max

// todo this is linear optimization like the coin problem :(

fun main() {
    val lines = readLines(25, 10, "sample.txt")
    val tasks = lines.map { it.parseTask() }
      //  .run { subList(6, size) }

    var sum = 0
    for (task in tasks) {

        val numLights = task.lightTarget.length
        val numButtons = task.buttonToLight.size
        var best = Int.MAX_VALUE

        val target = IntArray(numLights) {
            task.requirements[it]
        }

        class State(val distance: Int, val sum: IntArray) {
            override fun equals(other: Any?): Boolean {
                return other is State && other.sum.contentEquals(this.sum)
            }

            override fun hashCode(): Int {
                return this.sum.contentHashCode()
            }
        }

        fun heuristic(sum: IntArray): Int {
            var maxValue = 0
            for(i in 0 until numLights) {
                val thisValue = target[i] - sum[i]
                maxValue = max(maxValue, thisValue)
            }
            return maxValue
        }

        val checked = HashSet<State>()
        val next = PriorityQueue<State>(compareBy { it.distance + heuristic(it.sum) })
        val state0 = State(0, IntArray(numLights))
        next.add(state0)
        checked.add(state0)

        var t0 = System.nanoTime()
        loop@ while (true) {

            val curr = next.poll() ?: break
            val t1 = System.nanoTime()
            if (t1 > t0 + 1e9) {
                println("${checked.size}, ${curr.distance}")
                t0 = t1
            }

            for (buttonId in 0 until numButtons) {
                val nextSum = curr.sum.copyOf()
                val button = task.buttonToLight[buttonId]
                for (value in button) {
                    nextSum[value]++
                }

                val nextDistance = curr.distance + 1
                if (nextSum.contentEquals(target)) {
                    println("Best: $nextDistance")
                    best = nextDistance
                    break@loop
                } else if ((0 until numLights).all { lightId ->
                        nextSum[lightId] <= target[lightId]
                    }) {
                    val state1 = State(nextDistance, nextSum)
                    if (checked.add(state1)) {
                        next.add(state1)
                    }
                }
            }
        }

        sum += best
    }
    println("Total: $sum")
}