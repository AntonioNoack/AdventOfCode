package me.anno.aoc23.day14

import me.anno.utils.Utils.readLines

val stone = '#'
val ball = 'O'

fun main() {
    val field = readLines(23, 14, "data.txt")
    val rolled = rollNorthWeight(field)
    // solution: 110407
    println(rolled)
}

fun rollNorthWeight(field: List<String>): Int {
    var sum = 0
    val sx = field[0].length
    val sy = field.size
    for (x in 0 until sx) {
        var balls = 0
        for (y in sy - 1 downTo 0) {
            when (field[y][x]) {
                stone -> {
                    sum += getBallsWeight(balls, y, sy)
                    balls = 0
                }
                ball -> balls++
            }
        }
        sum += getBallsWeight(balls, -1, sy)
    }
    return sum
}

fun getBallsWeight(balls: Int, rockY: Int, sy: Int): Int {
    var sum = 0
    for (i in 0 until balls) {
        val y = rockY + 1 + i
        sum += getBallWeight(y, sy)
    }
    return sum
}

fun getBallWeight(y: Int, sy: Int): Int {
    return sy - y
}