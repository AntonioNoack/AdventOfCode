package me.anno.aoc23.day4

import me.anno.utils.Utils.readLines

data class Card(val winning: List<Int>, val given: List<Int>)

fun parseNumbers(line: String): List<Int> {
    return line.split(' ')
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
}

fun parseCard(line: String): Card {
    val i0 = line.indexOf(": ")
    val i1 = line.indexOf(" | ")
    val winning = parseNumbers(line.substring(i0 + 2, i1))
    val given = parseNumbers(line.substring(i1 + 3))
    return Card(winning, given)
}

fun isWinning(card: Card, given: Int): Boolean {
    return given in card.winning
}

fun numWinning(card: Card): Int {
    return  card.given.count { isWinning(card, it) }
}

fun getCardScore(card: Card): Long {
    val numWins = numWinning(card)
    return if (numWins > 0) 1L shl (numWins - 1) else 0
}

fun main() {
    val cards = readLines(23, 4, "data.txt")
        .map { parseCard(it) }
    println(cards.sumOf { getCardScore(it) })
    // solution: 27845
}