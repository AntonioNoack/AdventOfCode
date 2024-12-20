package me.anno.aoc22.day2

import me.anno.utils.Utils.readLines

enum class Play(val symbol: Char, val score: Int) {
    ROCK('X', 1),
    PAPER('Y', 2),
    SCISSORS('Z', 3)
}

enum class Result(val symbol: Char, val score: Int) {
    LOST('X', 0),
    DRAW('Y', 3),
    WON('Z', 6),
}

fun getScore(whatIPlayed: Play, result: Result): Int {
    return whatIPlayed.score + result.score
}

data class Game(val other: Play, val whatIPlayed: Play)

val charToPlay = Play.entries.associateBy { it.symbol }

fun parseGame(line: String): Game {
    return Game(charToPlay[line[0] + ('X' - 'A')]!!, charToPlay[line[2]]!!)
}

fun getResult(game: Game): Result {
    val delta = (game.other.ordinal - game.whatIPlayed.ordinal + 3) % 3
    return when (delta) {
        0 -> Result.DRAW
        1 -> Result.LOST
        else -> Result.WON
    }
}

fun main() {
    val games = readLines(22, 2, "data.txt")
        .map { parseGame(it) }
    val scores = games.map { getScore(it.whatIPlayed, getResult(it)) }
    println(scores)
    println(scores.sum())
    // solution: 14069
}