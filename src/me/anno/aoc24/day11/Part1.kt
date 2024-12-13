package me.anno.aoc24.day11

import me.anno.utils.Utils.readText
import java.math.BigInteger

fun main() {
    var stones = readText(24, 11, "data.txt")
        .split(' ').map { BigInteger(it) }
    // 245934 -> too high :(, was using 2048 instead of 2024
    // solution: 220999
    for (i in 0 until 25) {
        stones = stones.flatMap { blink(it) }
        println("${stones.size}, compact: ${stones.toHashSet().size}, max: ${stones.max()}")
    }
}

fun blink(stone: BigInteger): List<BigInteger> {
    if (stone == BigInteger.ZERO) return listOf(BigInteger.ONE)
    val str = stone.toString()
    if (str.length % 2 == 0) {
        val left = str.substring(0, str.length / 2)
        val right = str.substring(str.length / 2)
        return listOf(BigInteger(left), BigInteger(right))
    }
    return listOf(stone * BigInteger.valueOf(2024))
}