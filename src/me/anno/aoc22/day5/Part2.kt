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
        applyMove2(move, ships)
    }

    // println(ships)
    println(ships.map { it.last() }.joinToString(""))
}

fun applyMove2(move: Move, ships: List<ArrayList<Char>>) {
    applyMove2(move.howMany, ships[move.from], ships[move.to])
}

fun applyMove2(howMany: Int, from: ArrayList<Char>, to: ArrayList<Char>) {
    val subList = from.subList(from.size-howMany, from.size)
    to.addAll(subList) // add them as duplicates
    subList.clear() // remove the last items
}
