package me.anno.aoc24.day21

import me.anno.utils.Utils.readLines


fun main() {
    // this finds the solution, but is much too slow...,
    // because it tries ~10^25 combinations...
    val lines = readLines(24, 21, "data.txt")
    // [28,66,160,398,982,2442]
    for (line in lines) {
        println(line)
        for (k in 1..25) {
            val t0 = System.nanoTime()
            val kp0 = Keypad(numericKeys)
            val keypads = (0 until k)
                .map { Keypad(arrowKeys) } + kp0
            val steps = findShortestTranslation(keypads, line)
            val t1 = System.nanoTime()
            println("[$k] $steps, ${(t1-t0)/1e9f}s")
        }
    }
}
