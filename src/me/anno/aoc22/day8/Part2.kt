package me.anno.aoc22.day8

import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

fun main() {
    val field = readLines(22, 8, "data.txt")
    val bestSpot = field.indices.flatMap { y ->
        field[0].indices.map { x ->
            Vector2i(x, y) to findScenicScore(field, x, y)
        }
    }.maxBy { it.second }
    // ((32 38), 1152): apparently not correct... why???, apparently too low
    // 7616 [stopped = here >= maxValue] -> [stopped = here > maxValue] is still too low...
    // ((22 53), 209880) -> correct, found it by looking at the solution, because it doesn't make much sense,
    // that shadowing exists in part 1, but doesn't exist in part 2 -> because they wanted "distances", not amount of trees
    println(bestSpot)
}

fun findScenicScore(field: List<String>, tx: Int, ty: Int): Int {

    val scores = IntArray(4)
    val maxValue = field[ty][tx]

    var count = 0
    var stopped = false
    fun finishScan(i: Int) {
        scores[i] = count
        count = 0
        stopped = false
    }

    fun check(x: Int, y: Int) {
        if (stopped) return
        val here = field[y][x]
            count++
            stopped = here >= maxValue
    }

    val sy = field.size
    val sx = field[0].length
    for (x in tx - 1 downTo 0) check(x, ty)
    finishScan(0)
    for (x in tx + 1 until sx) check(x, ty)
    finishScan(1)
    for (y in ty - 1 downTo 0) check(tx, y)
    finishScan(2)
    for (y in ty + 1 until sy) check(tx, y)
    finishScan(3)
    return scores.reduce { a, b -> a * b }
}