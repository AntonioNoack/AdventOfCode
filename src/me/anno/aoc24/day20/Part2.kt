package me.anno.aoc24.day20

import me.anno.utils.Utils.getFieldSizeI
import kotlin.math.abs

fun main() {
    val field = readIndexField("data.txt")
    for ((y, line) in field.withIndex()) {
        for ((x, c) in line.withIndex()) {
            if (c >= trackI /*&& x == 1 && y == 3*/) {
                countValidSkips(field, x, y)
            }
        }
    }
    println(foundSkips)
    // solution: 1021490
}

// previously the limit was 2
val maxSkips = 20
var foundSkips = 0
fun countValidSkips(field: List<IntArray>, px: Int, py: Int) {
    val reference = field[py][px]
    val (sx, sy) = getFieldSizeI(field)
    val sx1 = sx - 1
    val sy1 = sy - 1
    for (dj in 0 .. maxSkips) {
        for (di in 0 .. maxSkips) {
            val needsK = di < maxSkips && dj > 0
            for (dk in 0 until (if (needsK) 2 else 1)) {
                val dx = di - dj + dk
                val dy = di + dj - maxSkips
                val nx = px + dx
                val ny = py + dy
                if (nx in 1 until sx1 && ny in 1 until sy1) {
                    val value = field[ny][nx]
                    // check for wall isn't needed
                    val thisSkip = value - reference - abs(dx) - abs(dy)
                    if (thisSkip >= 100) {
                        foundSkips++
                    }
                }
            }
        }
    }
}
