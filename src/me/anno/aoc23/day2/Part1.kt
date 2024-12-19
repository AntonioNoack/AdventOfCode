package me.anno.aoc23.day2

import me.anno.utils.Utils.readLines

val budget = mapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14
)

fun main() {
    val games = readLines(23, 2, "data.txt")
        .map { parseGame(it) }
    val possible = games
        .filter { isGamePossible(it) }
        .sumOf { it.id }
    println(possible)
}

fun isGamePossible(game: Game): Boolean {
    return game.throws.all { isThrowPossible(it) }
}

fun parseGame(line: String): Game {
    val ci = line.indexOf(": ")
    val id = line.substring(5, ci).toInt()
    val parts = line.substring(ci + 2)
        .split("; ")
        .map { parseThrow(it) }
    return Game(id, parts)
}

fun isThrowPossible(throwI: Throw): Boolean {
    return throwI.all { (type, required) ->
        budget[type]!! >= required
    }
}

fun parseThrow(line: String): Throw {
    val parts = line.split(", ")
    return parts.associate {
        val i = it.indexOf(' ')
        val required = it.substring(0, i).toInt()
        val type = it.substring(i + 1)
        type to required
    }
}