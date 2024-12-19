package me.anno.aoc23.day2

import me.anno.utils.Utils.readLines
import kotlin.math.max


fun main() {
    val games = readLines(23, 2, "data.txt")
        .map { parseGame(it) }
    val totalPower = games
        .sumOf { getGamePower(it) }
    println(totalPower)
}

fun getGamePower(game: Game): Int {
    return getThrowPower(joinThrows(game.throws))
}

fun joinThrows(throws: List<Throw>): Throw {
    val map = HashMap<String, Int>()
    for (throwI in throws) {
        for ((type, num) in throwI) {
            map[type] = max(map[type] ?: 0, num)
        }
    }
    return map
}

fun getThrowPower(throwI: Throw): Int {
    return throwI.values.reduce { a, b -> a * b }
}