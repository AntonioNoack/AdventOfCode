package me.anno.aoc24.day3

import me.anno.utils.Utils.readText

fun main() {
    // find all multiplications and add them up
    val prefix = "mul("
    val mid = ","
    val suffix = ")"
    val enableInstr = "do()"
    val disableInstr = "don't()"
    val input = readText(24, 3, "data.txt")
    var nextMulIndex = 0
    var sum = 0
    var enabled = true
    var nextEnabledIndex = 0
    while (true) {
        val pi = input.indexOf(prefix, nextMulIndex)
        if (pi < 0) break

        // check enabled-status
        while (true) {
            val nextStateChange = if (enabled) disableInstr else enableInstr
            val ai = input.indexOf(nextStateChange, nextEnabledIndex)
            if (ai in nextEnabledIndex until pi) {
                // state change :)
                enabled = !enabled
                nextEnabledIndex = ai + nextStateChange.length
            } else {
                // state change in the future; mark it already, so it's faster to calculate next time
                // (this part is optional, just breaking is ok)
                if (ai >= 0) {
                    nextEnabledIndex = ai
                }
                break
            }
        }

        nextMulIndex = pi + prefix.length // this is where the next valid sequence could start
        if (!enabled) continue // skip the following parsing & calculation
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