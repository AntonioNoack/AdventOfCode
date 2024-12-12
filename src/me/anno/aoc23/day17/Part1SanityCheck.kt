package me.anno.aoc23.day17

import me.anno.utils.Utils.readLines

fun main() {
    val field = readLines(23, 17, "sample.txt")
    val solution = """
        2>>34^>>>1323
        32v>>>35v5623
        32552456v>>54
        3446585845v52
        4546657867v>6
        14385987984v4
        44578769877v6
        36378779796v>
        465496798688v
        456467998645v
        12246868655<v
        25465488877v5
        43226746555v>
    """.trimIndent().split('\n')
    println(checkCost(field, solution))
    // that solution is incorrect -> we turned in-place
    val solution2 = """
        24........323
        ...54..53.623
        325524565..54
        3446585845.52
        4546657867..6
        14385987984.4
        44578769877.6
        36378779796..
        465496798688.
        456467998645.
        122468686556.
        254654888773.
        432267465553.
    """.trimIndent().split('\n')
    println(checkCost(field, solution2))
}

fun checkCost(field: List<String>, solution: List<String>): Int {
    var sum = 0
    for (y in field.indices) {
        for (x in field[0].indices) {
            if (solution[y][x] !in '0'..'9') {
                sum += field[y][x] - '0'
            }
        }
    }
    return sum
}