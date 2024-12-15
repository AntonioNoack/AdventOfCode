package me.anno.aoc23.day21

import me.anno.utils.Utils.findPosition
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import kotlin.math.abs
import kotlin.math.max

fun main() {
    // num steps: 26M, max effort: pi * (26M)² -> too much
    // we need to cache information... after a field has been conquered,
    //  there is only two changing counts

    // solution: 637087163925555
    println(countReachableFields2(readLines(23, 21, "data.txt"), 26501365))
}

fun countReachableFields2(field: List<String>, maxSteps: Int): Long {
    val start = findPosition(field, startSymbol) // exactly the center
    val sx = field[0].length
    val sy = field.size
    // check if edge is visible from start
    println("${(0 until sx).count { x -> field[start.y][x] != rock }} / $sx")
    println("${(0 until sy).count { y -> field[y][start.x] != rock }} / $sy")
    println(start)
    // yes, edge is visible, so the first point reaching a field will always be that "center",
    // or a corner for corner tiles...

    // we could optimize it further by deciding that a tile will be completely filled after a certain point,
    // and just calculate them
    val numReachedCache = HashMap<CacheKey, Int>()
    val maxNumChunks = (maxSteps + sx) / sx
    var totalSum = 0L
    for (cy in -maxNumChunks..maxNumChunks) {
        if (cy % 1000 == 0) println("$cy/$maxNumChunks, ${numReachedCache.size}")
        for (cx in -maxNumChunks..maxNumChunks) {
            val typeX = sign(cx)
            val typeY = sign(cy)
            val whenWillBeReached = if (cx != 0 && cy != 0) {
                whenWillCornerBeReached(cx, cy)
            } else if (cx != 0 || cy != 0) {
                whenWillEdgeBeReached(cx, cy)
            } else {
                0
            }

            var numSteps = maxSteps - whenWillBeReached
            if (numSteps >= 0) {
                // if steps > 2*sx, remove those
                val canBeRemoved = 2 * sx
                val dx = numSteps / canBeRemoved - 1
                if (dx > 0) {
                    numSteps -= dx * canBeRemoved
                }
                totalSum += numReachedCache.getOrPut(CacheKey(typeX, typeY, numSteps)) {
                    val startX = when (typeX) {
                        -1 -> sx - 1
                        0 -> hx
                        else -> 0
                    }
                    val startY = when (typeY) {
                        -1 -> sy - 1
                        0 -> hy
                        else -> 0
                    }
                    val startI = Vector2i(startX, startY)
                    countReachableFields(field, numSteps, startI)
                }
            }
        }
    }

    return totalSum
}

data class CacheKey(val tx: Int, val ty: Int, val numSteps: Int)

fun sign(i: Int): Int {
    return when {
        i < 0 -> -1
        i == 0 -> 0
        else -> +1
    }
}

val sx = 131
val sy = 131

val hx = sx / 2
val hy = sy / 2

fun whenWillCornerBeReached(cx: Int, cy: Int): Int {
    val posX = abs(cx) * sx - hx
    val posY = abs(cy) * sy - hy
    return posX + posY
}

fun whenWillEdgeBeReached(cx: Int, cy: Int): Int {
    return max(abs(cx), abs(cy)) * sx - hx
}
