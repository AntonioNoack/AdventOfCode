package me.anno.aoc23.day6

import me.anno.utils.Utils.readLines
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sqrt

fun getNumPossibleTimes(totalDuration: Double, bestLength: Double): Long {
    // solve: -x²+7x = bestLength
    val idealTime = totalDuration * 0.5
    val idealLength = idealTime * idealTime
    val deltaLength = idealLength - (bestLength + 1) // bestLength + 1, because we need to be better, not just equal
    val deltaTime = sqrt(deltaLength)
    val minTime = ceil(idealTime - deltaTime)
    val maxTime = floor(idealTime + deltaTime)
    return max((maxTime - minTime + 1).toLong(), 0)
}

fun main() {
    val (times, distances) = readLines(23, 6, "data.txt")
        .map { line -> line.substring(line.indexOf(':') + 1)
            .split(' ')
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
        }
    assert(times.size == distances.size)
    val solutions = times.zip(distances).map { (duration, bestLength)->
        getNumPossibleTimes(duration.toDouble(), bestLength.toDouble())
    }
    val product = solutions.reduce { a, b -> a*b }
    println(product)
    // solution: 1108800
}