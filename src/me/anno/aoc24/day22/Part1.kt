package me.anno.aoc24.day22

import me.anno.utils.Utils.readLines

fun main() {
    var sec = 123
    for (i in 0 until 10) {
        sec = nextSecret(sec)
        println(sec)
    }
    val input = readLines(24, 22, "data.txt")
        .map { it.toInt() }
    val thatDay = input
        .map { nextSecret(it, 2000) }
    println(thatDay.sumOf { it.toLong() })
}

fun nextSecret(sec0: Int): Int {
    val prune = 0xffffff
    val sec1 = ((sec0 shl 6) xor sec0) and prune
    val sec2 = (sec1 shr 5) xor sec1
    val sec3 = ((sec2 shl 11) xor sec2) and prune
    return sec3
}

fun nextSecret(sec0: Int, times: Int): Int {
    var sec = sec0
    for (i in 0 until times) {
        sec = nextSecret(sec)
    }
    return sec
}