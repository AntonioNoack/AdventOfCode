package me.anno.aoc22.day5

import me.anno.utils.Utils.readLines

fun main() {

    val lines = readLines(22, 5, "data.txt")
    val si = lines.indexOf("")
    val ships = parseShips(lines, si)
    // println(ships)

    val moves = lines
        .subList(si + 1, lines.size)
        .map { parseMove(it) }

    // println(moves)

    // execute
    for (move in moves) {
        applyMove(move, ships)
    }

    // println(ships)
    println(ships.map { it.last() }.joinToString(""))
}

fun applyMove(move: Move, ships: List<ArrayList<Char>>) {
    applyMove(move.howMany, ships[move.from], ships[move.to])
}

fun applyMove(howMany: Int, from: ArrayList<Char>, to: ArrayList<Char>) {
    for (i in 0 until howMany) {
        applyMove(from, to)
    }
}

fun applyMove(from: ArrayList<Char>, to: ArrayList<Char>) {
    to.add(from.removeLast())
}

fun parseShips(lines: List<String>, si: Int): List<ArrayList<Char>> {
    val sy = si - 1
    val sx = (lines[sy].length + 2) / 4
    val ships = (0 until sx).map { ArrayList<Char>() }
    for (x in 0 until sx) {
        val ship = ships[x]
        val xi = x * 4 + 1
        for (y in sy - 1 downTo 0) {
            val line = lines[y]
            if (xi >= line.length) break
            val char = line[xi]
            if (char == ' ') break
            ship.add(char)
        }
    }
    return ships
}

data class Move(val howMany: Int, val from: Int, val to: Int)

fun parseMove(line: String): Move {
    val str0 = "move "
    val str1 = " from "
    val str2 = " to "
    val i1 = line.indexOf(str1, str0.length + 1)
    val i2 = line.indexOf(str2, i1 + str1.length + 1)
    val howMany = line.substring(str0.length, i1).toInt()
    val from = line.substring(i1 + str1.length, i2).toInt() - 1
    val to = line.substring(i2 + str2.length).toInt() - 1
    return Move(howMany, from, to)
}