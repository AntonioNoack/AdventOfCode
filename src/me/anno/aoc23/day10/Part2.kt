package me.anno.aoc23.day10

import me.anno.utils.Utils.findPosition
import me.anno.utils.Vector2i
import me.anno.utils.Utils.readLines
import kotlin.math.max

// up, right, down, left
val sMaskToS = mapOf(
    0b0011 to 'L', // up, right
    0b0101 to '|', // up, down
    0b1001 to 'J', // up, left
    0b0110 to 'F', // right, down
    0b1010 to '-', // right, left
    0b1100 to '7', // down, left
)

fun main() {
    val field = readLines(23, 10, "data.txt")
    val start = findPosition(field, startSymbol)
    val foundTiles = HashMap<Vector2i, Int>()
    val tracker = object : Tracker {
        override fun nextTile(x: Int, y: Int, dir: Int) {
            foundTiles[Vector2i(x, y)] = dir
        }
    }
    var length = 0
    var sMask = 0
    for (i in 0 until 4) {
        foundTiles.clear()
        val lengthI = trackUntilS(field, i, start, tracker)
        if (lengthI > 0) {
            length = lengthI
            sMask += 1 shl i
        }
    }
    assert(length > 0)
    val sType = sMaskToS[sMask]!!
    val inside = max(
        tryFloodFill(foundTiles, field, +1, sType),
        tryFloodFill(foundTiles, field, +3, sType)
    )
    // 649 -> too high :(
    // 411 -> too low
    // 413 -> not correct :(
    // 415???
    println(inside)
}

val inside = 'I'
val outside = 'O'

fun tryFloodFill(foundTiles: Map<Vector2i, Int>, field: List<String>, dirDI: Int, sType: Char): Int {
    val remaining = ArrayList<Vector2i>()
    for ((tile, dir) in foundTiles) {
        // go left and right,
        //  and if free, override the tile there
        // BUG: this is incorrect for curves
        remaining.add(tile + directions[(dir + dirDI).and(3)])
    }
    val field1 = field.map { it.toCharArray() }
    val sx = field[0].length
    val sy = field.size
    for (y in 0 until sy) {
        for (x in 0 until sx) {
            if (Vector2i(x, y) !in foundTiles) {
                field1[y][x] = outside
            } else if (field1[y][x] == 'S') {
                field1[y][x] = sType
            }
        }
    }
    remaining.removeIf { it in foundTiles || it.x !in 0 until sx || it.y !in 0 until sy }
    while (remaining.isNotEmpty()) {
        val next = remaining.removeLast()
        val (x, y) = next
        if (field1[y][x] == inside) continue
        // mark as I
        field1[y][x] = inside
        if (x == 0 || x == sx - 1 || y == 0 || y == sy - 1) {
            return -1
        } else {
            // check neighbors
            fun addMaybe(dx: Int, dy: Int) {
                val x1 = x + dx
                val y1 = y + dy
                if (field1[y1][x1] != inside) {
                    val next2 = Vector2i(x1, y1)
                    if (next2 !in foundTiles) {
                        remaining.add(next2)
                    }
                }
            }
            addMaybe(-1, 0)
            addMaybe(+1, 0)
            addMaybe(0, -1)
            addMaybe(0, +1)
        }
    }

    // println(field1.joinToString("\n") { it.joinToString("") })

    // for all given I/O, check if they are valid:
    val correctnessMatrix = IntArray(4)
    for (y in 0 until sy) {
        var numPipesOnLeftSide = 0
        for (x in 0 until sx) {
            val expectOutside = numPipesOnLeftSide % 2 == 0
            when (val fieldI = field1[y][x]) {
                outside, inside -> {
                    val index = (if (fieldI == outside) 2 else 0) + (if (expectOutside) 1 else 0)
                    correctnessMatrix[index]++
                    // the Xs mark where the first algorithm expected a different value from the point-in-polygon-algorithm
                    //  analysis: the pipes are bowing around it like a star... we expected them outside, but they were inside
                    //  -> yes, because we don't mark them in the algorithm above
                    field1[y][x] =
                        if (expectOutside != (fieldI == outside)) 'X'
                        else if (expectOutside) outside else inside
                }
                '|' -> numPipesOnLeftSide++
                'L', 'J' -> numPipesOnLeftSide++
                'F', '7', '-' -> {}
                else -> throw IllegalStateException("Unknown symbol $fieldI")
            }
        }
    }

    println(correctnessMatrix.joinToString())
    println(field1.joinToString("\n") { it.joinToString("") })

    // 0+2 -> point-in-polygon
    // 0+1 -> flood-fill
    return correctnessMatrix[0] + correctnessMatrix[2]
}
