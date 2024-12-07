package me.anno.aoc23.day8

import me.anno.utils.Utils.readLines

fun parsePath(line: String): Pair<String, Pair<String, String>> {
    val src = line.substring(0, 3)
    val left = line.substring(7, 10)
    val right = line.substring(12, 15)
    return src to (left to right)
}

fun main() {
    val lines = readLines(23, 8, "data.txt")
    val rlSequence = lines[0]
    val paths = lines.subList(2, lines.size)
        .associate { parsePath(it) }
    var numSteps = 0
    var position = "AAA"
    val end = "ZZZ"
    while (position != end) {
        val path = paths[position]!!
        val isRight = rlSequence[(numSteps++) % rlSequence.length] == 'R'
        position = if (isRight) path.second else path.first
    }
    println(numSteps)
}