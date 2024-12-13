package me.anno.aoc24.day11

import me.anno.utils.Utils.readText
import java.math.BigInteger

// theoretically, longs are enough; practically, we're very close to having too-big numbers
data class StonePack(val value: BigInteger, val times: BigInteger)

// there are lots and lots of repetitions -> use that fact to our advantage
fun main() {
    var stones = readText(24, 11, "data.txt")
        .split(' ').map { StonePack(BigInteger(it), BigInteger.ONE) }
    // solution: 261936432123724
    for (i in 0 until 75) {
        stones = pack(stones.flatMap(::blink))
        println("${stones.sumOf { it.times }}, [${stones.size}, ${stones.maxOf { it.value }}, ${stones.maxOf { it.times }}]")
    }
}

fun blink(pack: StonePack): List<StonePack> {
    return blink(pack.value)
        .map { StonePack(it, pack.times) }
}

fun pack(packs: List<StonePack>): List<StonePack> {
    val byValue = HashMap<BigInteger, BigInteger>()
    for ((key, times) in packs) {
        val prev = byValue[key]
        byValue[key] = prev?.plus(times) ?: times
    }
    return byValue.map { StonePack(it.key, it.value) }
}