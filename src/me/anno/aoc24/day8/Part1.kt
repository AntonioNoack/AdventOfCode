package me.anno.aoc24.day8

import me.anno.utils.Utils.readLines

data class Antenna(val x: Int, val y: Int, val symbol: Char)

fun isAntenna(char: Char): Boolean {
    return char in '0' until '9' || char in 'A'..'Z' || char in 'a'..'z'
}

fun collectAntennas(field: List<String>): List<Antenna> {
    return field.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (isAntenna(c)) {
                Antenna(x, y, c)
            } else null
        }
    }
}

val antinode = '#'

/**
 * count unique positions of antinodes
 * todo fix this, and solve part 2
 * */
fun main() {
    val field = readLines(24, 8, "data.txt")

    // collect antennas
    val allAntennas = collectAntennas(field)
    val antinodeField = field.map { line ->
        line.toCharArray()
    }

    // collect antinodes
    val sx = field[0].length
    val sy = field.size

    fun markAntinode(a: Antenna, b: Antenna) {
        val x = 2 * a.x - b.x
        val y = 2 * a.y - b.y
        if (x in 0 until sx && y in 0 until sy) {
            antinodeField[y][x] = antinode
        }
    }

    for ((_, antennas) in allAntennas.groupBy { it.symbol }) {
        for (a in antennas) {
            for (b in antennas) {
                if (a != b) {
                    markAntinode(a, b)
                }
            }
        }
    }

    val numAntinodes = antinodeField.sumOf { line ->
        line.count { it == antinode }
    }

    for (line in antinodeField) {
        println(line.joinToString(""))
    }
    // 330 -> too low :(
    println(numAntinodes)
}