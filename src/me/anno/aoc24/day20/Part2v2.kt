package me.anno.aoc24.day20

import me.anno.utils.Utils.getFieldSizeI
import kotlin.math.abs

/**
 * make the solution slightly slower, but more beautiful in return
 * */
fun main() {
    val field = readIndexField("data.txt")
    var foundSkips = 0
    val (sx, sy) = getFieldSizeI(field)
    for (y in 0 until sy) {
        for (x in 0 until sx) {
            if (field[y][x] != wallI) {
                foundSkips += countValidSkips2(field, x, y)
            }
        }
    }
    // solution: 1021490
    println(foundSkips)
}

fun countValidSkips2(field: List<IntArray>, px: Int, py: Int): Int {
    val reference = field[py][px]
    val (sx, sy) = getFieldSizeI(field)
    var numValidSkips = 0
    for (dy in -maxSkips..maxSkips) {
        val dxSteps = maxSkips - abs(dy)
        val ny = py + dy
        if (ny < 0) continue
        if (ny >= sy) break
        for (dx in -dxSteps..dxSteps) {
            val nx = px + dx
            if (nx < 0) continue
            if (nx >= sx) break
            val value = field[py + dy][px + dx] // check for wall isn't needed
            val thisSkip = value - reference - abs(dx) - abs(dy)
            if (thisSkip >= 100) numValidSkips++
        }
    }
    return numValidSkips
}
