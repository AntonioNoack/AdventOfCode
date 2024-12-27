package me.anno.aoc22.day8

import me.anno.utils.Utils.readLines

fun main() {
    val field = readLines(22, 8, "data.txt")
    println(countVisibleTrees(field))
}

fun countVisibleTrees(field: List<String>): Int {
    val sx = field[0].length
    val sy = field.size
    val isVisible = field.map {
        BooleanArray(sx)
    }

    val border = '0' - 1
    var h = border
    fun reset() {
        h = border
    }

    fun check(x: Int, y: Int) {
        val here = field[y][x]
        if (here > h) {
            isVisible[y][x] = true
            h = here
        }
    }
    for (y in 0 until sy) {
        reset()
        for (x in 0 until sx) check(x, y)
        reset()
        for (x in sx - 1 downTo 0) check(x, y)
    }
    for (x in 0 until sx) {
        reset()
        for (y in 0 until sy) check(x, y)
        reset()
        for (y in sy - 1 downTo 0) check(x, y)
    }
    /*for (row in isVisible) {
        println(row.joinToString("") { if (it) "1" else "." })
    }*/
    return isVisible.sumOf { row -> row.count { it } }
}