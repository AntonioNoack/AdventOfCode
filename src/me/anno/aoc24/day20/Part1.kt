package me.anno.aoc24.day20

import me.anno.utils.Utils.directions
import me.anno.utils.Utils.findPosition
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import kotlin.math.max
import kotlin.math.min

val wallI = -1
val trackI = 0

fun readIndexField(name: String): List<IntArray> {
    val field0 = readLines(24, 20, name)
    val start = findPosition(field0, 'S')
    val field1 = field0.map {
        IntArray(it.length) { i ->
            if (it[i] == '#') wallI else trackI
        }
    }
    indexPath(start, field1)
    return field1
}

fun main() {
    val field = readIndexField("data.txt")
    if (field.size < 100) for ((y, line) in field.withIndex()) {
        println(line.withIndex().joinToString(" ") { (x, c) ->
            val k = if (c == wallI) -getSkipLength(field, Vector2i(x, y)) else c
            "$k".padStart(3)
        })
    }
    val countSkips = field.withIndex()
        .flatMap { (y, line) ->
            line.withIndex().mapNotNull { (x, c) ->
                if (c == wallI) getSkipLength(field, Vector2i(x, y))
                else null
            }
        }
        .groupBy { it }
        .mapValues { it.value.size }
        .toSortedMap()
    println("skips: ${countSkips.map { "${it.value}x ${it.key}" }}")
    val numSkips100Plus = field.withIndex().sumOf { (y, line) ->
        line.withIndex().count { (x, c) ->
            c == wallI && getSkipLength(field, Vector2i(x, y)) >= 100
        }
    }
    println("skips: $numSkips100Plus")
    // solution: 1455 -> not correct??
    //           1441 -> correct, I was forgetting to subtract 2
}

fun indexPath(start: Vector2i, field: List<IntArray>) {
    var pos = start
    var prev = start
    var distance = 1
    loop@ while (true) {
        for (dir in directions) {
            val next = pos + dir
            if (next != prev && field[next.y][next.x] == trackI) {
                field[next.y][next.x] = distance++
                prev = pos
                pos = next
                continue@loop
            }
        }
        break
    }
}

fun getSkipLength(field: List<IntArray>, pos: Vector2i): Int {
    if (pos.x in 1 until field[0].size - 1 &&
        pos.y in 1 until field.size - 1
    ) {
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        var ctr = 0
        for (dir in directions) {
            val next = pos + dir
            val value = field[next.y][next.x]
            if (value != wallI) {
                min = min(min, value)
                max = max(max, value)
                ctr++
            }
        }
        return if (ctr >= 2) {
            max(max - min - 2, 0)
        } else 0
    } else return 0
}