package me.anno.aoc22.day2

import me.anno.utils.Utils.readLines

data class Game2(val other: Play, val result: Result)

val charToResult = Result.entries.associateBy { it.symbol }

fun parseGame2(line: String): Game2 {
    return Game2(charToPlay[line[0] + ('X' - 'A')]!!, charToResult[line[2]]!!)
}

val map = HashMap<Game2, Game>()

fun findWhatIPlayed(game: Game2): Game {
    return map.getOrPut(game) {
        Play.entries.map { whatIPlayedMaybe ->
            Game(game.other, whatIPlayedMaybe)
        }.first { getResult(it) == game.result }
    }
}

fun main() {
    val games = readLines(22, 2, "data.txt")
        .map { parseGame2(it) }
        .map { findWhatIPlayed(it) }
    val scores = games.map { getScore(it.whatIPlayed, getResult(it)) }
    println(scores)
    println(scores.sum())
    // solution: 12411
}