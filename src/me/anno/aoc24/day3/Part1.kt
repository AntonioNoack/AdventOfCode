package me.anno.aoc24.day3

import me.anno.aoc24.utils.Utils.readText

fun main() {
    // find all multiplications and add them up
    val prefix = "mul("
    val mid = ","
    val suffix = ")"
    val input = readText(3, "data.txt")
    var i = 0
    var sum = 0
    while (true) {
        val pi = input.indexOf(prefix, i)
        if (pi < 0) break
        i = pi + prefix.length // this is where the next valid sequence could start
        val pm = input.indexOf(mid, pi + prefix.length + 1) // +1 for shortest viable number
        if (pm < 0) continue // not found -> skip this instruction
        val pj = input.indexOf(suffix, pm + mid.length + 1) // +1 for shortest viable number
        if (pj < 0) continue // not found -> skip this instruction
        val a = input.substring(pi + prefix.length, pm).toIntOrNull() ?: continue
        val b = input.substring(pm + mid.length, pj).toIntOrNull() ?: continue
        sum += a * b
    }
    println(sum)
}