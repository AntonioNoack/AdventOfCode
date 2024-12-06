package me.anno.aoc23.day3

data class GearCandidate(val ax: Int, val ay: Int, val number: Int)
data class GearPosition(val ax: Int, val ay: Int)

fun main() {
    val lines = getPaddedLines(23, 3, "data.txt")
    val candidates = HashSet<GearCandidate>()
    for (y in 1 until lines.size - 1) {
        var x = 1
        val line = lines[y]
        while (x < line.length) {
            if (line[x] in '0'..'9') {
                // collect number
                val x0 = x
                var number = line[x++] - '0'
                while (line[x] in '0'..'9') {
                    number = 10 * number + (line[x++] - '0')
                }
                // check if symbol is adjacent
                // range: x0 until x
                for (yi in y - 1..y + 1) {
                    for (xi in x0 - 1..x) {
                        if (lines[yi][xi] == '*') {
                            candidates += GearCandidate(xi, yi, number)
                        }
                    }
                }
            } else x++
        }
    }
    val sum = candidates
        .groupBy { GearPosition(it.ax, it.ay) }
        .filter { it.value.size == 2 }
        .map { it.value.map { gear -> gear.number } }
        .sumOf { it[0] * it[1] }
    println(sum)
}