package me.anno.aoc23.day6

import me.anno.utils.Utils.readLines

// this part is sooo easy XD, I guess they don't expect you to use maths, and instead expect a search algorithm
fun main() {
    val (time, distance) = readLines(23, 6, "data.txt")
        .map { line ->
            line.substring(line.indexOf(':') + 1)
                .replace(" ", "")
                .toLong()
        }
    val solutions = getNumPossibleTimes(time.toDouble(), distance.toDouble())
    println(solutions)
    // solution: 36919753
}